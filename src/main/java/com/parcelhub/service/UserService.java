package com.parcelhub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.parcelhub.entity.User;
import com.parcelhub.utils.Result;
import com.parcelhub.vo.UserAvatarVo;

import java.util.Map;

public interface UserService extends IService<User> {

    Result register(User user);

    Result updatePassword(Map<String,Object> map);
    Result getUserInfo(int userId);

    Result updateUserInfo(User user);

    Result uploadAvatar(UserAvatarVo userAvatarVo);
}
