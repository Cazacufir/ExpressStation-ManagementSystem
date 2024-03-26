package com.parcelhub.controller;

import com.parcelhub.entity.User;
import com.parcelhub.service.UserService;
import com.parcelhub.utils.Result;
import com.parcelhub.vo.UserAvatarVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

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
