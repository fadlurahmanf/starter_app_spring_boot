package com.fadlurahmanf.starter.debug.service;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class DebugService {

    @Scheduled(cron = "*/10 * * * * *")
    public void schedulerEvery10Second(){
        System.out.println("tes tes scheduled");
    }
}
