package com.parcelhub.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parcelhub.entity.Deliver;
import com.parcelhub.mapper.DeliverMapper;
import com.parcelhub.service.DeliverService;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliverServiceImpl extends ServiceImpl<DeliverMapper, Deliver> implements DeliverService {

    @Autowired
    DeliverMapper deliverMapper;

    public Result updateDeliver(Deliver deliver){
        deliverMapper.updateById(deliver);
        return Result.okResult();
    }

    public Result addDeliver(Deliver deliver){
        deliverMapper.insert(deliver);
        return Result.okResult();
    }

    public Result deleteDeliver(int deliverId){
        deliverMapper.deleteById(deliverId);
        return Result.okResult();
    }
}
