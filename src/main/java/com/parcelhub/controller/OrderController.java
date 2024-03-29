package com.parcelhub.controller;

import com.parcelhub.dto.OrderParcelMerge;
import com.parcelhub.service.OrderService;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {
    @Autowired
    OrderService orderService;

    @GetMapping("/getSendParcel")
    public Result getSendParcel(@RequestParam int user_id){
        return orderService.getSendList(user_id);
    }

    @PostMapping("/addSendList")
    public Result addSendList(@RequestBody OrderParcelMerge orderParcelMerge){
        return orderService.addSendList(orderParcelMerge);
    }

    @PutMapping("/updateSendList")
    public Result updateSendList(@RequestBody OrderParcelMerge orderParcelMerge){
        return orderService.updateSendList(orderParcelMerge);
    }
}
