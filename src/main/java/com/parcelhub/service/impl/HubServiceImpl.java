package com.parcelhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parcelhub.entity.ComHubMerge;
import com.parcelhub.entity.Company;
import com.parcelhub.entity.Hub;
import com.parcelhub.mapper.ComHubMergeMapper;
import com.parcelhub.mapper.CompanyMapper;
import com.parcelhub.mapper.HubMapper;
import com.parcelhub.service.HubService;
import com.parcelhub.utils.AppHttpCodeEnum;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class HubServiceImpl extends ServiceImpl<HubMapper, Hub>  implements HubService {
    @Autowired
    private HubMapper hubMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private ComHubMergeMapper comHubMergeMapper;

    public Result getMainInfo(int Id){
        return Result.okResult(hubMapper.selectById(Id));
    }

    public Result updateHubInfo(Hub hub){
        hubMapper.updateById(hub);
        return Result.okResult("更新成功!");
    }

    public Result getCompanyList(int hubId){
        List<Company> companyList = comHubMergeMapper.getCompanyByHubId(hubId);
        if(companyList.isEmpty()){
            return Result.errorResult2(404,"暂未与任何快递公司合作!");
        }
        else {
            return Result.okResult(companyList);
        }
    }

    public Result getCompanyName(){
//        LambdaQueryWrapper<Company> companyLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        companyLambdaQueryWrapper.eq(Company::getName,"");
//        return Result.okResult(companyMapper.selectList(companyLambdaQueryWrapper));
        return Result.okResult(companyMapper.getAllName());
    }

    public Result deleteCompany(int com_id,int hub_id){
        QueryWrapper<ComHubMerge> comHubMergeQueryWrapper = new QueryWrapper<>();
        comHubMergeQueryWrapper.eq("com_id",com_id)
                .eq("hub_id",hub_id)
                .eq("delFlag",0);
        ComHubMerge comHubMerge = comHubMergeMapper.selectOne(comHubMergeQueryWrapper);
        comHubMergeMapper.deleteById(comHubMerge.getMapId());
        return Result.okResult("取消合作成功");
    }

    public Result addCompany(int com_id,int hub_id){
        ComHubMerge comHubMerge = new ComHubMerge();
        comHubMerge.setCom_id(com_id);
        comHubMerge.setHub_id(hub_id);
        comHubMergeMapper.insert(comHubMerge);
        return Result.okResult();
    }

    public Result vertifyHub(Map<String,Object> map){
        if(map.get("address") == "" || map.get("name") == ""){
            return Result.errorResult(AppHttpCodeEnum.NAME_OR_ADDRESS_NOT_EXIST);
        }
        LambdaQueryWrapper<Hub> hubLambdaQueryWrapper = new LambdaQueryWrapper<>();
        hubLambdaQueryWrapper.eq(Hub::getAddress,map.get("address"));
        List<Hub> hub1 = hubMapper.selectList(hubLambdaQueryWrapper);

        LambdaQueryWrapper<Hub> hubLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        hubLambdaQueryWrapper1.eq(Hub::getName,map.get("name"));
        List<Hub> hub2 = hubMapper.selectList(hubLambdaQueryWrapper1);

        if(hub1.size()>1){
            return Result.errorResult(AppHttpCodeEnum.ADDRESS_EXIST);
        }
        if(hub2.size()>1){
            return Result.errorResult(AppHttpCodeEnum.HUB_NAME_EXIST);
        }
        return Result.okResult();
    }
}
