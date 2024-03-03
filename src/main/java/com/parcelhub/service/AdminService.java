package com.parcelhub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.parcelhub.entity.Admin;
import com.parcelhub.utils.Result;

public interface AdminService extends IService<Admin> {
    Result register(Admin admin);
}
