package com.parcelhub.controller;

import com.parcelhub.service.ReserveService;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ReserveController {

    @Autowired
    ReserveService reserveService;

    @PostMapping("/addReserve")
    public Result addReserve(@RequestBody List<Map<String,Object>> maps){
        return reserveService.addReserve(maps);
    }
}
