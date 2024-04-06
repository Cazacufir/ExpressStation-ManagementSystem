package com.parcelhub.controller;

import com.parcelhub.entity.Parcel;
import com.parcelhub.entity.UserParcelMerge;
import com.parcelhub.service.ParcelService;
import com.parcelhub.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
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

    @GetMapping("/getExtraParcel")
    public Result getExtraParcel(HttpServletRequest request){
        return parcelService.getExtraParcel(request);
    }

    @DeleteMapping("/deleteExtraParcel")
    public Result deleteExtraParcel(HttpServletRequest request,@RequestBody UserParcelMerge userParcelMerge){
        return parcelService.deleteExtraParcel(request,userParcelMerge);
    }

    @PostMapping("/addExtraParcel")
    public Result addExtraParcel(@RequestBody UserParcelMerge userParcelMerge){
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

    @GetMapping("/getSearchParcel")
    public Result getSearchParcel(@RequestParam int parcelId){
        return parcelService.getSearchParcel(parcelId);
    }

    @GetMapping("/getSearchReceiveList")
    public Result getSearchReceiveList(HttpServletRequest request, Integer parcelId, String word){
        return parcelService.getSearchReceiveList(request,parcelId,word);
    }
}
