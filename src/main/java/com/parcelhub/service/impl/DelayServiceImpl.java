package com.parcelhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parcelhub.entity.Delay;
import com.parcelhub.mapper.DelayMapper;
import com.parcelhub.service.DelayService;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class DelayServiceImpl extends ServiceImpl<DelayMapper, Delay> implements DelayService {

    @Autowired
    DelayMapper delayMapper;

    @Override
    public Result addDelay(Delay delay){
        LambdaQueryWrapper<Delay> delayLambdaQueryWrapper = new LambdaQueryWrapper<>();
        delayLambdaQueryWrapper.eq(Delay::getParcel_id,delay.getParcel_id());
        Delay delay1 = delayMapper.selectOne(delayLambdaQueryWrapper);
        if(Objects.isNull(delay1)){
            delayMapper.insert(delay);
        }
        else{
            delay1.setDays(delay.getDays());
            delay1.setReason(delay.getReason());
            delayMapper.updateById(delay1);
        }
        return Result.okResult();
    }
}
