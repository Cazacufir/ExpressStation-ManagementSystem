package com.parcelhub.controller;

import com.parcelhub.service.CarrierService;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CarrierController {
    @Autowired
    CarrierService carrierService;

    @GetMapping("/getCarrier")
    public Result getCarrier(@RequestParam int hub_id){
        return carrierService.getCarrier(hub_id);
    }
}
