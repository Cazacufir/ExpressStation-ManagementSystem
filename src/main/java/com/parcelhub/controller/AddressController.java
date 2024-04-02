package com.parcelhub.controller;

import com.parcelhub.entity.Address;
import com.parcelhub.service.AddressService;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class AddressController {
    @Autowired
    AddressService addressService;

    @GetMapping("/getAddressList")
    public Result getAddressList(@RequestParam int user_id){
        return addressService.getAddressList(user_id);
    }

    @PutMapping("/updateAddress")
    public Result updateAddress(@RequestBody Map<String,Object> map){
        return addressService.updateAddress(map);
    }

    @PostMapping("/addAddress")
    public Result addAddress(@RequestBody Map<String,Object> map){
        return addressService.addAddress(map);
    }

    @DeleteMapping("/deleteAddress")
    public Result deleteAddress(@RequestBody List<Address> addresses){
        return addressService.deleteAddress(addresses);
    }
}
