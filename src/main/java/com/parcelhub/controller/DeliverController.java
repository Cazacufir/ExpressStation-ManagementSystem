package com.parcelhub.controller;

import com.parcelhub.entity.Deliver;
import com.parcelhub.service.DeliverHubMergeService;
import com.parcelhub.service.DeliverService;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
