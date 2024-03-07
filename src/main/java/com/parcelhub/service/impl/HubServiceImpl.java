package com.parcelhub.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parcelhub.entity.Company;
import com.parcelhub.entity.Hub;
import com.parcelhub.mapper.ComHubMergeMapper;
import com.parcelhub.mapper.HubMapper;
import com.parcelhub.service.HubService;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HubServiceImpl extends ServiceImpl<HubMapper, Hub>  implements HubService {
    @Autowired
    private HubMapper hubMapper;

    @Autowired
    private ComHubMergeMapper comHubMergeMapper;

    public Result getMainInfo(int Id){
        return Result.okResult(hubMapper.selectById(Id));
    }

    public Result updateHubInfo(Hub hub){
        hubMapper.updateById(hub);
        return Result.okResult("更新成功!");
    }

    public Result getCompany(int hubId){
        List<Company> companyList = comHubMergeMapper.getCompanyByHubId(hubId);
        if(companyList.isEmpty()){
            return Result.errorResult2(404,"暂未与任何快递公司合作!");
        }
        else {
            return Result.okResult(companyList);
        }
    }
}
