package com.parcelhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parcelhub.dto.PagesDto;
import com.parcelhub.entity.Carrier;
import com.parcelhub.entity.Parcel;
import com.parcelhub.mapper.CarrierFlatMapper;
import com.parcelhub.mapper.CarrierMapper;
import com.parcelhub.service.CarrierService;
import com.parcelhub.utils.AppHttpCodeEnum;
import com.parcelhub.utils.PageUtils;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarrierServiceImpl extends ServiceImpl<CarrierMapper, Carrier> implements CarrierService {

    @Autowired
    CarrierMapper carrierMapper;

    @Autowired
    CarrierFlatMapper carrierFlatMapper;

    @Override
    public Result getCarrier(Integer pageNum,Integer pageSize,int hub_id){
        LambdaQueryWrapper<Carrier> carrierLambdaQueryWrapper = new LambdaQueryWrapper<>();
        carrierLambdaQueryWrapper.eq(Carrier::getHub_id,hub_id);

        Page<Carrier> carrierPage = new Page<>(pageNum,pageSize);
        page(carrierPage,carrierLambdaQueryWrapper);
        int total = (int) carrierPage.getTotal();
        if(total == 0){
            return Result.errorResult(AppHttpCodeEnum.PARCEL_NOT_FOUND);
        }
        return Result.okResult(carrierPage);
    }

    @Override
    public Result getCarrierParcel(Integer pageNum,Integer pageSize,int carrierId){
        List<Parcel> parcelList = carrierFlatMapper.getCarrierParcelList(carrierId);
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, parcelList.size());

        List<Parcel> parcelList1 = parcelList.subList(startIndex,endIndex);
        PagesDto<Parcel> parcelPagesDto = PageUtils.listToPageDTO(parcelList,pageNum,pageSize);
        parcelPagesDto.setDataList(parcelList1);
        if(parcelPagesDto.getTotalElements() == 0){
            return Result.errorResult(AppHttpCodeEnum.PARCEL_NOT_FOUND);
        }
        return Result.okResult(parcelPagesDto);
    }

    @Override
    public Result addCarrier(Carrier carrier){
        LambdaQueryWrapper<Carrier> carrierLambdaQueryWrapper = new LambdaQueryWrapper<>();
        carrierLambdaQueryWrapper.eq(Carrier::getHub_id,carrier.getHub_id())
                .eq(Carrier::getNum,carrier.getNum());
        List<Carrier> carrierList = carrierMapper.selectList(carrierLambdaQueryWrapper);
        if(carrierList.size() > 0){
            return Result.errorResult(AppHttpCodeEnum.CARRIER_EXIST);
        }
        Carrier carrier1 = new Carrier();
        carrier1.setHub_id(carrier.getHub_id());
        carrier1.setNum(carrier.getNum());
        carrier1.setFlats(carrier.getFlats());
        carrier1.setMaxCount(carrier.getMaxCount());
        save(carrier1);
        return Result.okResult();
    }
}
