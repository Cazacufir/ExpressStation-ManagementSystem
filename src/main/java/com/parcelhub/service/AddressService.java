package com.parcelhub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.parcelhub.entity.Address;
import com.parcelhub.utils.Result;

import java.util.Map;

public interface AddressService extends IService<Address> {
    Result getAddressList(int user_id);

    Result updateAddress(Map<String,Object> map);
}
