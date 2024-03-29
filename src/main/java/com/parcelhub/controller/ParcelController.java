package com.parcelhub.controller;

import com.parcelhub.service.ParcelService;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ParcelController {
    @Autowired
    ParcelService parcelService;

    @GetMapping("/getReceiveParcel")
    public Result getReceiveParcel(int userId){
        return parcelService.getReceiveParcel(userId);
    }
}
