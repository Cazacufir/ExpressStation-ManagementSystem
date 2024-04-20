package com.parcelhub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.parcelhub.entity.Parcel;
import com.parcelhub.entity.Reserve;
import com.parcelhub.utils.Result;

import java.util.List;
import java.util.Map;

public interface ReserveService extends IService<Reserve> {
    Result addReserve(List<Map<String,Object>> maps);
}
