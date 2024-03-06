package com.parcelhub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.parcelhub.entity.Hub;
import com.parcelhub.utils.Result;

public interface HubService extends IService<Hub> {
    Result getMainInfo(int Id);

    Result updateHubInfo(Hub hub);
}
