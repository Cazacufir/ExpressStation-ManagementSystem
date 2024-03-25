package com.parcelhub.controller;

import com.parcelhub.entity.Address;
import com.parcelhub.service.AddressService;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddressController {
    @Autowired
    AddressService addressService;

    @GetMapping("/getAddressList")
    public Result getAddressList(int user_id){
        return addressService.getAddressList(user_id);
    }

    @PutMapping("/updateAddress")
    public Result updateAddress(Address address){
        return addressService.updateAddress(address);
    }
}
