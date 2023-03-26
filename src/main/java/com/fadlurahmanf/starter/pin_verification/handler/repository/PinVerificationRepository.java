package com.fadlurahmanf.starter.pin_verification.handler.repository;

import com.fadlurahmanf.starter.general.constant.EntityConstant;
import com.fadlurahmanf.starter.pin_verification.dto.entity.PinVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface PinVerificationRepository extends JpaRepository<PinVerificationEntity, Long> {
    @Query(value = "SELECT * FROM `" + EntityConstant.PinVerification.entity + "` WHERE " + EntityConstant.PinVerification.pinToken + " = :pinToken AND " + EntityConstant.PinVerification.userId + " = :userId LIMIT 1", nativeQuery = true, countQuery = "SELECT 1")
    Optional<PinVerificationEntity> getByPinToken(@Param("userId") String userId, @Param("pinToken") String pinToken);

    @Modifying
    @Query(value = "REPLACE INTO `" + EntityConstant.PinVerification.entity + "`(" + EntityConstant.PinVerification.userId + ", " + EntityConstant.PinVerification.pinToken + ", " + EntityConstant.PinVerification.isUsed + ") VALUES(:userId, :pinToken, :isUsed)", nativeQuery = true, countQuery = "SELECT 1")
    void replacePinTokenByUserId(@Param("userId") String userId, @Param("pinToken") String pinToken, @Param("isUsed") Boolean isUsed);

    @Modifying
    @Query(value = "UPDATE `" + EntityConstant.PinVerification.entity + "` SET " + EntityConstant.PinVerification.isUsed + " = :isUsed WHERE " + EntityConstant.PinVerification.pinToken + " = :pinToken", nativeQuery = true, countQuery = "SELECT 1")
    void setUsedPinByPinToken(@Param("pinToken") String pinToken, @Param("isUsed") Boolean isUsed);
}
