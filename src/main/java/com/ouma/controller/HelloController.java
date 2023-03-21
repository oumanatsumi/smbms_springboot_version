package com.ouma.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("hi")
    public String hello(){
        System.out.println("into hello controller");
        return "hello~~~~~~~~~~~~~~~";
    }
}
