package com.parcelhub.controller;

import com.parcelhub.entity.Hub;
import com.parcelhub.service.HubService;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
        return hubService.getCompanyList(hub_id);
    }

    @GetMapping("/companyName")
    public Result getcompanyName(){
        return hubService.getCompanyName();
    }

    @DeleteMapping("/deleteCompany")
    public Result deleteCompany(@RequestParam int mapId){
        return hubService.deleteCompany(mapId);
    }

    @PostMapping("/addCompany")
    public Result addCompany(@RequestBody Map<String,Integer> map){
        return hubService.addCompany(map);
    }

    @PostMapping("/vertifyHub")
    public Result vertifyHub(@RequestBody Map<String,Object> map){
        return hubService.vertifyHub(map);
    }
}
