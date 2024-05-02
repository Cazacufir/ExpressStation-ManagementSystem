package com.parcelhub.controller;

import com.parcelhub.dto.OrderParcelMerge;
import com.parcelhub.service.OrderService;
import com.parcelhub.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
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

    @GetMapping("/cancelSendList")
    public Result cancelSendList(@RequestParam int orderId){
        return orderService.cancelSendList(orderId);
    }

    @GetMapping("/getSearchSendList")
    public Result getSearchSendList(HttpServletRequest request,Integer parcelId,String word){
        return orderService.getSearchSendList(request,parcelId,word);
    }

    @GetMapping("/getSendListByHub")
    public Result getSendListByHub(Integer pageNum,Integer pageSize,int hub_id){
        return orderService.getSendListByHub(pageNum,pageSize,hub_id);
    }

    @GetMapping("/searchSendListByHub")
    public Result searchSendListByHub(Integer hub_id,Integer parcelId,String word){
        return orderService.searchSendListByHub(hub_id,parcelId,word);
    }

    @GetMapping("/getPriceWeek")
    public Result getPriceWeek(@RequestParam int hub_id){
        return orderService.getPriceWeek(hub_id);
    }

    @GetMapping("/getOutedParcel")
    public Result getOutedParcel(Integer pageNum, Integer pageSize,int hub_id){
        return orderService.getOutedParcel(pageNum,pageSize,hub_id);
    }

    @GetMapping("/searchOutedParcel")
    public Result searchOutedParcel(Integer hub_id,Integer parcelId,String word){
        return orderService.searchOutedParcel(hub_id,parcelId,word);
    }
}
