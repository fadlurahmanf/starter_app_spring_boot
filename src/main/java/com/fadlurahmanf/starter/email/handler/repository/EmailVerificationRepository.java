package com.fadlurahmanf.starter.email.handler.repository;

import com.fadlurahmanf.starter.email.dto.entity.EmailVerificationEntity;
import com.fadlurahmanf.starter.general.constant.EntityConstant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface EmailVerificationRepository extends JpaRepository<EmailVerificationEntity, Long> {

    @Query(value = "SELECT * FROM `" + EntityConstant.emailVerification + "` WHERE token = :token", nativeQuery = true, countQuery = "SELECT 1")
    Optional<EmailVerificationEntity> findByToken(@Param("token") String token);

    @Query(value = "SELECT * FROM `" + EntityConstant.emailVerification + "` WHERE email=:email", nativeQuery = true, countQuery = "SELECT 1")
    List<EmailVerificationEntity> findByEmail(@Param("email") String email);

    @Modifying
    @Query(value = "UPDATE `email-verification` SET is_verified = true WHERE token = :token", nativeQuery = true, countQuery = "SELECT 1")
    void updateIsVerifiedEmailVerification(@Param("token") String token);
}
