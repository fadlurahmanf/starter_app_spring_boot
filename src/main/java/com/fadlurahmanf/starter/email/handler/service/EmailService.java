package com.fadlurahmanf.starter.email.handler.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {
    Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    SpringTemplateEngine springTemplateEngine;

    public void sendBroadcastEmail(String email) throws MessagingException {
        Map<String, Object> map = new HashMap<>();
        map.put("full_name", "fullName");
        map.put("button_link", "buttonLink");
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        Context context = new Context();
        context.setVariables(map);
        helper.setTo(email);
        String html = springTemplateEngine.process("registration-confirmation.html", context);
        helper.setFrom("KerjaMudah");
        helper.setText(html, true);
        javaMailSender.send(mimeMessage);
        logger.info("sent email success");
        logger.info("to: " + email);
        logger.info("buttonlink: " + "buttonLink");
    }
}
