package com.parcelhub.controller;

import com.parcelhub.service.StaffService;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class StaffController {

    @Autowired
    StaffService staffService;
    @PostMapping("/vertifyInfo")
    public Result vertifyInfo(@RequestBody Map<String,Object> map){
        return staffService.vertifyInfo(map);
    }
}
