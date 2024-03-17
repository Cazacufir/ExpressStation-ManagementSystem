package com.parcelhub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.parcelhub.entity.DeliverHubMerge;
import com.parcelhub.utils.Result;

public interface DeliverHubMergeService extends IService<DeliverHubMerge> {
    Result getAllDeliver(int hubId);

    Result deleteDeliver(int mapId);
}
