package com.fadlurahmanf.starter.history.transfer.dto.entity;

import com.fadlurahmanf.starter.general.constant.EntityConstant;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity(name = EntityConstant.HistoryTransfer.entity)
public class HistoryTransferEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    public String id;
    @Column(name = EntityConstant.HistoryTransfer.transactionId)
    public String transactionId;
    @Column(name = EntityConstant.HistoryTransfer.fromUserId)
    public String fromUserId;
    @Column(name = EntityConstant.HistoryTransfer.toUserId)
    public String toUserId;
    public Double balance;
    @Column(name = EntityConstant.HistoryTransfer.createdAt)
    public Date createdAt;

    @PrePersist()
    protected void onCreate(){
        this.createdAt = new Date();
    }

    public HistoryTransferEntity() {}

    public HistoryTransferEntity(String transactionId, String fromUserId, String toUserId, Double balance) {
        this.transactionId = transactionId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.balance = balance;
    }
}
