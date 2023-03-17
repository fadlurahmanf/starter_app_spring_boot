package com.fadlurahmanf.starter.fcm.handler.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.Executor;

@Service
public class FCMService {
    Logger logger = LoggerFactory.getLogger(FCMService.class);

    private FirebaseMessaging firebaseMessaging;

    public FCMService(FirebaseMessaging firebaseMessaging){
        this.firebaseMessaging = firebaseMessaging;
    }

    private Notification notification(){
        return Notification.builder().build();
    }

    private Notification notification(String title, String body){
        return Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();
    }

    private Message.Builder messageBuilder(String token){
        return Message.builder()
                .setToken(token);
    }

    public String sendMessage(String token, String title, String body){
        try {
            Notification notification = notification(title, body);
            Message.Builder message = messageBuilder(token).setNotification(notification);
            String result = firebaseMessaging.send(message.build());
            firebaseMessaging.send(message.build());
            logger.info("========= RESULT NOTIFICATION =========");
            logger.info("TITLE: " + title);
            logger.info("BODY: " + body);
            return result;
        }catch (Exception e){
            logger.error(e.getMessage());
            return e.getMessage();
        }
    }

    public String sendMessage(String token, Map<String, String> data){
        try {
            Notification notification = notification();
            Message.Builder message = messageBuilder(token).setNotification(notification);
            message.putAllData(data);
            String result = firebaseMessaging.send(message.build());
            logger.info("========= RESULT NOTIFICATION =========");
            logger.info("DATA: " + data);
            return result;
        }catch (Exception e){
            logger.error(e.getMessage());
            return e.getMessage();
        }
    }
}
