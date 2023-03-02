package com.fadlurahmanf.starter.email.handler.controller;

import com.fadlurahmanf.starter.email.handler.service.EmailService;
import com.fadlurahmanf.starter.general.constant.MessageConstant;
import com.fadlurahmanf.starter.general.constant.PathConstant;
import com.fadlurahmanf.starter.general.dto.response.BaseResponse;
import com.fadlurahmanf.starter.general.helper.RequestBodyHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.json.JSONObject;
import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(path = PathConstant.emailPath)
public class EmailController {
    @Autowired
    EmailService emailService;

    @PostMapping("/broadcast-email")
    public ResponseEntity broadcastEmail(@RequestBody String body){
        try {
            System.out.println("masuk woiii ");
            JSONObject jsonObject = new JSONObject(body);
            String email = RequestBodyHelper.isEmailExist(jsonObject);
            emailService.sendBroadcastEmail(email);
            return new ResponseEntity(new BaseResponse<>(HttpStatus.OK.value(), MessageConstant.SUCCESS), HttpStatus.OK);
        }catch (MessagingException e){
            System.out.println("masuk mes " + e.getMessage());
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            System.out.println("masuk else " + e.getMessage());
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
