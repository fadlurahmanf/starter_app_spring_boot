package com.fadlurahmanf.starter.email.handler.service;

import com.fadlurahmanf.starter.email.constant.EmailConstant;
import com.fadlurahmanf.starter.email.dto.entity.EmailVerificationEntity;
import com.fadlurahmanf.starter.email.handler.repository.EmailVerificationRepository;
import com.fadlurahmanf.starter.general.constant.DateFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailService {
    Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    public EmailVerificationRepository emailVerificationRepository;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    SpringTemplateEngine springTemplateEngine;

    public void sendBroadcastEmail(String email) throws MessagingException {
        Map<String, Object> map = new HashMap<>();
        map.put("full_name", "fullName");
        map.put("name", "Taufik");
        map.put("button_link", "buttonLink");
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        mimeMessage.setSubject("TES");
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

    public void updateIsVerifiedEmail(String token){
        emailVerificationRepository.updateIsVerifiedEmailVerification(token);
    }

    public Optional<EmailVerificationEntity> findByToken(String token){
        return emailVerificationRepository.findByToken(token);
    }

    public void insertNewRegistrationEmail(String email){
        LocalDateTime now = LocalDateTime.now();
        String type = EmailConstant.EMAIL_TYPE_REGISTRATION;
        DateTimeFormatter dtf = DateFormatter.dtf1;
        emailVerificationRepository.save(new EmailVerificationEntity(
                email,
                type,
                now.plusMinutes(5).format(dtf)
        ));
    }
}
