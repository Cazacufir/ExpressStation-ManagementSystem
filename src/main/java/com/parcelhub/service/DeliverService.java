package com.parcelhub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.parcelhub.entity.Deliver;
import com.parcelhub.utils.Result;

public interface DeliverService extends IService<Deliver> {

    Result updateDeliver(Deliver deliver);
}
