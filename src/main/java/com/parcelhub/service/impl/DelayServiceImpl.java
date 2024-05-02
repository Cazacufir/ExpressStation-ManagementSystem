package com.parcelhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parcelhub.dto.PagesDto;
import com.parcelhub.entity.Delay;
import com.parcelhub.entity.Parcel;
import com.parcelhub.entity.Reserve;
import com.parcelhub.mapper.DelayMapper;
import com.parcelhub.mapper.ReserveMapper;
import com.parcelhub.service.DelayService;
import com.parcelhub.utils.AppHttpCodeEnum;
import com.parcelhub.utils.PageUtils;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class DelayServiceImpl extends ServiceImpl<DelayMapper, Delay> implements DelayService {

    @Autowired
    DelayMapper delayMapper;

    @Autowired
    ReserveMapper reserveMapper;

    @Override
    public Result addDelay(Delay delay){
        LambdaQueryWrapper<Reserve> reserveLambdaQueryWrapper = new LambdaQueryWrapper<>();
        reserveLambdaQueryWrapper.eq(Reserve::getParcel_id,delay.getParcel_id());
        Reserve reserve = reserveMapper.selectOne(reserveLambdaQueryWrapper);
        if (!Objects.isNull(reserve)){
            return Result.errorResult(AppHttpCodeEnum.PARCEL_RESERVED);
        }
        LambdaQueryWrapper<Delay> delayLambdaQueryWrapper = new LambdaQueryWrapper<>();
        delayLambdaQueryWrapper.eq(Delay::getParcel_id,delay.getParcel_id());
        Delay delay1 = delayMapper.selectOne(delayLambdaQueryWrapper);
        if(Objects.isNull(delay1)){
            delayMapper.insert(delay);
        }
        else{
            delay1.setDays(delay.getDays());
            if (!Objects.isNull(delay.getReason())){
                delay1.setReason(delay.getReason());
            }
            delayMapper.updateById(delay1);
        }
        return Result.okResult();
    }

    @Override
    public Result getDelayParcel(Integer pageNum,Integer pageSize,int hub_id){
        List<Parcel> parcelList = delayMapper.getDelayParcelByHub(hub_id);
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, parcelList.size());
        List<Parcel> parcelList1 = parcelList.subList(startIndex,endIndex);
        PagesDto<Parcel> parcelPagesDto = PageUtils.listToPageDTO(parcelList,pageNum,pageSize);
        parcelPagesDto.setDataList(parcelList1);

        if (parcelPagesDto.getTotalElements() == 0){
            return Result.errorResult(AppHttpCodeEnum.PARCEL_NOT_FOUND);
        }
        return Result.okResult(parcelPagesDto);
    }
}
