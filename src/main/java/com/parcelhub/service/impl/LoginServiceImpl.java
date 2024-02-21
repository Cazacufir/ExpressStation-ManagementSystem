package com.parcelhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.parcelhub.entity.Admin;
import com.parcelhub.mapper.AdminMapper;
import com.parcelhub.service.LoginService;
import com.parcelhub.utils.AppHttpCodeEnum;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AdminMapper adminMapper;

    public Result login(Admin admin){
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.eq("contact",admin.getContact())
                .eq("password",admin.getPassword());
        Admin user = adminMapper.selectOne(adminQueryWrapper);
        if(user == null){
            return Result.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(),AppHttpCodeEnum.LOGIN_ERROR.getMsg());
        }
        return Result.okResult(user);
    }
}
