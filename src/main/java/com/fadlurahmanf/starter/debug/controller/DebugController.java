package com.fadlurahmanf.starter.debug.controller;

import com.fadlurahmanf.starter.debug.service.DebugService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class DebugController {
    Logger logger = LoggerFactory.getLogger(DebugController.class);

    @Autowired
    DebugService debugService;

    @GetMapping("debug/hello-world")
    public String helloWorld(){
        return "HelloWorld!";
    }

    @GetMapping("debug/tes-scheduler-cron")
    public String tesScheduler() {
        debugService.schedulerEvery10Second();
        return "SUCCESS";
    }
}
