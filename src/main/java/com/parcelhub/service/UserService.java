package com.parcelhub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.parcelhub.entity.User;
import com.parcelhub.utils.Result;
import org.springframework.web.multipart.MultipartFile;

public interface UserService extends IService<User> {
    Result getUserInfo(int userId);

    Result updateUserInfo(User user);

//    Result uploadAvatar(MultipartFile file);
}
