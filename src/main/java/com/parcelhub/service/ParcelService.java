package com.parcelhub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.parcelhub.entity.Parcel;
import com.parcelhub.entity.UserParcelMerge;
import com.parcelhub.utils.Result;

public interface ParcelService extends IService<Parcel> {
    Result getReceiveParcel(int userId);

    Result getExtraParcel(int userId);

    Result addExtraParcel(UserParcelMerge userParcelMerge);

    Result getRecentSendParcel(int userId);

    Result getRecentReceiveParcel(Parcel parcel);
}
