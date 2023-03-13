package com.fadlurahmanf.starter.identity.handler.controller;

import com.fadlurahmanf.starter.email.handler.service.EmailService;
import com.fadlurahmanf.starter.general.constant.MessageConstant;
import com.fadlurahmanf.starter.general.dto.exception.CustomException;
import com.fadlurahmanf.starter.general.dto.response.BaseResponse;
import com.fadlurahmanf.starter.general.helper.validator.RequestBodyValidator;
import com.fadlurahmanf.starter.identity.constant.IdentityStatusConstant;
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
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(path = IdentityURL.basePrefix)
class IdentityController {
    @Autowired
    IdentityService identityService;

    @Autowired
    EmailService emailService;

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
            LoginResponse loginResponse = identityService.authenticate(email, password);
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.OK.value(), MessageConstant.SUCCESS, loginResponse), HttpStatus.OK);
        }catch (CustomException e){
            return new ResponseEntity<>(new BaseResponse<>(e.statusCode, e.message), e.httpStatus);
        }catch (Exception e){
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(IdentityURL.pathRefreshToken)
    public ResponseEntity refreshToken(@RequestBody String body){
        try {
            JSONObject jsonObject = new JSONObject(body);
            String refreshToken = RequestBodyValidator.validateRefreshToken(jsonObject);
            LoginResponse newRefreshTokenResponse = identityService.authenticateRefreshToken(refreshToken);
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.OK.value(), MessageConstant.SUCCESS, newRefreshTokenResponse), HttpStatus.OK);
        }catch (CustomException e){
            return new ResponseEntity<>(new BaseResponse<>(e.statusCode, e.message), e.httpStatus);
        }catch (Exception e){
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(IdentityURL.pathRegister)
    public ResponseEntity register(@RequestBody String body){
        try {
            JSONObject jsonObject = new JSONObject(body);
            String email = RequestBodyValidator.validateEmailRequest(jsonObject);
            String password = RequestBodyValidator.validatePasswordRequest(jsonObject);
            Optional<IdentityEntity> optIdentity = identityService.findByEmail(email);
            if(optIdentity.isPresent()){
                String statusUser = optIdentity.get().status;
                if(Objects.equals(statusUser, IdentityStatusConstant.ACTIVE) || Objects.equals(statusUser, IdentityStatusConstant.BLOCKED)){
                    throw new CustomException(MessageConstant.EMAIL_ALREADY_EXIST);
                } else if (Objects.equals(statusUser, IdentityStatusConstant.NEW)) {
                    emailService.insertNewRegistrationEmail(email);
                    identityService.updateIdentity(IdentityStatusConstant.NEW, email, password);
                }
            }else{
                emailService.insertNewRegistrationEmail(email);
                identityService.saveIdentity(email, password);
            }
            return new  ResponseEntity<BaseResponse<List<IdentityEntity>>>(new BaseResponse(HttpStatus.OK.value(), MessageConstant.SUCCESS), HttpStatus.OK);
        }catch (CustomException e){
            return new ResponseEntity<>(new BaseResponse<>(e.statusCode, e.message), e.httpStatus);
        }catch (Exception e){
            return new ResponseEntity<>(new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
