package com.fadlurahmanf.starter.email.handler.controller;

import com.fadlurahmanf.starter.email.constant.EmailURL;
import com.fadlurahmanf.starter.email.dto.entity.EmailVerificationEntity;
import com.fadlurahmanf.starter.email.handler.service.EmailService;
import com.fadlurahmanf.starter.email.helper.EmailHelper;
import com.fadlurahmanf.starter.general.constant.MessageConstant;
import com.fadlurahmanf.starter.general.dto.exception.CustomException;
import com.fadlurahmanf.starter.general.dto.response.BaseResponse;
import com.fadlurahmanf.starter.general.helper.validator.RequestBodyValidator;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.Optional;

@RestController
@RequestMapping(path = EmailURL.basePrefix)
public class EmailController {
    @Autowired
    EmailService emailService;

    @PostMapping(EmailURL.pathBroadcastEmail)
    public ResponseEntity broadcastEmail(@RequestBody String body){
        try {
            JSONObject jsonObject = new JSONObject(body);
            String email = RequestBodyValidator.validateEmailRequest(jsonObject);
            emailService.sendBroadcastEmail(email);
            return new ResponseEntity(new BaseResponse<>(HttpStatus.OK.value(), MessageConstant.SUCCESS), HttpStatus.OK);
        }catch (MessagingException e){
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(EmailURL.pathListEmailVerification)
    public ResponseEntity getEmailVerify(){
        try {
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.OK.value(), MessageConstant.SUCCESS, emailService.emailVerificationRepository.findAll()), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(EmailURL.pathRequestEmailRegistration)
    public ResponseEntity requestEmail(@RequestBody String body){
        try {
            JSONObject jsonObject = new JSONObject(body);
            String email = RequestBodyValidator.validateEmailRequest(jsonObject);
//            emailService.sendBroadcastEmail(email);
            emailService.insertNewRegistrationEmail(email);
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.OK.value(), MessageConstant.SUCCESS), HttpStatus.OK);
        } catch (CustomException e){
            return new ResponseEntity<>(new BaseResponse<>(e.statusCode, e.message), e.httpStatus);
        } catch (Exception e){
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(EmailURL.pathVerifyEmailRegistration + "{token}")
    public ResponseEntity verifyEmailRegistration(
            @PathVariable("token") String token
    ){
        try {
            Optional<EmailVerificationEntity> optEmail = emailService.findByToken(token);
            if(optEmail.isPresent()){
                EmailVerificationEntity email = optEmail.get();
                Boolean isExpired = EmailHelper.isExpired(email.expiredAt);
                Boolean isVerified = email.isVerified;
                if(isVerified){
                    throw new CustomException(MessageConstant.EMAIL_NOT_VALID);
                }else if (isExpired){
                    throw new CustomException(MessageConstant.EMAIL_EXPIRED);
                }
                emailService.updateIsVerifiedEmail(email.token);
                return new ResponseEntity<>(new BaseResponse<>(HttpStatus.OK.value(), MessageConstant.SUCCESS), HttpStatus.OK);
            }else{
                throw new CustomException(MessageConstant.EMAIL_NOT_FOUND);
            }
        }catch (CustomException e){
            return new ResponseEntity<>(new BaseResponse<>(e.statusCode, e.message), e.httpStatus);
        }catch (Exception e){
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
