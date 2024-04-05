package com.parcelhub.controller;

import com.parcelhub.entity.Parcel;
import com.parcelhub.entity.UserParcelMerge;
import com.parcelhub.service.ParcelService;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ParcelController {
    @Autowired
    ParcelService parcelService;

    @GetMapping("/getReceiveParcel")
    public Result getReceiveParcel(@RequestParam int userId){
        return parcelService.getReceiveParcel(userId);
    }

    @GetMapping("/getMoreParcel")
    public Result getMoreParcel(@RequestParam int userId){
        return parcelService.getExtraParcel(userId);
    }

    @PostMapping("/addExtraParcel")
    public Result addExtraParcel(UserParcelMerge userParcelMerge){
        return parcelService.addExtraParcel(userParcelMerge);
    }

    @GetMapping("/getRecentSendParcel")
    public Result getRecentSendParcel(@RequestParam int userId){
        return parcelService.getRecentSendParcel(userId);
    }

    @PostMapping("/getRecentReceiveParcel")
    public Result getRecentReceiveParcel(@RequestBody Parcel parcel){
        return parcelService.getRecentReceiveParcel(parcel);
    }
}
