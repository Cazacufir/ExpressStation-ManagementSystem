package com.parcelhub.controller;

import com.parcelhub.mapper.HubMapper;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HubController {

    @Autowired
    HubMapper hubMapper;

    @GetMapping("/mainInfo")
    public Result getMainInfo(@RequestParam int Id){
        return Result.okResult(hubMapper.selectById(Id));
    }
}
