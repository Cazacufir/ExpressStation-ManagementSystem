package com.parcelhub.controller;

import com.parcelhub.dto.OrderParcelMerge;
import com.parcelhub.service.ParcelService;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class parcelController {
    @Autowired
    ParcelService parcelService;

    @PostMapping("/updateSendParcel")
    public Result updateSendParcel(OrderParcelMerge orderParcelMerge){
        return parcelService.updateSendParcel(orderParcelMerge);
    }
}
