package com.fadlurahmanf.starter.identity.handler.service;

import com.fadlurahmanf.starter.identity.dto.entity.IdentityEntity;
import com.fadlurahmanf.starter.identity.handler.repository.IdentityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IdentityService {
    @Autowired
    IdentityRepository identityRepository;

    public List<IdentityEntity> findAll(){
        return identityRepository.findAll();
    }

    public Boolean isUserExistByEmail(String email){
        Optional<IdentityEntity> optIdentity = identityRepository.findByEmail(email);
        return optIdentity.isPresent();
    }

    public void saveIdentity(String email, String password){
        identityRepository.save(new IdentityEntity(email, password));
    }
}
