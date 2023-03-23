package com.fadlurahmanf.starter.history.handler.repository;


import com.fadlurahmanf.starter.general.constant.EntityConstant;
import com.fadlurahmanf.starter.history.transfer.dto.entity.HistoryTransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface HistoryTransferRepository extends JpaRepository<HistoryTransferEntity, Long> {
    @Query(value = "SELECT * FROM `" + EntityConstant.HistoryTransfer.entity + "` WHERE " + EntityConstant.HistoryTransfer.toUserId + " = :userId OR " + EntityConstant.HistoryTransfer.fromUserId + " = :userId", nativeQuery = true, countQuery = "SELECT 1")
    List<HistoryTransferEntity> getAllByUserId(@Param("userId") String userId);

    @Query(value = "SELECT * FROM `" + EntityConstant.HistoryTransfer.entity + "` WHERE DATE(" + EntityConstant.HistoryTransfer.createdAt + ") BETWEEN DATE(:morning) AND DATE(:midnight)", nativeQuery = true, countQuery = "SELECT 1")
    List<HistoryTransferEntity> getAllHistoryEntityToday(@Param("morning") String morning, @Param("midnight") String midnight);

    @Query(value = "SELECT COUNT(*) FROM `" + EntityConstant.HistoryTransfer.entity + "` WHERE DATE(" + EntityConstant.HistoryTransfer.createdAt + ") BETWEEN DATE(:morning) AND DATE(:midnight)", nativeQuery = true, countQuery = "SELECT 1")
    Long getCountAllHistoryEntityToday(@Param("morning") String morning, @Param("midnight") String midnight);
}
