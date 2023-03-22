package com.ouma.controller;

import com.ouma.pojo.User;
import com.ouma.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class HelloController {

    @Autowired
    private UserService userService;

    @RequestMapping("hi")
    public String hello(){
        List<User> allUser = userService.findAllUser();
        for(User user :allUser){
            System.out.println(user.toString());
        }
        return "index";
    }
    @RequestMapping("/hi2")
    public ModelAndView  hello2(){
        return new ModelAndView("index");
    }

    @RequestMapping("/hi3")
    public ModelAndView hello3(){
        return new ModelAndView("jsp/index2");
    }

}
