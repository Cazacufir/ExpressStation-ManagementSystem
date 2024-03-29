package com.parcelhub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.parcelhub.dto.OrderParcelMerge;
import com.parcelhub.entity.Parcel;
import com.parcelhub.utils.Result;

public interface ParcelService extends IService<Parcel> {
    Result updateSendParcel(OrderParcelMerge orderParcelMerge);
}
