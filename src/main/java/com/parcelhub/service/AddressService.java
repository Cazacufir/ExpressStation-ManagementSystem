package com.parcelhub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.parcelhub.entity.Address;
import com.parcelhub.utils.Result;

import java.util.List;
import java.util.Map;

public interface AddressService extends IService<Address> {
    Result getAddressList(int user_id);

    Result updateAddress(Map<String,Object> map);

    Result addAddress(Map<String,Object> map);

    Result deleteAddress(List<Address> addresses);
}
