package com.fadlurahmanf.starter.product.handler.service;

import com.fadlurahmanf.starter.general.constant.MessageConstant;
import com.fadlurahmanf.starter.general.dto.exception.CustomException;
import com.fadlurahmanf.starter.identity.dto.entity.IdentityEntity;
import com.fadlurahmanf.starter.identity.handler.service.IdentityService;
import com.fadlurahmanf.starter.product.dto.entity.ProductEntity;
import com.fadlurahmanf.starter.product.handler.respository.ProductRepository;
import com.fadlurahmanf.starter.transaction.helper.TransactionHelper;
import com.fadlurahmanf.starter.transaction.helper.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Service
public class ProductService {
    Logger logger = LoggerFactory.getLogger(ProductService.class);
    public static String PRODUCT_LOCK_KEY = "PRODUCT_LOCK_KEY";

    @Autowired
    LockRegistry lockRegistry;

    @Autowired
    IdentityService identityService;

    @Autowired
    ProductRepository repository;

    public void buyProduct(String email, String productId) throws CustomException {
        Lock lock = lockRegistry.obtain(PRODUCT_LOCK_KEY);
        try {
            logger.info("ATTEMPTING LOCK BY " + email);
            if (lock.tryLock(5, TimeUnit.SECONDS)) {
                Optional<ProductEntity> optProduct = repository.getProductByProductId(productId);
                if(optProduct.isEmpty()){
                    throw new CustomException(MessageConstant.PRODUCT_NOT_AVAILABLE);
                }
                ProductEntity product = optProduct.get();
                if(product.totalProduct <= 0){
                    throw new CustomException(MessageConstant.PRODUCT_SOLD_OUT);
                }
                repository.removeProductByProductId(productId);
            } else {
                logger.info("FAILED LOCK BY EMAIL " + email);
            }
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            logger.info("ERROR TRY LOCK " + e.getMessage() + " BY EMAIL " + email);
        } finally {
            lock.unlock();
            logger.info("UNLOCKED BY EMAIL " + email);
        }
    }
}
