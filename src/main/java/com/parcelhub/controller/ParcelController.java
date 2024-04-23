package com.parcelhub.controller;

import com.parcelhub.entity.Parcel;
import com.parcelhub.entity.UserParcelMerge;
import com.parcelhub.service.ParcelService;
import com.parcelhub.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/getRoute")
    public Result getRoute(@RequestParam int parcelId){
        return parcelService.getRoute(parcelId);
    }

    @PostMapping("/sendParcelByHub")
    public Result sendParcelByHub(@RequestBody Map<String,Object> map){
        return parcelService.sendParcelByHub(map);
    }

    @PutMapping("/updateRoute")
    public Result updateRoute(@RequestBody Parcel parcel){
        return parcelService.updateRoute(parcel);
    }

    @GetMapping("/getSendingParcel")
    public Result getSendingParcel(Integer pageNum,Integer pageSize,int hub_id){
        return parcelService.getSendingParcel(pageNum,pageSize,hub_id);
    }

    @PostMapping("/receiveParcelByHub")
    public Result receiveParcelByHub(@RequestBody Parcel parcel){
        return parcelService.receiveParcelByHub(parcel);
    }

    @GetMapping("/getAllParcelByHub")
    public Result getAllParcelByHub(Integer pageNum,Integer pageSize,int hub_id){
        return parcelService.getAllParcelByHub(pageNum,pageSize,hub_id);
    }

    @PostMapping("/getReceivedParcel")
    public Result getReceivedParcel(@RequestBody Parcel parcel){
        return parcelService.getReceivedParcel(parcel);
    }

    @GetMapping("/receiveSingleParcel")
    public Result receiveSingleParcel(@RequestParam int parcelId){
        return parcelService.receiveSingleParcel(parcelId);
    }

    @PostMapping("/receiveParcelByUser")
    public Result receiveParcelByUser(@RequestBody List<Parcel> parcels){
        return parcelService.receiveParcelByUser(parcels);
    }

    @GetMapping("/getAllReserveParcel")
    public Result getAllReserveParcel(Integer pageNum,Integer pageSize,int hub_id){
        return parcelService.getAllReserveParcel(pageNum,pageSize,hub_id);
    }

    @GetMapping("/returnParcel")
    public Result returnParcel(@RequestParam int parcelId){
        return parcelService.returnParcel(parcelId);
    }

    @GetMapping("/countParcel")
    public Result countParcel(@RequestParam int hub_id){
        return parcelService.countParcel(hub_id);
    }

    @GetMapping("/getKindsParcel")
    public Result getKindsParcel(@RequestParam int hub_id){
        return parcelService.getKindsParcel(hub_id);
    }
}
