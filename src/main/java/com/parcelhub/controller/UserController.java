package com.parcelhub.controller;

import com.parcelhub.entity.User;
import com.parcelhub.service.LoginService;
import com.parcelhub.service.UserService;
import com.parcelhub.utils.Result;
import com.parcelhub.vo.UserAvatarVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    LoginService loginService;

    @PostMapping("/login")
    public Result userLogin(@RequestBody User user){
        return loginService.userLogin(user);
    }

    @PostMapping("/register")
    public Result userRegister(@RequestBody User user){
        return userService.register(user);
    }

    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody Map<String,Object> map){
        return userService.updatePassword(map);
    }

    @GetMapping("/getUserInfo")
    public Result getUserInfo(@RequestParam int userId){
        return userService.getUserInfo(userId);
    }

    @PostMapping("/updateUserInfo")
    public Result updateUserInfo(@RequestBody User user){
        return userService.updateUserInfo(user);
    }

    @PostMapping("/uploadAvatar")
    public Result uploadAvatar(UserAvatarVo userAvatarVo){
        return userService.uploadAvatar(userAvatarVo);
    }
}
