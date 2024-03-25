package com.parcelhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parcelhub.entity.Address;
import com.parcelhub.mapper.AddressMapper;
import com.parcelhub.service.AddressService;
import com.parcelhub.utils.AppHttpCodeEnum;
import com.parcelhub.utils.BeanCopyUtils;
import com.parcelhub.utils.Result;
import com.parcelhub.vo.AdressVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    @Autowired
    AddressMapper addressMapper;

    @Override
    public Result getAddressList(int user_id){
        LambdaQueryWrapper<Address> addressLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressLambdaQueryWrapper.eq(Address::getUser_id,user_id);
//                .groupBy(Address::getName,Address::getContact);
        List<Address> addresses = addressMapper.selectList(addressLambdaQueryWrapper);
        List<AdressVo> adressVos = BeanCopyUtils.copyBeanList(addresses, AdressVo.class);
        if(addresses.size() > 0){
            Map<String, List<String>> mergedAddresses = new HashMap<>();
            for (Address address : addresses) {
                String key = address.getName() + "_" + address.getContact();
                mergedAddresses.computeIfAbsent(key, k -> new ArrayList<>()).add(address.getAddress());
            }
            for(AdressVo address : adressVos){
                address.setAddress(mergedAddresses.get(address.getName() + "_" + address.getContact()));
            }
            Set set = new HashSet();
            set.addAll(adressVos);
            return Result.okResult(set);
        }
        else {
            return Result.errorResult(AppHttpCodeEnum.ADDRESS_NOT_FOUND);
        }
    }
}
