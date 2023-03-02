package com.fadlurahmanf.starter.identity.handler.repository;

import com.fadlurahmanf.starter.identity.dto.entity.IdentityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IdentityRepository extends JpaRepository<IdentityEntity, Long> {
    
}
