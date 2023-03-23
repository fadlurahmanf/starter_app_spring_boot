package com.fadlurahmanf.starter.history.handler.service;

import com.fadlurahmanf.starter.general.constant.DateFormatter;
import com.fadlurahmanf.starter.general.dto.exception.CustomException;
import com.fadlurahmanf.starter.history.handler.repository.HistoryTransferRepository;
import com.fadlurahmanf.starter.history.transfer.dto.entity.HistoryTransferEntity;
import com.fadlurahmanf.starter.history.transfer.dto.response.HistoryTransferResponse;
import com.fadlurahmanf.starter.identity.dto.entity.IdentityEntity;
import com.fadlurahmanf.starter.identity.handler.service.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Service
public class HistoryTransferService {
    @Autowired
    IdentityService identityService;
    @Autowired
    HistoryTransferRepository repository;

    public List<HistoryTransferEntity> getAllHistoryTransferEntityByUserId(String userId){
        return repository.getAllByUserId(userId);
    }

    public List<HistoryTransferEntity> getAllHistoryTransferEntityToday(){
        LocalDate today = LocalDate.now();
        LocalDateTime morning = LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 0, 0, 0);
        LocalDateTime midnight = LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 23, 59, 59);
        return repository.getAllHistoryEntityToday(morning.format(DateFormatter.dtf3), midnight.format(DateFormatter.dtf3));
    }

    public Long getTotalHistoryTransferToday(){
        LocalDate today = LocalDate.now();
        LocalDateTime morning = LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 0, 0, 0);
        LocalDateTime midnight = LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 23, 59, 59);
        return repository.getCountAllHistoryEntityToday(morning.format(DateFormatter.dtf3), midnight.format(DateFormatter.dtf3));
    }

    private String getTypeHistoryTransferResponse(HistoryTransferEntity p0, String currentUserId){
        if (Objects.equals(p0.fromUserId, currentUserId)){
            return "OUTCOME_TRANSFER";
        }else if (Objects.equals(p0.toUserId, currentUserId)){
            return "INCOME_TRANSFER";
        }else{
            return "";
        }
    }

    public List<HistoryTransferResponse> convertAllHistoryTransferEntityToHistoryTransferResponse(String currentUserId, List<HistoryTransferEntity> entities){
        return entities.stream().map(new Function<HistoryTransferEntity, HistoryTransferResponse>() {
            @Override
            public HistoryTransferResponse apply(HistoryTransferEntity p0) {
                String oppositeUserId;
                if(Objects.equals(p0.fromUserId, currentUserId)){
                    oppositeUserId = p0.toUserId;
                }else{
                    oppositeUserId = p0.fromUserId;
                }
                IdentityEntity identity = identityService.getOptIdentityByUserId(oppositeUserId).orElse(null);
                HistoryTransferResponse.User newUser = null;
                if(identity != null){
                    newUser = new HistoryTransferResponse.User(identity.id, identity.email);
                }
                return new HistoryTransferResponse(
                        p0.id, getTypeHistoryTransferResponse(p0, currentUserId), p0.balance, newUser, p0.createdAt.toString()
                );
            }
        }).toList();
    }

    public void save(String transactionId, String fromUserId, String toUserId, Double balance){
        repository.save(new HistoryTransferEntity(transactionId, fromUserId, toUserId, balance));
    }
}
