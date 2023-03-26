package com.fadlurahmanf.starter.pin_verification.handler.controller;

import com.fadlurahmanf.starter.general.constant.MessageConstant;
import com.fadlurahmanf.starter.general.dto.exception.CustomException;
import com.fadlurahmanf.starter.general.dto.response.BaseResponse;
import com.fadlurahmanf.starter.general.helper.validator.RequestBodyValidator;
import com.fadlurahmanf.starter.identity.handler.service.IdentityService;
import com.fadlurahmanf.starter.pin_verification.constant.PinVerificationURL;
import com.fadlurahmanf.starter.pin_verification.dto.request.PinVerificationRequest;
import com.fadlurahmanf.starter.pin_verification.handler.service.PinVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = PinVerificationURL.basePrefix)
public class PinVerificationController {
    @Autowired
    PinVerificationService service;

    @Autowired
    IdentityService identityService;

    @PostMapping(path = PinVerificationURL.verifyPin)
    public ResponseEntity<BaseResponse<String>> verifyPin(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken, @RequestBody PinVerificationRequest body){
        try {
            String pin = RequestBodyValidator.validatePinVerificationRequest(body.pin);
            identityService.authenticatePin(authorizationToken, pin);
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.OK.value(), MessageConstant.SUCCESS, service.generatePinToken(authorizationToken)), HttpStatus.OK);
        }catch (CustomException e){
            return new ResponseEntity<>(new BaseResponse<>(e.statusCode, e.message), e.httpStatus);
        }catch (Exception e){
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
