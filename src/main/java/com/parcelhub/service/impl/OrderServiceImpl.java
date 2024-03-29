package com.parcelhub.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parcelhub.dto.OrderParcelMerge;
import com.parcelhub.entity.OrderList;
import com.parcelhub.entity.Parcel;
import com.parcelhub.mapper.OrderMapper;
import com.parcelhub.mapper.ParcelMapper;
import com.parcelhub.service.OrderService;
import com.parcelhub.utils.AppHttpCodeEnum;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderList> implements OrderService {
    @Autowired
    OrderMapper orderMapper;

    @Autowired
    ParcelMapper parcelMapper;

    @Override
    public Result getSendList(int user_id){
        List<OrderParcelMerge> orderParcelMerges = orderMapper.getSendParcel(user_id);
        if (orderParcelMerges.size() > 0){
            return Result.okResult(orderParcelMerges);
        }
        return Result.errorResult(AppHttpCodeEnum.ORDER_NOT_FOUND);
    }

    @Override
    public Result addSendList(OrderParcelMerge orderParcelMerge){
        OrderList orderList = new OrderList();
        Parcel parcel = new Parcel();

        parcel.setSendName(orderParcelMerge.getSendName());
        parcel.setSendAddress(orderParcelMerge.getSendAddress());
        parcel.setSendContact(orderParcelMerge.getSendContact());
        parcel.setReceiveName(orderParcelMerge.getReceiveName());
        parcel.setReceiveAddress(orderParcelMerge.getReceiveAddress());
        parcel.setReceiveContact(orderParcelMerge.getReceiveContact());
        parcel.setType(orderParcelMerge.getType());
        parcel.setWeight(orderParcelMerge.getWeight());
        parcelMapper.insert(parcel);
        int parcelId = parcel.getParcelId();

        orderList.setOrderType(orderParcelMerge.getOrderType());
        orderList.setDateTime(orderParcelMerge.getDateTime());
        orderList.setPrice(orderParcelMerge.getPrice());
        orderList.setUser_id(orderParcelMerge.getUser_id());
        orderList.setHub_id(orderParcelMerge.getHub_id());
        orderList.setParcel_id(parcelId);
        orderMapper.insert(orderList);

        return Result.okResult();
    }

    @Override
    public Result updateSendList(OrderParcelMerge orderParcelMerge){
        OrderList orderList = orderMapper.selectById(orderParcelMerge.getOrderId());
        Parcel parcel = parcelMapper.selectById(orderParcelMerge.getParcelId());

        orderList.setOrderType(orderParcelMerge.getOrderType());
        orderList.setOrderTime(orderParcelMerge.getOrderTime());
        orderList.setHub_id(orderParcelMerge.getHub_id());
        orderList.setPrice(orderParcelMerge.getPrice());
        orderMapper.updateById(orderList);

        parcel.setReceiveName(orderParcelMerge.getReceiveName());
        parcel.setReceiveAddress(orderParcelMerge.getReceiveAddress());
        parcel.setReceiveContact(orderParcelMerge.getReceiveContact());
        parcel.setSendName(orderParcelMerge.getSendName());
        parcel.setSendAddress(orderParcelMerge.getSendAddress());
        parcel.setSendContact(orderParcelMerge.getSendContact());
        parcel.setType(orderParcelMerge.getType());
        parcel.setWeight(orderParcelMerge.getWeight());
        parcelMapper.updateById(parcel);

        return Result.okResult();
    }

    @Override
    public Result cancelSendList(int orderId){
        OrderList orderList = orderMapper.selectById(orderId);
        parcelMapper.deleteById(orderList.getParcel_id());

        orderList.setDel_flag(1);
        orderMapper.updateById(orderList);
        return Result.okResult();
    }
}
