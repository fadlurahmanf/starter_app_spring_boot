package com.fadlurahmanf.starter.debug.controller;

import com.fadlurahmanf.starter.debug.service.DebugService;
import com.fadlurahmanf.starter.identity.dto.entity.IdentityEntity;
import com.fadlurahmanf.starter.identity.handler.service.IdentityService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class DebugController {
    Logger logger = LoggerFactory.getLogger(DebugController.class);

    @Autowired
    DebugService debugService;

    @Autowired
    IdentityService identityService;

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
        identityService.updateBalance(email, balance);
        return "SUCCESS";
    }

    @GetMapping("debug/tes-redis-lock")
    public String tesLocking(){
        String email = "tffajari@gmail.com";
        IdentityEntity identity = identityService.findByEmail(email).get();
        identity.balance = identity.balance - 11000;
        identityService.updateBalance(email, identity.balance);
        String email2 = "tffajari@gmail.com";
        IdentityEntity identity2 = identityService.findByEmail(email2).get();
        identity2.balance = identity2.balance - 40000;
        identityService.updateBalance(email2, identity2.balance);
        return "SUCCESS";
    }
}
