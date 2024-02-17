package com.parcelhub.service.impl;

import com.parcelhub.entity.Admin;
import com.parcelhub.service.LoginService;
import com.parcelhub.utils.Result;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    public Result login(Admin admin){
        return Result.okResult();
    }
}
