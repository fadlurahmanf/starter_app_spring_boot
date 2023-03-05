package com.fadlurahmanf.starter.debug.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class DebugController {

    @GetMapping("debug/hello-world")
    public String helloWorld(){
        return "HelloWorld!";
    }
}
