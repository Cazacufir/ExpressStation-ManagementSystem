package com.parcelhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parcelhub.entity.Deliver;
import com.parcelhub.entity.DeliverHubMerge;
import com.parcelhub.mapper.CompanyMapper;
import com.parcelhub.mapper.DeliverHubMergeMapper;
import com.parcelhub.mapper.DeliverMapper;
import com.parcelhub.service.DeliverService;
import com.parcelhub.utils.AppHttpCodeEnum;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class DeliverServiceImpl extends ServiceImpl<DeliverMapper, Deliver> implements DeliverService {

    @Autowired
    DeliverMapper deliverMapper;

    @Autowired
    CompanyMapper companyMapper;

    @Autowired
    DeliverHubMergeMapper deliverHubMergeMapper;

    public Result updateDeliver(Deliver deliver){
        deliverMapper.updateById(deliver);
        return Result.okResult();
    }

    public Result addDeliver(Deliver deliver){
        LambdaQueryWrapper<Deliver> deliverLambdaQueryWrapper = new LambdaQueryWrapper<>();
        deliverLambdaQueryWrapper.eq(Deliver::getContact,deliver.getContact());
        Deliver deliver1 = deliverMapper.selectOne(deliverLambdaQueryWrapper);

        if (Objects.isNull(deliver1)){
            DeliverHubMerge deliverHubMerge = new DeliverHubMerge();
            deliver.setName(deliver.getName());
            deliver.setAge(deliver.getAge());
            deliver.setSex(deliver.getSex());
            deliver.setContact(deliver.getContact());
            save(deliver);
            deliverHubMerge.setDeliver_id(deliver.getDeliverId());
            deliverHubMerge.setHub_id(deliver.getHub_id());
            deliverHubMergeMapper.insert(deliverHubMerge);
//            LambdaQueryWrapper<Staff> staffLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
//            staffLambdaQueryWrapper1.eq(Staff::getStaffId,staff.getStaffId());
            Deliver deliver2 = deliverMapper.selectOne(deliverLambdaQueryWrapper);
            deliver2.setComName(companyMapper.getName(deliver2.getCom_id()));
            return Result.okResult(deliver2);
        }
        else {
            return Result.errorResult(AppHttpCodeEnum.DELIVER_EXIST);
        }
    }

    @Override
    public Result getDeliver(Integer deliverId,String name){
        if (deliverId == null) {
            LambdaQueryWrapper<Deliver> deliverLambdaQueryWrapper = new LambdaQueryWrapper<>();
            deliverLambdaQueryWrapper.like(Deliver::getName,name);
            List<Deliver> deliver = deliverMapper.selectList(deliverLambdaQueryWrapper);

            if (Objects.isNull(deliver)){
                return Result.errorResult(AppHttpCodeEnum.STAFF_NOT_EXIST);
            }
            deliver.forEach(deliver1 -> {deliver1.setComName(companyMapper.getName(deliver1.getCom_id()));});
            return Result.okResult(deliver);
        }
        else {
            Deliver deliver = deliverMapper.selectById(deliverId);
            if(Objects.isNull(deliver)){
                return Result.errorResult(AppHttpCodeEnum.STAFF_NOT_EXIST);
            }
            deliver.setComName(companyMapper.getName(deliver.getCom_id()));
            return Result.okResult(deliver);
        }
    }
}
