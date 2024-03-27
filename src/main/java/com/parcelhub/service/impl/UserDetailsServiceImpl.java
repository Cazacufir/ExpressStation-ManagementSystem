package com.parcelhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.parcelhub.entity.Admin;
import com.parcelhub.entity.User;
import com.parcelhub.mapper.AdminMapper;
import com.parcelhub.mapper.UserMapper;
import com.parcelhub.model.SecurityForeStageUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.parcelhub.model.SecurityUser;

import java.util.List;
import java.util.Objects;

@Service
public class  UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String[] userStr = username.split("@");
        String name = userStr[1];
        if(userStr[0].equals("ad")){
            //根据用户名查询用户信息
            LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Admin::getContact,name);
            Admin admin = adminMapper.selectOne(wrapper);
            //如果查询不到数据就通过抛出异常来给出提示
            if(Objects.isNull(admin)){
                throw new RuntimeException("用户名或密码错误");
            }

            int adminId = admin.getAdminId();
            List<String> permissionKeyList = adminMapper.getUserType(adminId);

            //封装成UserDetails对象返回
            return new SecurityUser(admin, permissionKeyList);
        }
        else {
            //根据用户名查询用户信息
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getContact,name);
            User user = userMapper.selectOne(wrapper);
            //如果查询不到数据就通过抛出异常来给出提示
            if(Objects.isNull(user)){
                throw new RuntimeException("用户名或密码错误");
            }

            int userId = user.getUserId();
            List<String> permissionKeyList =userMapper.getUserContact(userId);

            //封装成UserDetails对象返回
            return new SecurityForeStageUser(user, permissionKeyList);
        }
    }
}