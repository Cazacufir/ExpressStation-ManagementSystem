package com.parcelhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parcelhub.entity.Admin;
import com.parcelhub.mapper.AdminMapper;
import com.parcelhub.service.AdminService;
import com.parcelhub.utils.AppHttpCodeEnum;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Result register(Admin admin){
        try
        {
            LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Admin::getContact, admin.getContact());
            Admin admin2 = getOne(queryWrapper);

            if (Objects.isNull(admin2)) {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encodePassword = passwordEncoder.encode(admin.getPassword());
                admin.setPassword(encodePassword);
                admin.setRole(admin.getRole());
                admin.setHub_id(admin.getHub_id());
                save(admin);
                return Result.okResult();
            }
            return Result.errorResult(AppHttpCodeEnum.USERNAME_EXIST);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return Result.errorResult(1000, "网络异常");
    }
}
