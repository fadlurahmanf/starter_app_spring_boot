package com.fadlurahmanf.starter.identity.handler.service;

import com.fadlurahmanf.starter.identity.dto.entity.IdentityEntity;
import com.fadlurahmanf.starter.identity.handler.repository.IdentityRepository;
import com.fadlurahmanf.starter.jwt.handler.JwtUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
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
    JwtUserDetailService jwtUserDetailService;

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

    public UserDetails getUserDetails(String email) throws UsernameNotFoundException {
        return jwtUserDetailService.loadUserByUsername(email);
    }
}
