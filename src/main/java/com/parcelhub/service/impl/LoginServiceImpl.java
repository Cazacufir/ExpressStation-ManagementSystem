package com.parcelhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.parcelhub.entity.Admin;
import com.parcelhub.mapper.AdminMapper;
import com.parcelhub.service.LoginService;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AdminMapper adminMapper;

    public Result login(Admin admin){
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.eq("contact",admin.getContact())
                .eq("password",admin.getPassword());
        List<Admin> user = adminMapper.selectList(adminQueryWrapper);
        if(user.isEmpty()){
            return Result.errorResult(401,"账号或者密码错误!");
        }
        return Result.okResult();
    }
}
