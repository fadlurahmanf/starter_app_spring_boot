package com.fadlurahmanf.starter.swagger.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URI;

@Controller
public class SwaggerController {

    @GetMapping(value = "/swagger-ui/www.linkedin.com/in/tffajari")
    public ResponseEntity redirectProfileLinkedIn(){
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("www.linkedin.com/in/tffajari"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }
}
