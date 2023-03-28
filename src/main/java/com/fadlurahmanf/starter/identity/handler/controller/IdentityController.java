package com.fadlurahmanf.starter.identity.handler.controller;

import com.fadlurahmanf.starter.email.handler.service.EmailService;
import com.fadlurahmanf.starter.general.constant.MessageConstant;
import com.fadlurahmanf.starter.general.dto.exception.CustomException;
import com.fadlurahmanf.starter.general.dto.response.BaseResponse;
import com.fadlurahmanf.starter.general.helper.validator.RequestBodyValidator;
import com.fadlurahmanf.starter.identity.constant.IdentityStatusConstant;
import com.fadlurahmanf.starter.identity.constant.IdentityURL;
import com.fadlurahmanf.starter.identity.dto.entity.IdentityEntity;
import com.fadlurahmanf.starter.identity.dto.request.CreatePinRequest;
import com.fadlurahmanf.starter.identity.dto.request.LoginRequest;
import com.fadlurahmanf.starter.identity.dto.request.RegisterRequest;
import com.fadlurahmanf.starter.identity.dto.response.LoginResponse;
import com.fadlurahmanf.starter.identity.handler.service.IdentityService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity getListAccount() {
        try {
            return new ResponseEntity<BaseResponse<List<IdentityEntity>>>(new BaseResponse(HttpStatus.OK.value(), "SUCCESS", identityService.getAllIdentity()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(IdentityURL.pathLogin)
    public ResponseEntity<BaseResponse<LoginResponse>> login(@RequestBody LoginRequest body) {
        try {
            String email = RequestBodyValidator.validateEmailRequest(body.email);
            String password = RequestBodyValidator.validateCreatePasswordRequest(body.password);
            Boolean isUserExist = identityService.isUserExistByEmail(email);
            if (!isUserExist) {
                throw new CustomException(MessageConstant.USER_NOT_EXIST, HttpStatus.UNAUTHORIZED);
            }
            LoginResponse loginResponse = identityService.authenticate(email, password);
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.OK.value(), MessageConstant.SUCCESS, loginResponse), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(new BaseResponse<>(e.statusCode, e.message), e.httpStatus);
        } catch (Exception e) {
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(IdentityURL.pathRefreshToken)
    public ResponseEntity<BaseResponse<LoginResponse>> refreshToken(@RequestBody JSONObject jsonObject) {
        try {
            String refreshToken = RequestBodyValidator.validateRefreshToken(jsonObject);
            LoginResponse newRefreshTokenResponse = identityService.authenticateRefreshToken(refreshToken);
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.OK.value(), MessageConstant.SUCCESS, newRefreshTokenResponse), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(new BaseResponse<>(e.statusCode, e.message), e.httpStatus);
        } catch (Exception e) {
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(IdentityURL.pathMyAccountInfo)
    public ResponseEntity<BaseResponse<IdentityEntity>> getMyAccountInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        try {
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.OK.value(), MessageConstant.SUCCESS, identityService.getIdentityFromToken(authorization)), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(new BaseResponse<>(e.statusCode, e.message), e.httpStatus);
        } catch (Exception e) {
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(IdentityURL.pathUpdateFCMToken)
    public ResponseEntity updateFCMToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @RequestBody JSONObject jsonObject) {
        try {
            RequestBodyValidator.validateFCMToken(jsonObject);
            IdentityEntity identity = identityService.getIdentityFromToken(authorization);
            identityService.updateFCMToken(identity.id, jsonObject.getString("fcmToken"));
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.OK.value(), MessageConstant.SUCCESS), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(new BaseResponse<>(e.statusCode, e.message), e.httpStatus);
        } catch (Exception e) {
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(IdentityURL.pathRegister)
    public ResponseEntity register(@RequestBody RegisterRequest request) {
        try {
            if (request.email == null || request.password == null) {
                throw new CustomException(MessageConstant.FIELD_REQUIRED, HttpStatus.BAD_GATEWAY);
            }
            Optional<IdentityEntity> optIdentity = identityService.getOptionalIdentityByEmail(request.email);
            if (optIdentity.isPresent()) {
                String statusUser = optIdentity.get().status;
                if (Objects.equals(statusUser, IdentityStatusConstant.ACTIVE) || Objects.equals(statusUser, IdentityStatusConstant.BLOCKED)) {
                    throw new CustomException(MessageConstant.EMAIL_ALREADY_EXIST);
                } else if (Objects.equals(statusUser, IdentityStatusConstant.NEW)) {
                    emailService.insertNewRegistrationEmail(request.email);
                    identityService.updateStatusAndPasswordByEmail(IdentityStatusConstant.NEW, request.email, request.password);
                }
            } else {
                emailService.insertNewRegistrationEmail(request.email);
                identityService.saveNewIdentity(request.email, request.password);
            }
            return new ResponseEntity<BaseResponse<List<IdentityEntity>>>(new BaseResponse(HttpStatus.OK.value(), MessageConstant.SUCCESS), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(new BaseResponse<>(e.statusCode, e.message), e.httpStatus);
        } catch (Exception e) {
            return new ResponseEntity<>(new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(IdentityURL.pathCreatePin)
    public ResponseEntity createPin(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @RequestBody CreatePinRequest body) {
        try {
            identityService.saveNewPin(authorization, body.pin);
            return new ResponseEntity<BaseResponse<List<IdentityEntity>>>(new BaseResponse(HttpStatus.OK.value(), MessageConstant.SUCCESS), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(new BaseResponse<>(e.statusCode, e.message), e.httpStatus);
        } catch (Exception e) {
            return new ResponseEntity<>(new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
