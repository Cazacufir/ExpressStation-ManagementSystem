package com.parcelhub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.parcelhub.entity.Parcel;
import com.parcelhub.entity.UserParcelMerge;
import com.parcelhub.utils.Result;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

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

    Result sendParcelByHub(Map<String,Object> map);

    Result updateRoute(Parcel parcel);

    Result getSendingParcel(Integer pageNum,Integer pageSize,int hub_id);

    Result searchSendingParcel(int hub_id,int parcelId,String word);

    Result receiveParcelByHub(Parcel parcel);

    Result getAllParcelByHub(Integer pageNum,Integer pageSize,int hub_id);

    Result searchAllParcelByHub(int hub_id,int parcelId,String word);

    Result getReceivedParcel(Parcel parcel);

    Result getReceivedParcelByUser(Parcel parcel);

    Result receiveSingleParcel(int parcelId);

    Result receiveParcelByUser(List<Parcel> parcels);

    Result getAllReserveParcel(Integer pageNum,Integer pageSize,int hub_id);

    Result returnParcel(int parcelId);

    Result countParcel(int hub_id);

    Result getKindsParcel(int hub_id);

    Result countUser(int hub_id);

    Result countSRParcel(int hub_id);

    Result deleteParcelRecord(Parcel parcel);

    Result selectAllinHub(int hub_id,String start,String end);

    Result outParcelList(List<Map<String,Object>> mapList);

    Result inParcelList(List<Parcel> parcelList);
}
