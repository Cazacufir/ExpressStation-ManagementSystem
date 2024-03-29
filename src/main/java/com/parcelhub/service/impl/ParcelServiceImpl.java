package com.parcelhub.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parcelhub.dto.OrderParcelMerge;
import com.parcelhub.entity.Parcel;
import com.parcelhub.mapper.ParcelMapper;
import com.parcelhub.service.ParcelService;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParcelServiceImpl extends ServiceImpl<ParcelMapper, Parcel> implements ParcelService {

    @Autowired
    ParcelMapper parcelMapper;

    @Override
    public Result updateSendParcel(OrderParcelMerge orderParcelMerge){
        Parcel parcel = parcelMapper.selectById(orderParcelMerge.getParcelId());

        parcel.setReceiveName(orderParcelMerge.getReceiveName());
        parcel.setReceiveAddress(orderParcelMerge.getReceiveAddress());
        parcel.setReceiveContact(orderParcelMerge.getReceiveContact());
        save(parcel);

        return Result.okResult();
    }
}
