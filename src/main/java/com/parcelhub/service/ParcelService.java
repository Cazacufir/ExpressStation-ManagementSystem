package com.parcelhub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.parcelhub.entity.Parcel;
import com.parcelhub.entity.UserParcelMerge;
import com.parcelhub.utils.Result;
import jakarta.servlet.http.HttpServletRequest;

public interface ParcelService extends IService<Parcel> {
    Result getReceiveParcel(int userId);

    Result getExtraParcel(HttpServletRequest request);

    Result addExtraParcel(UserParcelMerge userParcelMerge);

    Result deleteExtraParcel(HttpServletRequest request,UserParcelMerge userParcelMerge);

    Result getRecentSendParcel(int userId);

    Result getRecentReceiveParcel(Parcel parcel);

    Result getSearchParcel(int parcelId);

    Result getSearchReceiveList(HttpServletRequest request, Integer parcelId, String word);

    Result getRoute(int parcelId);
}
