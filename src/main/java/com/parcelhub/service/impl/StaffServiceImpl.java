package com.parcelhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parcelhub.entity.Hub;
import com.parcelhub.entity.Staff;
import com.parcelhub.mapper.HubMapper;
import com.parcelhub.mapper.StaffMapper;
import com.parcelhub.service.StaffService;
import com.parcelhub.utils.AppHttpCodeEnum;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class StaffServiceImpl extends ServiceImpl<StaffMapper, Staff> implements StaffService {

    @Autowired
    HubMapper hubMapper;

    @Autowired
    StaffMapper staffMapper;

    @Override
    public Result vertifyInfo(Map<String,Object> map){
        LambdaQueryWrapper<Hub> hubLambdaQueryWrapper = new LambdaQueryWrapper<>();
        hubLambdaQueryWrapper.eq(Hub::getId,map.get("hub_id"));
        Hub hub = hubMapper.selectOne(hubLambdaQueryWrapper);

        if (Objects.isNull(hub)){
            return Result.errorResult(AppHttpCodeEnum.HUB_ID_NOT_EXIST);
        }

        return Result.okResult();
    }

    @Override
    public Result getStaffList(int hub_id){
        LambdaQueryWrapper<Staff> staffLambdaQueryWrapper = new LambdaQueryWrapper<>();
        staffLambdaQueryWrapper.eq(Staff::getHub_id,hub_id);
        List<Staff> staff = staffMapper.selectList(staffLambdaQueryWrapper);
        return Result.okResult(staff);
    }

    @Override
    public Result addStaff(Staff staff){
        LambdaQueryWrapper<Staff> staffLambdaQueryWrapper = new LambdaQueryWrapper<>();
        staffLambdaQueryWrapper.eq(Staff::getContact,staff.getContact());
        Staff staff1 = staffMapper.selectOne(staffLambdaQueryWrapper);

        if (Objects.isNull(staff1)){
            staff.setName(staff.getName());
            staff.setAge(staff.getAge());
            staff.setSex(staff.getSex());
            staff.setContact(staff.getContact());
            staff.setHub_id(staff.getHub_id());
            save(staff);
//            LambdaQueryWrapper<Staff> staffLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
//            staffLambdaQueryWrapper1.eq(Staff::getStaffId,staff.getStaffId());
            return Result.okResult(staffMapper.selectOne(staffLambdaQueryWrapper));
        }
        else {
            return Result.errorResult(AppHttpCodeEnum.STAFF_EXIST);
        }
    }

    @Override
    public Result deleteStaff(int staffId){
        staffMapper.deleteById(staffId);
        return Result.okResult();
    }

    @Override
    public Result updateStaffInfo(Staff staff){
//        LambdaQueryWrapper<Staff> staffLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        staffLambdaQueryWrapper.eq(Staff::getContact,staff.getContact());
//        Staff staff1 = staffMapper.selectOne(staffLambdaQueryWrapper);
//        if(Objects.isNull(staff1)){
//            staffMapper.updateById(staff);
//        }
//        else {
//            return Result.errorResult(AppHttpCodeEnum.STAFF_EXIST);
//        }
        staffMapper.updateById(staff);
        return Result.okResult();
    }

    @Override
    public Result getStaff(Integer staffId,String name){
        if (staffId == null) {
            LambdaQueryWrapper<Staff> staffLambdaQueryWrapper = new LambdaQueryWrapper<>();
            staffLambdaQueryWrapper.like(Staff::getName,name);
            List<Staff> staff = staffMapper.selectList(staffLambdaQueryWrapper);

            if (Objects.isNull(staff)){
                return Result.errorResult(AppHttpCodeEnum.STAFF_NOT_EXIST);
            }
            return Result.okResult(staff);
        }
        else {
            Staff staff = staffMapper.selectById(staffId);
            if(Objects.isNull(staff)){
                return Result.errorResult(AppHttpCodeEnum.STAFF_NOT_EXIST);
            }
            return Result.okResult(staff);
        }
    }
}
