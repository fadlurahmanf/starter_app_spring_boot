package com.fadlurahmanf.starter.history.handler.controller;

import com.fadlurahmanf.starter.general.dto.exception.CustomException;
import com.fadlurahmanf.starter.general.dto.response.BaseResponse;
import com.fadlurahmanf.starter.history.constant.HistoryURL;
import com.fadlurahmanf.starter.history.handler.service.HistoryTransferService;
import com.fadlurahmanf.starter.history.transfer.dto.entity.HistoryTransferEntity;
import com.fadlurahmanf.starter.history.transfer.dto.response.HistoryTransferResponse;
import com.fadlurahmanf.starter.identity.dto.entity.IdentityEntity;
import com.fadlurahmanf.starter.identity.handler.service.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping(path = HistoryURL.url)
public class HistoryTransferController {
    @Autowired
    IdentityService identityService;

    @Autowired
    HistoryTransferService service;

    @GetMapping(path = HistoryURL.all)
    public ResponseEntity getAllHistoryTransaction(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        try {
            IdentityEntity identity = identityService.getIdentityFromToken(authorization);
            List<HistoryTransferEntity> historyTransferEntities = service.getAllHistoryTransferEntityByUserId(identity.id);
            List<HistoryTransferResponse> historyTransferResponses = service.convertAllHistoryTransferEntityToHistoryTransferResponse(identity.id, historyTransferEntities);
            return new ResponseEntity<>(new BaseResponse<List<HistoryTransferResponse>>(historyTransferResponses), HttpStatus.OK);
        }  catch (CustomException e){
            return new ResponseEntity<>(new BaseResponse<>(e.statusCode, e.message), e.httpStatus);
        } catch (Exception e){
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
