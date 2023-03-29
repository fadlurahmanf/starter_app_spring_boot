package com.fadlurahmanf.starter.product.handler.respository;

import com.fadlurahmanf.starter.product.dto.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query(value = "SELECT * FROM `product` WHERE id = :productId LIMIT 1", nativeQuery = true, countQuery = "SELECT 1")
    Optional<ProductEntity> getProductByProductId(@Param("productId") String productId);

    @Modifying
    @Query(value = "UPDATE `product` SET total_product = total_product - 1 WHERE id = :productId", nativeQuery = true, countQuery = "SELECT 1")
    void removeProductByProductId(@Param("productId") String productId);
}
