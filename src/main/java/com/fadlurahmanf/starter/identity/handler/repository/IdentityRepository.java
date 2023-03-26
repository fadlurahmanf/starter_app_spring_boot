package com.fadlurahmanf.starter.identity.handler.repository;

import com.fadlurahmanf.starter.general.constant.EntityConstant;
import com.fadlurahmanf.starter.identity.dto.entity.IdentityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface IdentityRepository extends JpaRepository<IdentityEntity, Long> {
    @Query(value = "SELECT * FROM `" + EntityConstant.Identity.entity + "` WHERE " + EntityConstant.Identity.email + " = :email LIMIT 1", nativeQuery = true, countQuery = "SELECT 1")
    Optional<IdentityEntity> findByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM `" + EntityConstant.Identity.entity + "` WHERE " + EntityConstant.Identity.id + " = :id LIMIT 1", nativeQuery = true, countQuery = "SELECT 1")
    Optional<IdentityEntity> findByUserId(@Param("id") String email);

    @Modifying
    @Query(value = "UPDATE `" + EntityConstant.Identity.entity + "` SET " + EntityConstant.Identity.status + " = :status WHERE " + EntityConstant.Identity.email + " = :email", nativeQuery = true, countQuery = "SELECT 1")
    void updateStatusIdentity(@Param("status") String status, @Param("email") String email);

    @Modifying
    @Query(value = "UPDATE `" + EntityConstant.Identity.entity + "` SET " + EntityConstant.Identity.status + " = :status, " + EntityConstant.Identity.password + " = :password, " + EntityConstant.Identity.createdAt + " = CURRENT_TIMESTAMP WHERE " + EntityConstant.Identity.email + " = :email", nativeQuery = true, countQuery = "SELECT 1")
    void updateStatusAndPasswordByEmail(@Param("status") String status, @Param("email") String email, @Param("password") String password);

    @Modifying
    @Query(value = "UPDATE `" + EntityConstant.Identity.entity + "` SET " + EntityConstant.Identity.pin + " = :pin WHERE " + EntityConstant.Identity.id + " = :id", nativeQuery = true, countQuery = "SELECT 1")
    void updatePinByUserId(@Param("id") String id, @Param("pin") String pin);

    @Modifying
    @Query(value = "UPDATE `" + EntityConstant.Identity.entity + "` SET " + EntityConstant.Identity.balance + " = :balance WHERE " + EntityConstant.Identity.email + " = :email", nativeQuery = true, countQuery = "SELECT 1")
    void updateBalanceByEmail(@Param("balance") Double balance, @Param("email") String email);

    @Modifying
    @Query(value = "UPDATE `" + EntityConstant.Identity.entity + "` SET balance = :balance WHERE " + EntityConstant.Identity.id + " = :id", nativeQuery = true, countQuery = "SELECT 1")
    void updateBalanceByUserId(@Param("balance") Double balance, @Param("id") String email);

    @Modifying
    @Query(value = "UPDATE `" + EntityConstant.Identity.entity + "` SET " + EntityConstant.Identity.fcmToken + " = :token WHERE " + EntityConstant.Identity.id + " = :id", nativeQuery = true, countQuery = "SELECT 1")
    void updateFCMTokenByUserId(@Param("id") String id, @Param("token") String token);

    @Modifying
    @Query(value = "UPDATE `" + EntityConstant.Identity.entity + "` SET " + EntityConstant.Identity.balance + " = (" + EntityConstant.Identity.balance + " - (:value)) WHERE " + EntityConstant.Identity.id + " = :id AND (" + EntityConstant.Identity.balance + " - (:value) >= 0)", nativeQuery = true, countQuery = "SELECT 1")
    void reduceBalanceByUserId(@Param("id") String id, @Param("value") Double balance);


}
