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

    @Query(value = "SELECT * FROM `" + EntityConstant.verificationEmail + "` WHERE email_token = :token", nativeQuery = true, countQuery = "SELECT 1")
    Optional<EmailVerificationEntity> findByToken(@Param("token") String token);

    @Query(value = "SELECT * FROM `" + EntityConstant.verificationEmail + "` WHERE email=:email", nativeQuery = true, countQuery = "SELECT 1")
    List<EmailVerificationEntity> findByEmail(@Param("email") String email);

    @Modifying
    @Query(value = "UPDATE `" + EntityConstant.verificationEmail + "` SET is_verified = true WHERE email_token = :token", nativeQuery = true, countQuery = "SELECT 1")
    void updateIsVerifiedEmailVerification(@Param("token") String token);

    @Modifying
    @Query(value = "UPDATE `" + EntityConstant.verificationEmail + "` SET is_verified = true WHERE email_token = :token", nativeQuery = true, countQuery = "SELECT 1")
    void updateIsExpiredByEmail(@Param("token") String token);

    @Query(value = "INSERT INTO `email-user` (`email`, `email_type`, `token`, `is_verified`, `expired_at`) VALUES (:email, :emailType, :token, :isVerified, :expiredAt)", nativeQuery = true, countQuery = "SELECT 1")
    void saveManual(@Param("email") String email, @Param("emailType") String type, @Param("token") String token, @Param("isVerified") Boolean isVerified, @Param("expiredAt") String expiredAt);
}
