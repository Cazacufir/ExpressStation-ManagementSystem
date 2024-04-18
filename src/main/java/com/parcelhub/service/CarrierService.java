package com.parcelhub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.parcelhub.entity.Carrier;
import com.parcelhub.utils.Result;

public interface CarrierService extends IService<Carrier> {
    Result getCarrier(Integer pageNum,Integer pageSize,int hub_id);

    Result getCarrierParcel(Integer pageNum,Integer pageSize,int carrierId);

    Result addCarrier(Carrier carrier);
}