package com.parcelhub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.parcelhub.entity.Hub;
import com.parcelhub.utils.Result;

import java.util.Map;

public interface HubService extends IService<Hub> {
    Result getMainInfo(int Id);

    Result updateHubInfo(Hub hub);

    Result getCompanyList(int hubId);

    Result deleteCompany(int com_id,int hub_id);

    Result addCompany(int com_id,int hub_id);

    Result vertifyHub(Map<String,Object> map);
}
