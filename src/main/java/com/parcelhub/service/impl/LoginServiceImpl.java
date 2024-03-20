package com.parcelhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.parcelhub.entity.Admin;
import com.parcelhub.mapper.AdminMapper;
import com.parcelhub.model.SecurityUser;
import com.parcelhub.service.LoginService;
import com.parcelhub.utils.*;
import com.parcelhub.vo.AdminInfoVo;
import com.parcelhub.vo.AdminLoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private AuthenticationManager authenticationManager;

    public Result login(Admin admin){
//        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
//        adminQueryWrapper.eq("contact",admin.getContact())
//                .eq("password",admin.getPassword());
//        Admin user = adminMapper.selectOne(adminQueryWrapper);
//        if(user == null){
//            return Result.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(),AppHttpCodeEnum.LOGIN_ERROR.getMsg());
//        }
//        return Result.okResult(user);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(admin.getContact(),admin.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        System.out.println("authenticate" + authenticate);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }

        SecurityUser securityUser = (SecurityUser) authenticate.getPrincipal();
        String adminId =String.valueOf(securityUser.getAdmin().getAdminId()) ;
        String jwt = JwtUtil.createJWT(adminId);

        redisCache.setCacheObject("login:"+adminId,securityUser);

        AdminInfoVo adminInfoVo = BeanCopyUtils.copyBean(securityUser.getAdmin(), AdminInfoVo.class);
        AdminLoginVo vo = new AdminLoginVo(jwt,adminInfoVo);
        return Result.okResult(vo);
    }
}
