package com.fadlurahmanf.starter.email.handler.controller;

import com.fadlurahmanf.starter.email.dto.entity.EmailVerificationEntity;
import com.fadlurahmanf.starter.email.handler.service.EmailService;
import com.fadlurahmanf.starter.email.helper.EmailHelper;
import com.fadlurahmanf.starter.general.constant.MessageConstant;
import com.fadlurahmanf.starter.general.constant.PathConstant;
import com.fadlurahmanf.starter.general.dto.response.BaseResponse;
import com.fadlurahmanf.starter.general.helper.validator.RequestBodyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;
import javax.mail.MessagingException;
import java.util.Optional;

@RestController
@RequestMapping(path = PathConstant.emailPath)
public class EmailController {
    @Autowired
    EmailService emailService;

    @PostMapping("/broadcast-email")
    public ResponseEntity broadcastEmail(@RequestBody String body){
        try {
            JSONObject jsonObject = new JSONObject(body);
            String email = RequestBodyValidator.isEmailExist(jsonObject);
            emailService.sendBroadcastEmail(email);
            return new ResponseEntity(new BaseResponse<>(HttpStatus.OK.value(), MessageConstant.SUCCESS), HttpStatus.OK);
        }catch (MessagingException e){
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/verify-email/all")
    public ResponseEntity getEmailVerify(){
        try {
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.OK.value(), MessageConstant.SUCCESS, emailService.emailVerificationRepository.findAll()), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/verify-email-registration/{token}")
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
                    throw new Exception(MessageConstant.EMAIL_NOT_VALID);
                }else if (isExpired){
                    throw new Exception(MessageConstant.EMAIL_EXPIRED);
                }
                emailService.updateIsVerifiedEmail(email.token);
                return new ResponseEntity<>(new BaseResponse<>(HttpStatus.OK.value(), MessageConstant.SUCCESS), HttpStatus.OK);
            }else{
                throw new Exception(MessageConstant.EMAIL_NOT_FOUND);
            }
        }catch (Exception e){
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
