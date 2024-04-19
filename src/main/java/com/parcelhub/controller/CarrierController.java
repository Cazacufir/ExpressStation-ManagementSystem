package com.parcelhub.controller;

import com.parcelhub.entity.Carrier;
import com.parcelhub.service.CarrierService;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/addCarrier")
    public Result addCarrier(@RequestBody Carrier carrier){
        return carrierService.addCarrier(carrier);
    }

    @PutMapping("/updateCarrier")
    public Result updateCarrier(@RequestBody Carrier carrier){
        return carrierService.updateCarrier(carrier);
    }
    @DeleteMapping("/deleteCarrier")
    public Result deleteCarrier(@RequestParam int carrierId){
        return carrierService.deleteCarrier(carrierId);
    }
}
