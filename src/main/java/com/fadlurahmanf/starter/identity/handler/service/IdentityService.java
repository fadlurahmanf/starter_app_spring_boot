package com.fadlurahmanf.starter.identity.handler.service;

import com.fadlurahmanf.starter.general.constant.MessageConstant;
import com.fadlurahmanf.starter.general.dto.exception.CustomException;
import com.fadlurahmanf.starter.identity.constant.IdentityStatusConstant;
import com.fadlurahmanf.starter.identity.dto.entity.IdentityEntity;
import com.fadlurahmanf.starter.identity.dto.response.LoginResponse;
import com.fadlurahmanf.starter.identity.handler.repository.IdentityRepository;
import com.fadlurahmanf.starter.jwt.handler.JWTTokenUtil;
import com.fadlurahmanf.starter.jwt.handler.JWTUserDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

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

    @Autowired
    RedisLockRegistry redisLockRegistry;

    private static final String MY_LOCK_KEY = "BALANCE-LOCK-KEY";

    public List<IdentityEntity> getAllIdentity(){
        return identityRepository.findAll();
    }

    public Optional<IdentityEntity> getOptionalIdentityByEmail(String email){
        return identityRepository.findByEmail(email);
    }
    public Optional<IdentityEntity> getOptionalIdentityByUserId(String id){
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

    public void updateStatusAndPasswordByEmail(String status, String email, String unEncryptedPassword){
        String encryptedPassword = bCryptPasswordEncoder.encode(unEncryptedPassword);
        identityRepository.updateStatusAndPasswordByEmail(status, email, encryptedPassword);
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

    public String authenticatePin(String authorizationToken, String pin) throws CustomException {
        IdentityEntity identity = getActiveIdentityFromToken(authorizationToken);
        if(bCryptPasswordEncoder.matches(pin, identity.pin)){
            return pin;
        }else{
            throw new CustomException(MessageConstant.PIN_NOT_VALID, HttpStatus.UNAUTHORIZED);
        }
    }

    public void saveNewPin(String authorizationToken, String rawPin) throws CustomException {
        if(rawPin.length() != 6){
            throw new CustomException(MessageConstant.TOKEN_NOT_VALID, HttpStatus.BAD_REQUEST);
        }
        IdentityEntity identity = getActiveIdentityFromToken(authorizationToken);
        String encryptedPin = bCryptPasswordEncoder.encode(rawPin);
        identityRepository.updatePinByUserId(identity.id, encryptedPin);
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
        Optional<IdentityEntity> optIdentity = getOptionalIdentityByUserId(userId);
        if(optIdentity.isEmpty()){
            throw new CustomException(MessageConstant.USER_NOT_EXIST, HttpStatus.UNAUTHORIZED);
        }
        return optIdentity.get();
    }

    public IdentityEntity getActiveIdentityFromToken(String authorizationToken) throws CustomException{
        String userId = getUserIdFromToken(authorizationToken);
        Optional<IdentityEntity> optIdentity = getOptionalIdentityByUserId(userId);
        if(optIdentity.isEmpty()){
            throw new CustomException(MessageConstant.USER_NOT_EXIST, HttpStatus.UNAUTHORIZED);
        }
        if(Objects.equals(optIdentity.get().status, IdentityStatusConstant.ACTIVE)){
            return optIdentity.get();
        }else{
            throw new CustomException(MessageConstant.USER_NOT_ACTIVE_YET, HttpStatus.UNAUTHORIZED);
        }
    }

    public UserDetails getUserDetails(String email) throws UsernameNotFoundException {
        return jwtUserDetailService.loadUserByUsername(email);
    }

    public void updateBalanceByEmail(String email, Double balance){
        identityRepository.updateBalanceByEmail(balance, email);
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
        Optional<IdentityEntity> optIdentity = getOptionalIdentityByUserId(userId);
        if(optIdentity.isEmpty()){
            return "KOSONG";
        }else{
            return "BERHASIL NIHHH " + optIdentity.get().fcmToken;
        }
    }

    public IdentityEntity getIdentityByUserId(String userId) throws CustomException{
        Optional<IdentityEntity> optIdentity = getOptionalIdentityByUserId(userId);
        if(optIdentity.isEmpty()){
            throw new CustomException(MessageConstant.USER_NOT_EXIST, HttpStatus.OK);
        }
        return optIdentity.get();
    }

    public Optional<IdentityEntity> getOptIdentityByUserId(String userId){
        return getOptionalIdentityByUserId(userId);
    }

    public void updateBalanceByUserId(String userId, Double balance){
        identityRepository.updateBalanceByUserId(balance, userId);
    }

    public void reduceBalanceByUserId(String fromUserId, String toUserId, Double balance) throws CustomException {
        Lock lock = redisLockRegistry.obtain(MY_LOCK_KEY);
        IdentityEntity fromIdentity = getIdentityByUserId(fromUserId);
        try {
            logger.info("ATTEMPTED TRY LOCK REDUCE BALANCE " + balance.toString() + " BY " + fromIdentity.email);
            if(lock.tryLock(10, TimeUnit.SECONDS)){
                logger.info("LOCKED BY " + fromIdentity.email);
                IdentityEntity toIdentity = getIdentityByUserId(toUserId);
                if((toIdentity.balance - balance) < 0.0){
                    throw new CustomException(MessageConstant.BALANCE_NOT_ENOUGH, HttpStatus.BAD_REQUEST);
                }
                identityRepository.reduceBalanceByUserId(toUserId, balance);
            }else{
                logger.info("FAILED TO LOCK BY " + fromIdentity.email);
                throw new CustomException(MessageConstant.LOCKING_FROM_ANOTHER_THREAD, HttpStatus.CONFLICT);
            }
        }catch (CustomException e){
            throw e;
        }catch (Exception e){
            logger.error("CATCHING ERROR TRY LOCK BY " + fromIdentity.email);
            logger.error(e.getMessage());
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            lock.unlock();
            logger.info("UNLOCKED BY " + fromIdentity.email);
        }
    }

    public void addBalanceByUserId(String userId, Double balance) throws CustomException {
        IdentityEntity identity = getIdentityByUserId(userId);
        identityRepository.reduceBalanceByUserId(userId, balance);
    }

    public void redisLockExample(){
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
