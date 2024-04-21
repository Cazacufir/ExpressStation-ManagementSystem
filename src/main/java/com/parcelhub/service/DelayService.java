package com.parcelhub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.parcelhub.entity.Delay;
import com.parcelhub.utils.Result;

public interface DelayService extends IService<Delay> {
    Result addDelay(Delay delay);

    Result getDelayParcel(Integer pageNum,Integer pageSize,int hub_id);
}
