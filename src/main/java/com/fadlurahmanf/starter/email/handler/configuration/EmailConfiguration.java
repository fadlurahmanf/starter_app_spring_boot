package com.fadlurahmanf.starter.email.handler.configuration;

import com.fadlurahmanf.starter.general.constant.PropertyConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfiguration {

    @Value(PropertyConstant.mailUsername)
    private String username;

    @Value(PropertyConstant.mailPassword)
    private String password;
    @Value(PropertyConstant.mailHost)
    private String host;
    @Value(PropertyConstant.mailPort)
    private int port;

    @Bean
    public JavaMailSenderImpl mailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.starttls.required", "true");
        return mailSender;
    }
}
