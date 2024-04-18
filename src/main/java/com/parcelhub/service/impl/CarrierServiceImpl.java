package com.parcelhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parcelhub.entity.Carrier;
import com.parcelhub.mapper.CarrierMapper;
import com.parcelhub.service.CarrierService;
import com.parcelhub.utils.AppHttpCodeEnum;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarrierServiceImpl extends ServiceImpl<CarrierMapper, Carrier> implements CarrierService {

    @Autowired
    CarrierMapper carrierMapper;

    @Override
    public Result getCarrier(int hub_id){
        LambdaQueryWrapper<Carrier> carrierLambdaQueryWrapper = new LambdaQueryWrapper<>();
        carrierLambdaQueryWrapper.eq(Carrier::getHub_id,hub_id);
        List<Carrier> carrierList = carrierMapper.selectList(carrierLambdaQueryWrapper);
        if(carrierList.size() == 0){
            return Result.errorResult(AppHttpCodeEnum.PARCEL_NOT_FOUND);
        }
        return Result.okResult(carrierList);
    }
}
