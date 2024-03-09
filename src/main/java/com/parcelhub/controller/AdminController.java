package com.parcelhub.controller;

import com.parcelhub.entity.Admin;
import com.parcelhub.service.AdminService;
import com.parcelhub.service.LoginService;
import com.parcelhub.utils.AppHttpCodeEnum;
import com.parcelhub.utils.Result;
import com.parcelhub.utils.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AdminController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private AdminService adminService;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @PostMapping("/login")
    public Result login(@RequestBody Admin admin){
        if (!StringUtils.hasText(admin.getContact())) {
            //提示错误
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return  loginService.login(admin);
    }

//    @PostMapping("/register")
//    public Result register(@RequestBody Admin admin){
//        return adminService.register(admin);
//    }

    @PostMapping("/register")
    public Result register(@RequestBody Map<String,Object> map){
        return adminService.register(map);
    }
}
