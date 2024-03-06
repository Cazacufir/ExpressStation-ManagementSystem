package com.parcelhub.controller;

import com.parcelhub.entity.Hub;
import com.parcelhub.mapper.HubMapper;
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
}
