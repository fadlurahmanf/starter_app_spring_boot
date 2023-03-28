package com.fadlurahmanf.starter.transaction.handler.service;

import com.fadlurahmanf.starter.general.dto.exception.CustomException;
import com.fadlurahmanf.starter.history.handler.service.HistoryTransferService;
import com.fadlurahmanf.starter.identity.dto.entity.IdentityEntity;
import com.fadlurahmanf.starter.identity.handler.service.IdentityService;
import com.fadlurahmanf.starter.transaction.helper.TransactionHelper;
import com.fadlurahmanf.starter.transaction.helper.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Service
public class TransactionService {
    Logger logger = LoggerFactory.getLogger(TransactionService.class);
    @Autowired
    HistoryTransferService historyTransferService;

    @Autowired
    IdentityService identityService;

    public Long getTodayTransactionNumber() {
        return historyTransferService.getTotalHistoryTransferToday() + 1L;
    }

    @Autowired
    LockRegistry lockRegistry;

    private static final String TRANSACTION_LOCK_KEY = "TRANSACTION-LOCK-KEY";

    public void fundTransfer(String fromUserId, String toUserId, Double balance) throws CustomException {
        Lock lock = lockRegistry.obtain(TRANSACTION_LOCK_KEY);
        IdentityEntity toIdentity = identityService.getIdentityByUserId(toUserId);
        IdentityEntity fromIdentity = identityService.getIdentityByUserId(fromUserId);
        try {
            logger.info("ATTEMPTING LOCK BY " + fromIdentity.email + " WITH BALANCE " + balance);
            if (lock.tryLock(5, TimeUnit.SECONDS)) {
                String transactionId = TransactionHelper.generateTransactionId(TransactionType.FUND_TRANSFER, getTodayTransactionNumber());
                historyTransferService.save(transactionId, fromUserId, toUserId, balance);
            } else {
                logger.info("FAILED LOCK BY EMAIL " + fromUserId);
            }
        } catch (Exception e) {
            logger.info("ERROR TRY LOCK " + e.getMessage() + " BY EMAIL " + fromIdentity.email);
        } finally {
            lock.unlock();
            logger.info("UNLOCKED BY EMAIL " + fromIdentity.email);
        }
    }

    public void testDuplicateFundTransferWithLocking(String fromUserId, String toUserId, Double balance) {
        var executor = Executors.newFixedThreadPool(2);
        Runnable lockThreadOne = () -> {
            Lock lock = lockRegistry.obtain(TRANSACTION_LOCK_KEY);
            UUID uuid = UUID.randomUUID();
            try {
                IdentityEntity toIdentity = identityService.getIdentityByUserId(toUserId);
                IdentityEntity fromIdentity = identityService.getIdentityByUserId(fromUserId);
                logger.info("ATTEMPTING LOCK BY " + uuid);
                if (lock.tryLock(10, TimeUnit.SECONDS)) {
                    logger.info("LOCKED BY " + uuid);
                    String transactionId = TransactionHelper.generateTransactionId(TransactionType.FUND_TRANSFER, getTodayTransactionNumber());
                    Thread.sleep(5000);
                    historyTransferService.save(transactionId, fromUserId, toUserId, balance);
                    logger.info("SAVED TRANSACTION ID " + transactionId + " BY " + uuid);
                } else {
                    logger.info("FAILED LOCK BY " + uuid);
                }
            } catch (Exception e) {
                logger.info("ERROR TRY LOCK " + e.getMessage() + " BY " + uuid);
            } finally {
                lock.unlock();
                logger.info("UNLOCKED BY " + uuid);
            }
        };

        Runnable lockThreadTwo = () -> {
            Lock lock = lockRegistry.obtain(TRANSACTION_LOCK_KEY);
            UUID uuid = UUID.randomUUID();
            try {
                IdentityEntity toIdentity = identityService.getIdentityByUserId(toUserId);
                IdentityEntity fromIdentity = identityService.getIdentityByUserId(fromUserId);
                logger.info("ATTEMPTING LOCK BY " + uuid);
                if (lock.tryLock(10, TimeUnit.SECONDS)) {
                    logger.info("LOCKED BY " + uuid);
                    String transactionId = TransactionHelper.generateTransactionId(TransactionType.FUND_TRANSFER, getTodayTransactionNumber());
                    logger.info("TRANSACTION ID " + transactionId + " BY " + uuid);
                    Thread.sleep(5000);
                    historyTransferService.save(transactionId, fromUserId, toUserId, balance);
                    logger.info("SAVED TRANSACTION ID " + transactionId + " BY " + uuid);
                } else {
                    logger.info("FAILED LOCK BY " + uuid);
                }
            } catch (Exception e) {
                logger.info("ERROR TRY LOCK " + e.getMessage() + " BY " + uuid);
            } finally {
                lock.unlock();
                logger.info("UNLOCKED BY " + uuid);
            }
        };
        executor.submit(lockThreadOne);
        executor.submit(lockThreadTwo);
        executor.shutdown();
    }

    public void testDuplicateFundTransferWithoutLocking(String fromUserId, String toUserId, Double balance) {
        var executor = Executors.newFixedThreadPool(2);
        Runnable lockThreadOne = () -> {
            UUID uuid = UUID.randomUUID();
            try {
                logger.info("ATTEMPTING SAVE BY " + uuid);
                String transactionId = TransactionHelper.generateTransactionId(TransactionType.FUND_TRANSFER, getTodayTransactionNumber());
                logger.info("TRANSACTION ID " + transactionId + " BY " + uuid);
                Thread.sleep(5000);
                historyTransferService.save(transactionId, fromUserId, toUserId, balance);
                logger.info("SAVED TRANSACTION ID " + transactionId + " BY " + uuid);
            } catch (Exception e) {
                logger.info("ERROR TRY SAVE " + e.getMessage() + " BY " + uuid);
            }
        };
        Runnable lockThreadTwo = () -> {
            UUID uuid = UUID.randomUUID();
            try {
                logger.info("ATTEMPTING SAVE BY " + uuid);
                String transactionId = TransactionHelper.generateTransactionId(TransactionType.FUND_TRANSFER, getTodayTransactionNumber());
                logger.info("TRANSACTION ID " + transactionId + " BY " + uuid);
                Thread.sleep(5000);
                historyTransferService.save(transactionId, fromUserId, toUserId, balance);
                logger.info("SAVED TRANSACTION ID " + transactionId + " BY " + uuid);
            } catch (Exception e) {
                logger.info("ERROR TRY SAVE " + e.getMessage() + " BY " + uuid);
            }
        };
        executor.submit(lockThreadOne);
        executor.submit(lockThreadTwo);
        executor.shutdown();
    }
}
