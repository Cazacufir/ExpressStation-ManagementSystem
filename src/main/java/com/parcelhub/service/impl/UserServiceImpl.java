package com.parcelhub.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parcelhub.entity.User;
import com.parcelhub.mapper.UserMapper;
import com.parcelhub.service.UserService;
import com.parcelhub.utils.AppHttpCodeEnum;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public Result getUserInfo(int userId){
        User user = userMapper.selectById(userId);
        if(Objects.isNull(user)){
            return Result.errorResult(AppHttpCodeEnum.USER_NOT_FOUND);
        }
        return Result.okResult(user);
    }

    @Override
    public Result updateUserInfo(User user){
        userMapper.updateById(user);
        return Result.okResult();
    }
}
