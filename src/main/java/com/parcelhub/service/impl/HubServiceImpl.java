package com.parcelhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

@Service
public class HubServiceImpl extends ServiceImpl<HubMapper, Hub>  implements HubService {
    @Autowired
    private HubMapper hubMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private ComHubMergeMapper comHubMergeMapper;

    @Override
    public Result getMainInfo(int Id){
        return Result.okResult(hubMapper.selectById(Id));
    }

    @Override
    public Result updateHubInfo(Hub hub){
        hubMapper.updateById(hub);
        return Result.okResult("更新成功!");
    }

    @Override
    public Result getCompanyList(int hubId){
        List<Company> companyList = comHubMergeMapper.getCompanyByHubId(hubId);
        if(companyList.isEmpty()){
            return Result.errorResult2(404,"暂未与任何快递公司合作!");
        }
        else {
            return Result.okResult(companyList);
        }
    }

    @Override
    public Result getCompanyName(){
        return Result.okResult(companyMapper.getAllName());
    }

    @Override
    public Result deleteCompany(int mapId){
//        QueryWrapper<ComHubMerge> comHubMergeQueryWrapper = new QueryWrapper<>();
//        comHubMergeQueryWrapper.eq("com_id",com_id)
//                .eq("hub_id",hub_id)
//                .eq("delFlag",0);
//        ComHubMerge comHubMerge = comHubMergeMapper.selectOne(comHubMergeQueryWrapper);
//        comHubMergeMapper.deleteById(comHubMerge.getMapId());
        comHubMergeMapper.deleteById(mapId);
        return Result.okResult("取消合作成功");
    }

    @Override
    public Result addCompany(Map<String,Integer> map){
        int com_id = map.get("com_id");
        int hub_id = map.get("hub_id");
        ComHubMerge comHubMerge = new ComHubMerge();
        comHubMerge.setCom_id(com_id);
        comHubMerge.setHub_id(hub_id);
        comHubMergeMapper.insert(comHubMerge);

        LambdaQueryWrapper<ComHubMerge> comHubMergeQueryWrapper = new LambdaQueryWrapper<>();
        comHubMergeQueryWrapper.eq(ComHubMerge::getMapId,comHubMerge.getMapId());
        Company company = comHubMergeMapper.getCompanyByMapId(comHubMerge.getMapId());
        return Result.okResult(company);
    }

    @Override
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

    @Override
    public Result getNearHub(String address){
        LambdaQueryWrapper<Hub> hubLambdaQueryWrapper = new LambdaQueryWrapper<>();
        hubLambdaQueryWrapper.like(Hub::getAddress,address);
        List<Hub> hub = hubMapper.selectList(hubLambdaQueryWrapper);
        if (hub.size() == 0){
            return Result.errorResult(AppHttpCodeEnum.HUB_NOT_FOUND);
        }
        return Result.okResult(hub);
    }
}
