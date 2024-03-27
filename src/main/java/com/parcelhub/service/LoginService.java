package com.parcelhub.service;

import com.parcelhub.entity.Admin;
import com.parcelhub.entity.User;
import com.parcelhub.utils.Result;

public interface LoginService {
    Result login(Admin admin);

    Result userLogin(User user);
}
