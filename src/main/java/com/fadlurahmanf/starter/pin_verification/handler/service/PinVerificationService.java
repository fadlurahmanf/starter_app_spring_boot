package com.fadlurahmanf.starter.pin_verification.handler.service;

import com.fadlurahmanf.starter.general.constant.MessageConstant;
import com.fadlurahmanf.starter.general.dto.exception.CustomException;
import com.fadlurahmanf.starter.identity.dto.entity.IdentityEntity;
import com.fadlurahmanf.starter.identity.handler.service.IdentityService;
import com.fadlurahmanf.starter.jwt.handler.JWTTokenUtil;
import com.fadlurahmanf.starter.jwt.handler.JWTUserDetailService;
import com.fadlurahmanf.starter.pin_verification.dto.entity.PinVerificationEntity;
import com.fadlurahmanf.starter.pin_verification.handler.repository.PinVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PinVerificationService {
    @Autowired
    JWTTokenUtil jwtTokenUtil;

    @Autowired
    JWTUserDetailService jwtUserDetailService;

    @Autowired
    IdentityService identityService;

    @Autowired
    PinVerificationRepository repository;

    public String generatePinToken(String authorizationToken) throws CustomException {
        IdentityEntity identity = identityService.getActiveIdentityFromToken(authorizationToken);
        UserDetails userDetails = jwtUserDetailService.loadUserByUsername(identity.email);
        String pinToken = jwtTokenUtil.generatePINToken(userDetails);
        repository.replacePinTokenByUserId(identity.id, pinToken, false);
        return pinToken;
    }

    public void validatePinToken(String authorizationToken, String pinToken) throws CustomException {
        IdentityEntity identity = identityService.getActiveIdentityFromToken(authorizationToken);
        Optional<PinVerificationEntity> optPinVerification = repository.getByPinToken(identity.id, pinToken);
        if(optPinVerification.isEmpty()) throw new CustomException(MessageConstant.TOKEN_NOT_VALID, HttpStatus.UNAUTHORIZED);
        PinVerificationEntity pinVerification = optPinVerification.get();
        if(jwtTokenUtil.validatePinToken(pinToken)){
            if(!pinVerification.isUsed){
                setAlreadyUsedTokenByPinToken(identity.id, pinToken);
            }else{
                throw new CustomException(MessageConstant.PIN_EXPIRED);
            }
        }else{
            setAlreadyUsedTokenByPinToken(identity.id, pinToken);
            throw new CustomException(MessageConstant.TOKEN_NOT_VALID, HttpStatus.UNAUTHORIZED);
        }
    }

    public void setAlreadyUsedTokenByPinToken(String userId, String pinToken){
        Optional<PinVerificationEntity> pinVerification = repository.getByPinToken(userId, pinToken);
        if(pinVerification.isPresent()){
            repository.setUsedPinByPinToken(pinToken, true);
        }
    }
}
