package com.parcelhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parcelhub.entity.Admin;
import com.parcelhub.entity.Hub;
import com.parcelhub.entity.Staff;
import com.parcelhub.mapper.AdminMapper;
import com.parcelhub.mapper.HubMapper;
import com.parcelhub.mapper.StaffMapper;
import com.parcelhub.service.AdminService;
import com.parcelhub.utils.AppHttpCodeEnum;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private HubMapper hubMapper;

    @Autowired
    private StaffMapper staffMapper;

//    @Override
//    public Result register(Admin admin){
//        try
//        {
//            LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(Admin::getContact, admin.getContact());
//            Admin admin2 = getOne(queryWrapper);
//
//            if (Objects.isNull(admin2)) {
//                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//                String encodePassword = passwordEncoder.encode(admin.getPassword());
//                admin.setPassword(encodePassword);
//                admin.setRole(admin.getRole());
//                admin.setHub_id(admin.getHub_id());
//                save(admin);
//                return Result.okResult();
//            }
//            return Result.errorResult(AppHttpCodeEnum.USERNAME_EXIST);
//        }
//        catch (Exception e)
//        {
//            System.out.println(e);
//        }
//        return Result.errorResult(1000, "网络异常");
//    }

    @Override
    public Result register(Map<String,Object> map){
        Admin admin = new Admin();
        Hub hub = new Hub();
        Staff staff = new Staff();
        String contact = (String) map.get("contact");
        try{
            LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Admin::getContact,contact);
            Admin admin2 = getOne(queryWrapper);

            if(Objects.isNull(admin2)){
                int role = (int) map.get("role");
                String password = (String) map.get("password");

                if(role == 1){
                    String name = (String) map.get("name");
                    String address = (String) map.get("address");
                    String hubContact = (String) map.get("hubContact");
                    String close = (String) map.get("close");
                    String open = (String) map.get("open");
                    hub.setName(name);
                    hub.setAddress(address);
                    hub.setContact(hubContact);
                    hub.setClose_time(close);
                    hub.setOpen_time(open);
                    hubMapper.insert(hub);
                    LambdaQueryWrapper<Hub> hubLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    hubLambdaQueryWrapper.eq(Hub::getName,name);
                    int hubId = hubMapper.selectOne(hubLambdaQueryWrapper).getId();

                    admin.setHub_id(hubId);
                    staff.setHub_id(hubId);
                }

                else{
                    int hub_id = (int) map.get("hub_id");
                    admin.setHub_id(hub_id);
                    staff.setHub_id(hub_id);
                }

                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encodePassword = passwordEncoder.encode(password);
                admin.setContact(contact);
                admin.setPassword(encodePassword);
                admin.setRole(role);
                save(admin);

                String staffName = (String) map.get("staffName");
                int age = (int) map.get("age");
                String sex = (String) map.get("sex");
                String staffContact = (String) map.get("staffContact");

                staff.setName(staffName);
                staff.setAge(age);
                staff.setSex(sex);
                staff.setContact(staffContact);
                staffMapper.insert(staff);

                return Result.okResult();
            }
            return Result.errorResult(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return Result.errorResult(1000, "网络异常");
    }
}
