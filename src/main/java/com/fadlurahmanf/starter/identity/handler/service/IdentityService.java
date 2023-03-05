package com.fadlurahmanf.starter.identity.handler.service;

import com.fadlurahmanf.starter.general.constant.MessageConstant;
import com.fadlurahmanf.starter.general.dto.exception.CustomException;
import com.fadlurahmanf.starter.identity.dto.entity.IdentityEntity;
import com.fadlurahmanf.starter.identity.handler.repository.IdentityRepository;
import com.fadlurahmanf.starter.jwt.handler.JWTTokenUtil;
import com.fadlurahmanf.starter.jwt.handler.JWTUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IdentityService {
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

    public Boolean isUserExistByEmail(String email){
        Optional<IdentityEntity> optIdentity = identityRepository.findByEmail(email);
        return optIdentity.isPresent();
    }

    public void saveIdentity(String email, String unEncryptedPassword){
        String encryptedPassword =bCryptPasswordEncoder.encode(unEncryptedPassword);
        identityRepository.save(new IdentityEntity(email, encryptedPassword));
    }

    public String authenticate(String email, String password) throws CustomException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            UserDetails userDetails = jwtUserDetailService.loadUserByUsername(email);
            return jwtTokenUtil.generateToken(userDetails);
        }catch (BadCredentialsException e){
            throw new CustomException(MessageConstant.BAD_CREDENTIAL, HttpStatus.UNAUTHORIZED);
        }
    }

    public UserDetails getUserDetails(String email) throws UsernameNotFoundException {
        return jwtUserDetailService.loadUserByUsername(email);
    }
}
