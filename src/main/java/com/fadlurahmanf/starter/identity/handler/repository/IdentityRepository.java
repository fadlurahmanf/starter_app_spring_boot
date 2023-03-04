package com.fadlurahmanf.starter.identity.handler.repository;

import com.fadlurahmanf.starter.general.constant.EntityConstant;
import com.fadlurahmanf.starter.identity.dto.entity.IdentityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface IdentityRepository extends JpaRepository<IdentityEntity, Long> {
    @Query(value = "SELECT * FROM `" + EntityConstant.identity + "` WHERE email = :email LIMIT 1", nativeQuery = true, countQuery = "SELECT 1")
    public Optional<IdentityEntity> findByEmail(@Param("email") String email);

}
