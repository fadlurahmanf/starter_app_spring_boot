package com.fadlurahmanf.starter.debug.controller;

import com.fadlurahmanf.starter.amqp.service.RabbitMQProducerService;
import com.fadlurahmanf.starter.debug.service.DebugService;
import com.fadlurahmanf.starter.fcm.handler.service.FCMService;
import com.fadlurahmanf.starter.general.dto.exception.CustomException;
import com.fadlurahmanf.starter.identity.dto.entity.IdentityEntity;
import com.fadlurahmanf.starter.identity.handler.service.IdentityService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class DebugController {
    Logger logger = LoggerFactory.getLogger(DebugController.class);

    @Autowired
    DebugService debugService;

    @Autowired
    IdentityService identityService;

    @Autowired
    RabbitMQProducerService rabbitMQProducerService;

    @Autowired
    FCMService fcmService;

    @GetMapping("debug/hello-world")
    public String helloWorld(){
        return "HelloWorld!";
    }

    @GetMapping("debug/tes-scheduler-cron")
    public String tesScheduler() {
        debugService.schedulerEvery10Second();
        return "SUCCESS";
    }

    @PostMapping("debug/add-balance")
    public String updateBalance(@RequestBody String body){
        JSONObject jsonObject = new JSONObject(body);
        String email = jsonObject.getString("email");
        Double balance = jsonObject.getDouble("balance");
        identityService.updateBalanceByEmail(email, balance);
        return "SUCCESS";
    }

    @PostMapping("debug/tes-rabbitmq")
    public String tesRabbitMq(@RequestBody String body){
        JSONObject jsonObject = new JSONObject(body);
        String message = jsonObject.getString("message");
        rabbitMQProducerService.sendMessage(message);
        return "MESSAGE `" + message + "` SUCCESSFULLY SENT TO RABBITMQ SERVER";
    }

    @PostMapping("debug/tes-fcm")
    public String testFcm(@RequestBody String body){
        JSONObject jsonObject = new JSONObject(body);
        String token = jsonObject.getString("token");
        String result;
        if(jsonObject.optJSONObject("data") != null){
            Map<String, String> map = new HashMap<String, String>();
            map.put("tes", "isi tes");
            map.put("tes2", "isi tes2");
            result = fcmService.sendMessage(token, map);
        }else{
            result = fcmService.sendMessage(token, "title ngasal", "body ngasal");
        }
        return result;
    }

    @PostMapping("debug/tes-put-cache-fcm")
    public String testPutCacheRedis(@RequestBody String body){
        JSONObject jsonObject = new JSONObject(body);
        String userId = jsonObject.getString("userId");
        String token = jsonObject.getString("fcmToken");
        identityService.updateFCMTokenWithCache(userId, token);
        return "SUCCESS";
    }

    @PostMapping("debug/tes-get-cache-fcm")
    public String testGetCacheRedis(@RequestBody String body){
        JSONObject jsonObject = new JSONObject(body);
        String userId = jsonObject.getString("userId");
        logger.info("MASUK SEBELUM GET FCM TOKEN");
        return identityService.getFCMToken(userId);
    }

    @PostMapping("debug/tes-redis-lock")
    public String testRedisLock(@RequestBody String body){
        try {
            JSONObject jsonObject = new JSONObject(body);
            String fromUserId = jsonObject.getString("fromUserId");
            String toUserId = jsonObject.getString("toUserId");
            Double balance = jsonObject.getDouble("balance");
            identityService.reduceBalanceByUserId(fromUserId, toUserId, balance);
            return "SUCCESS";
        }catch (CustomException e){
            return e.message;
        }
    }
}
