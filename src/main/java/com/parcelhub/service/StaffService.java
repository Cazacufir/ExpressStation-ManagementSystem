package com.parcelhub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.parcelhub.entity.Staff;
import com.parcelhub.utils.Result;

import java.util.Map;

public interface StaffService extends IService<Staff> {
    Result vertifyInfo(Map<String,Object> map);

    Result getStaffList(int hub_id);

    Result addStaff(Staff staff);

    Result deleteStaff(int staffId);

    Result updateStaffInfo(Staff staff);
}
