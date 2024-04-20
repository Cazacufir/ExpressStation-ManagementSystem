package com.parcelhub.controller;

import com.parcelhub.entity.Delay;
import com.parcelhub.service.DelayService;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DelayController {
    @Autowired
    DelayService delayService;

    @PostMapping("/addDelay")
    public Result addDelay(@RequestBody Delay delay){
        return delayService.addDelay(delay);
    }
}
