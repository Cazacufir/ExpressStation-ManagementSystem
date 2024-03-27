package com.parcelhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parcelhub.config.MinioConfig;
import com.parcelhub.entity.User;
import com.parcelhub.mapper.UserMapper;
import com.parcelhub.service.UserService;
import com.parcelhub.utils.AppHttpCodeEnum;
import com.parcelhub.utils.MinioUtil;
import com.parcelhub.utils.Result;
import com.parcelhub.vo.UserAvatarVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    MinioUtil minioUtil;

    @Autowired
    MinioConfig prop;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Result register(User user){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getContact,user.getContact());
        List<User> users = userMapper.selectList(lambdaQueryWrapper);
        if(users.size() > 1){
            return Result.errorResult(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        save(user);
        return Result.okResult();
    }

    @Override
    public Result updatePassword(Map<String,Object> map){
        String rawPassword = (String) map.get("rawPassword");
        String newPassword = (String) map.get("newPassword");

        int userId = (int) map.get("userId");

        if(rawPassword.equals(newPassword)){
            return Result.okResult(AppHttpCodeEnum.PASSWORD_EXITST);
        }

        User user = userMapper.selectById(userId);
        String encodeNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodeNewPassword);
        userMapper.updateById(user);
        return Result.okResult();
    }

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

    @Override
    public Result uploadAvatar(UserAvatarVo userAvatarVo){
        String objectName = minioUtil.upload(userAvatarVo.getFile());
        if (null != objectName) {
            String avatar = prop.getEndpoint() + "/" + prop.getBucketName() + "/" + objectName;
            User user = userMapper.selectById(userAvatarVo.getUserId());
            user.setAvatar(avatar);
            userMapper.updateById(user);
            System.out.println(user);
            return Result.okResult(prop.getEndpoint() + "/" + prop.getBucketName() + "/" + objectName);
        }
        return Result.errorResult(AppHttpCodeEnum.UPLOAD_ERROR);
    }
}
