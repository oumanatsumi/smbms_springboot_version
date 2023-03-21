package com.ouma.controller;

import com.ouma.pojo.User;
import com.ouma.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {

    @Autowired
    private UserService userService;

    @RequestMapping("hi")
    public String hello(){

        List<User> allUser = userService.findAllUser();
        for(User user : allUser){
            System.out.println(user.toString());
        }

        System.out.println("into hello controller");
        return "hello~~~~~~~~~~~~~~~";
    }

}
