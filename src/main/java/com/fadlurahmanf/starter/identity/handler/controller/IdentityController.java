package com.fadlurahmanf.starter.identity.handler.controller;

import com.fadlurahmanf.starter.general.constant.MessageConstant;
import com.fadlurahmanf.starter.general.constant.PathConstant;
import com.fadlurahmanf.starter.general.dto.exception.CustomException;
import com.fadlurahmanf.starter.general.dto.response.BaseResponse;
import com.fadlurahmanf.starter.general.helper.validator.RequestBodyValidator;
import com.fadlurahmanf.starter.identity.constant.IdentityURL;
import com.fadlurahmanf.starter.identity.dto.entity.IdentityEntity;
import com.fadlurahmanf.starter.identity.dto.response.LoginResponse;
import com.fadlurahmanf.starter.identity.handler.service.IdentityService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = IdentityURL.basePrefix)
class IdentityController {
    @Autowired
    IdentityService identityService;

    @GetMapping(IdentityURL.pathListAccount)
    public ResponseEntity getListAccount(){
        try {
            return new  ResponseEntity<BaseResponse<List<IdentityEntity>>>(new BaseResponse(HttpStatus.OK.value(), "SUCCESS", identityService.findAll()), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(IdentityURL.pathLogin)
    public ResponseEntity login(@RequestBody String body){
        try {
            JSONObject jsonObject = new JSONObject(body);
            String email = RequestBodyValidator.validateEmailRequest(jsonObject);
            String password = RequestBodyValidator.validatePasswordRequest(jsonObject);
            Boolean isUserExist = identityService.isUserExistByEmail(email);
            if(!isUserExist){
                throw new CustomException(MessageConstant.USER_NOT_EXIST);
            }
            String token = identityService.authenticate(email, password);
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.OK.value(), MessageConstant.SUCCESS, new LoginResponse(token)), HttpStatus.OK);
        }catch (CustomException e){
            return new ResponseEntity<>(new BaseResponse<>(e.statusCode, e.message), e.httpStatus);
        }catch (Exception e){
            return new ResponseEntity<>(new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(IdentityURL.pathRegister)
    public ResponseEntity register(@RequestBody String body){
        try {
            JSONObject jsonObject = new JSONObject(body);
            String email = RequestBodyValidator.validateEmailRequest(jsonObject);
            String password = RequestBodyValidator.validatePasswordRequest(jsonObject);
            Boolean isUserExist = identityService.isUserExistByEmail(email);
            if(isUserExist){
                throw new CustomException(MessageConstant.EMAIL_ALREADY_EXIST);
            }
            identityService.saveIdentity(email, password);
            return new  ResponseEntity<BaseResponse<List<IdentityEntity>>>(new BaseResponse(HttpStatus.OK.value(), MessageConstant.SUCCESS), HttpStatus.OK);
        }catch (CustomException e){
            return new ResponseEntity<>(new BaseResponse<>(e.statusCode, e.message), e.httpStatus);
        }catch (Exception e){
            return new ResponseEntity<>(new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
