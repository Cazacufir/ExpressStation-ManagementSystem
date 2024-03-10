package com.parcelhub.controller;

import com.parcelhub.entity.Deliver;
import com.parcelhub.service.DeliverHubMergeService;
import com.parcelhub.service.DeliverService;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class DeliverController {
    @Autowired
    DeliverHubMergeService deliverHubMergeService;

    @Autowired
    DeliverService deliverService;

    @GetMapping("/getAllDeliver")
    public Result getAllDeliver(int hubId){
        return deliverHubMergeService.getAllDeliver(hubId);
    }

    @PutMapping("/updateDeliver")
    public Result updateDeliver(Deliver deliver){
        return deliverService.updateDeliver(deliver);
    }

    @PostMapping("/addDeliver")
    public Result addDeliver(Deliver deliver){
        return deliverService.addDeliver(deliver);
    }

    @DeleteMapping("/deleteDeliver")
    public Result deleteDeliver(int deliverId){
        return deliverService.deleteDeliver(deliverId);
    }
}
