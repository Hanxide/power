package com.user.power.system.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by youxiang on 2016/12/13.
 *
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    @RequestMapping("/initAaa")
    public String initAaa(){
        System.out.println("initAaa");
        System.out.println("initAaa");
        System.out.println("initAaa");
        return "aaa";
    }
}
