package com.fadlurahmanf.starter.identity.handler.service;

import com.fadlurahmanf.starter.general.constant.MessageConstant;
import com.fadlurahmanf.starter.general.dto.exception.CustomException;
import com.fadlurahmanf.starter.identity.dto.entity.IdentityEntity;
import com.fadlurahmanf.starter.identity.dto.response.LoginResponse;
import com.fadlurahmanf.starter.identity.handler.repository.IdentityRepository;
import com.fadlurahmanf.starter.jwt.handler.JWTTokenUtil;
import com.fadlurahmanf.starter.jwt.handler.JWTUserDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class IdentityService{
    Logger logger = LoggerFactory.getLogger(IdentityService.class);
    @Autowired
    IdentityRepository identityRepository;

    @Autowired
    JWTUserDetailService jwtUserDetailService;

    @Autowired
    JWTTokenUtil jwtTokenUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<IdentityEntity> findAll(){
        return identityRepository.findAll();
    }

    public Optional<IdentityEntity> findByEmail(String email){
        return identityRepository.findByEmail(email);
    }
    public Optional<IdentityEntity> findById(String id){
        return identityRepository.findByUserId(id);
    }

    public Boolean isUserExistByEmail(String email){
        Optional<IdentityEntity> optIdentity = identityRepository.findByEmail(email);
        return optIdentity.isPresent();
    }

    public String getStatusByEmail(String email) throws CustomException {
        Optional<IdentityEntity> optIdentity = identityRepository.findByEmail(email);
        if(optIdentity.isPresent()){
            IdentityEntity identity = optIdentity.get();
            return identity.status;
        }
        return "";
    }

    public void saveNewIdentity(String email, String unEncryptedPassword){
        String encryptedPassword = bCryptPasswordEncoder.encode(unEncryptedPassword);
        identityRepository.save(new IdentityEntity(email, encryptedPassword, 0.0));
    }

    public LoginResponse authenticate(String email, String password) throws CustomException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            UserDetails userDetails = jwtUserDetailService.loadUserByUsername(email);
            String accessToken = jwtTokenUtil.generateToken(userDetails);
            String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);
            return new LoginResponse(accessToken, refreshToken);
        }catch (BadCredentialsException e){
            throw new CustomException(MessageConstant.BAD_CREDENTIAL, HttpStatus.UNAUTHORIZED);
        }
    }

    public void updateStatusIdentity(String status, String email){
        identityRepository.updateStatusIdentity(status, email);
    }

    public void updateIdentity(String status, String email, String unEncryptedPassword){
        String encryptedPassword = bCryptPasswordEncoder.encode(unEncryptedPassword);
        identityRepository.updateIdentity(status, email, encryptedPassword);
    }

    public LoginResponse authenticateRefreshToken(String refreshToken) throws CustomException {
        try {
            Boolean isValidRefreshToken = jwtTokenUtil.validateRefreshToken(refreshToken);
            if(!isValidRefreshToken){
                throw new CustomException(MessageConstant.REFRESH_TOKEN_NOT_VALID);
            }
            String username = jwtTokenUtil.getEmailFromToken(refreshToken);
            final UserDetails userDetails = jwtUserDetailService.loadUserByUsername(username);
            String newAccessToken = jwtTokenUtil.generateToken(userDetails);
            String newRefreshToken = jwtTokenUtil.generateRefreshToken(userDetails);
            return new LoginResponse(newAccessToken, newRefreshToken);
        }catch (CustomException e){
            throw e;
        }catch (Exception e){
            if(e.getMessage() != null && e.getMessage().toLowerCase().contains("EXPIRED".toLowerCase())){
                throw new CustomException(MessageConstant.TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED);
            }else{
                throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    public String getUserIdFromToken(String authorizationToken) throws CustomException{
        if(!authorizationToken.startsWith("Bearer ")){
            throw new CustomException(MessageConstant.TOKEN_NOT_WITH_BEARER, HttpStatus.UNAUTHORIZED);
        }
        String token = authorizationToken.substring(7);
        String email = jwtTokenUtil.getEmailFromToken(token);
        Optional<IdentityEntity> optIdentity = identityRepository.findByEmail(email);
        if(optIdentity.isEmpty()){
            throw new CustomException(MessageConstant.USER_NOT_EXIST, HttpStatus.UNAUTHORIZED);
        }
        return optIdentity.get().id;
    }

    public IdentityEntity getIdentityFromToken(String authorizationToken) throws CustomException{
        String userId = getUserIdFromToken(authorizationToken);
        Optional<IdentityEntity> optIdentity = findById(userId);
        if(optIdentity.isEmpty()){
            throw new CustomException(MessageConstant.USER_NOT_EXIST, HttpStatus.UNAUTHORIZED);
        }
        return optIdentity.get();
    }

    public UserDetails getUserDetails(String email) throws UsernameNotFoundException {
        return jwtUserDetailService.loadUserByUsername(email);
    }

    public void updateBalance(String email, Double balance){
        identityRepository.updateBalance(balance, email);
    }

    @CachePut(value = "fcm", key = "#userId")
    public String updateFCMTokenWithCache(String userId, String token){
        identityRepository.updateFCMTokenByUserId(userId, token);
        return token;
    }

    public void updateFCMToken(String userId, String token){
        identityRepository.updateFCMTokenByUserId(userId, token);
    }

    @Cacheable(value = "fcm", key = "#userId")
    public String getFCMToken(String userId){
        Optional<IdentityEntity> optIdentity = findById(userId);
        if(optIdentity.isEmpty()){
            return "KOSONG";
        }else{
            return "BERHASIL NIHHH " + optIdentity.get().fcmToken;
        }
    }

    @Autowired
    RedisLockRegistry redisLockRegistry;

    private static final String MY_LOCK_KEY = "BALANCE-LOCK-KEY";

    public void updateBalance(){
        var executor = Executors.newFixedThreadPool(2);
        Runnable lockThreadOne = () -> {
            UUID uuid = UUID.randomUUID();
            var lock = redisLockRegistry.obtain(MY_LOCK_KEY);
            try {
                System.out.println("Attempting to lock with thread: " + uuid);
                if(lock.tryLock(1, TimeUnit.SECONDS)){
                    System.out.println("Locked with thread: " + uuid);
                    Thread.sleep(5000);
                }
                else{
                    System.out.println("failed to lock with thread: " + uuid);
                }
            } catch(Exception e0){
                System.out.println("exception thrown with thread: " + uuid);
            } finally {
                lock.unlock();
                System.out.println("unlocked with thread: " + uuid);
            }
        };
        Runnable lockThreadTwo = () -> {
            UUID uuid = UUID.randomUUID();
            var lock = redisLockRegistry.obtain(MY_LOCK_KEY);
            try {
                System.out.println("Attempting to lock with thread: " + uuid);
                if(lock.tryLock(10, TimeUnit.SECONDS)){
                    System.out.println("Locked with thread: " + uuid);
                    Thread.sleep(5000);
                }
                else{
                    System.out.println("failed to lock with thread: " + uuid);
                }
            } catch(Exception e0){
                System.out.println("exception thrown with thread: " + uuid);
            } finally {
                lock.unlock();
                System.out.println("unlocked with thread: " + uuid);
            }
        };
        executor.submit(lockThreadOne);
        executor.submit(lockThreadTwo);
        executor.shutdown();
    }
}
