package com.fadlurahmanf.starter.transaction.handler.controller;

import com.fadlurahmanf.starter.general.constant.MessageConstant;
import com.fadlurahmanf.starter.general.dto.exception.CustomException;
import com.fadlurahmanf.starter.general.dto.response.BaseResponse;
import com.fadlurahmanf.starter.identity.dto.entity.IdentityEntity;
import com.fadlurahmanf.starter.identity.handler.service.IdentityService;
import com.fadlurahmanf.starter.transaction.constant.TransactionURL;
import com.fadlurahmanf.starter.transaction.handler.service.TransactionService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping(path = TransactionURL.url)
public class TransactionController {
    @Autowired
    IdentityService identityService;

    @Autowired
    TransactionService service;

    Double tesBalance = 2000.0;

    @PostMapping(path = TransactionURL.fundTransfer)
    public ResponseEntity fundTransfer(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @RequestBody String body){
        try {
            JSONObject jsonObject = new JSONObject(body);
            tesBalance += 1;
            IdentityEntity identity = identityService.getIdentityFromToken(authorization);
            service.fundTransfer(identity.id, "e8a8b64f-b833-4602-a8a3-6fed67f90911", tesBalance);
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.OK.value(), MessageConstant.SUCCESS), HttpStatus.OK);
        }catch (CustomException e){
            return new ResponseEntity<>(new BaseResponse<>(e.statusCode, e.message), e.httpStatus);
        } catch (Exception e){
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
