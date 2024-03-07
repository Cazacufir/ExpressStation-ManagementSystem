package com.parcelhub.controller;

import com.parcelhub.entity.Hub;
import com.parcelhub.service.HubService;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class HubController {

    @Autowired
    HubService hubService;

    @GetMapping("/mainInfo")
    public Result getMainInfo(@RequestParam int Id){
        return hubService.getMainInfo(Id);
    }

    @PutMapping("/updateHubInfo")
    public Result updateHubInfo(@RequestBody Hub hub){
        return hubService.updateHubInfo(hub);
    }

    @GetMapping("/companyList")
    public Result getCompany(@RequestParam int hub_id){
        return hubService.getCompany(hub_id);
    }

    @DeleteMapping("/deleteCompany")
    public Result deleteCompany(@RequestParam int com_id,@RequestParam int hub_id){
        return hubService.deleteCompany(com_id,hub_id);
    }
}
