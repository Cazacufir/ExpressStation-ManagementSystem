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
            return Result.okResult(adressVos);
        }
        else {
            return Result.errorResult(AppHttpCodeEnum.ADDRESS_NOT_FOUND);
        }
    }

    @Override
    public Result updateAddress(Map<String,Object> map){
        LambdaQueryWrapper<Address> addressLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressLambdaQueryWrapper.eq(Address::getUser_id,map.get("user_id"))
                .eq(Address::getName,map.get("oldName"))
                .eq(Address::getContact,map.get("oldContact"))
                .eq(Address::getAddress,map.get("oldAddress"));
        Address address = addressMapper.selectOne(addressLambdaQueryWrapper);
        String name = (String) map.get("newName");
        String contact = (String) map.get("newContact");
        String newAddress = (String) map.get("newAddress");
        address.setName(name);
        address.setContact(contact);
        address.setAddress(newAddress);
        addressMapper.updateById(address);
        return Result.okResult();
    }

    @Override
    public Result addAddress(Map<String,Object> map){
        Address address = new Address();
        String name = (String) map.get("name");
        String address1 = (String) map.get("address");
        String contact = (String) map.get("contact");
        int user_id = (int) map.get("user_id");

        address.setName(name);
        address.setContact(contact);
        address.setAddress(address1);
        address.setUser_id(user_id);
        save(address);
        return Result.okResult();
    }

    @Override
    public Result deleteAddress(List<Address> addresses){
        addresses.forEach(item -> {
            LambdaQueryWrapper<Address> addressLambdaQueryWrapper = new LambdaQueryWrapper<>();
            addressLambdaQueryWrapper.eq(Address::getName,item.getName())
                            .eq(Address::getContact,item.getContact())
                            .eq(Address::getAddress,item.getAddress());
            Address address = addressMapper.selectOne(addressLambdaQueryWrapper);
            addressMapper.deleteById(address);
        });
        return Result.okResult();
    }
}
