package com.parcelhub.controller;

import com.parcelhub.service.CarrierService;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CarrierController {
    @Autowired
    CarrierService carrierService;

    @GetMapping("/getCarrier")
    public Result getCarrier(Integer pageNum,Integer pageSize,int hub_id){
        return carrierService.getCarrier(pageNum,pageSize,hub_id);
    }

    @GetMapping("/getCarrierParcel")
    public Result getCarrierParcel(Integer pageNum,Integer pageSize,int carrierId){
        return carrierService.getCarrierParcel(pageNum,pageSize,carrierId);
    }
}
