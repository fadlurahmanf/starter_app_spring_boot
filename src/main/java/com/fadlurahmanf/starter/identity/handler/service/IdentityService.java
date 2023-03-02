package com.fadlurahmanf.starter.identity.handler.service;

import com.fadlurahmanf.starter.identity.dto.entity.IdentityEntity;
import com.fadlurahmanf.starter.identity.handler.repository.IdentityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IdentityService {
    @Autowired
    IdentityRepository identityRepository;

    public List<IdentityEntity> findAll(){
        return identityRepository.findAll();
    }
}
