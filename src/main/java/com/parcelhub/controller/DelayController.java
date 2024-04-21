package com.parcelhub.controller;

import com.parcelhub.entity.Delay;
import com.parcelhub.service.DelayService;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class DelayController {
    @Autowired
    DelayService delayService;

    @PostMapping("/addDelay")
    public Result addDelay(@RequestBody Delay delay){
        return delayService.addDelay(delay);
    }

    @GetMapping("/getDelayParcel")
    public Result getDelayParcel(Integer pageNum,Integer pageSize,int hub_id){
        return delayService.getDelayParcel(pageNum,pageSize,hub_id);
    }
}
