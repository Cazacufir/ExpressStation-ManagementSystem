package com.parcelhub.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parcelhub.entity.Deliver;
import com.parcelhub.entity.DeliverHubMerge;
import com.parcelhub.mapper.CompanyMapper;
import com.parcelhub.mapper.DeliverHubMergeMapper;
import com.parcelhub.service.DeliverHubMergeService;
import com.parcelhub.utils.AppHttpCodeEnum;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliverHubMergeServiceImpl extends ServiceImpl<DeliverHubMergeMapper, DeliverHubMerge> implements DeliverHubMergeService {

    @Autowired
    DeliverHubMergeMapper deliverHubMergeMapper;

    @Autowired
    CompanyMapper companyMapper;

    public Result getAllDeliver(int hubId){
        List<Deliver> deliverHubMerges = deliverHubMergeMapper.getAllDeliverByHubId(hubId);
        if (deliverHubMerges.isEmpty()){
            return Result.errorResult(AppHttpCodeEnum.DELIVER_NOT_FOUND);
        }
        deliverHubMerges.forEach(deliver -> { deliver.setComName(companyMapper.getName(deliver.getCom_id())); });
        return Result.okResult(deliverHubMerges);
    }
}
