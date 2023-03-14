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
    @Query(value = "SELECT * FROM `" + EntityConstant.identity + "` WHERE email = :email LIMIT 1", nativeQuery = true, countQuery = "SELECT 1")
    Optional<IdentityEntity> findByEmail(@Param("email") String email);

    @Modifying
    @Query(value = "UPDATE `" + EntityConstant.identity + "` SET status = :status WHERE email = :email", nativeQuery = true, countQuery = "SELECT 1")
    void updateStatusIdentity(@Param("status") String status, @Param("email") String email);

    @Modifying
    @Query(value = "UPDATE `" + EntityConstant.identity + "` SET status = :status, password = :password, created_at = CURRENT_TIMESTAMP WHERE email = :email", nativeQuery = true, countQuery = "SELECT 1")
    void updateIdentity(@Param("status") String status, @Param("email") String email, @Param("password") String password);

    @Modifying
    @Query(value = "UPDATE `" + EntityConstant.identity + "` SET balance = :balance WHERE email = :email", nativeQuery = true, countQuery = "SELECT 1")
    void updateBalance(@Param("balance") Double balance, @Param("email") String email);

}
