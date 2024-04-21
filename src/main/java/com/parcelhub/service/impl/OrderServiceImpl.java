package com.parcelhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parcelhub.dto.OrderParcelMerge;
import com.parcelhub.dto.PagesDto;
import com.parcelhub.entity.*;
import com.parcelhub.mapper.OrderMapper;
import com.parcelhub.mapper.ParcelMapper;
import com.parcelhub.mapper.StaffMapper;
import com.parcelhub.mapper.UserMapper;
import com.parcelhub.service.OrderService;
import com.parcelhub.utils.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderList> implements OrderService {
    @Autowired
    OrderMapper orderMapper;

    @Autowired
    ParcelMapper parcelMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    StaffMapper staffMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public Result getSendList(int user_id){
        List<OrderParcelMerge> orderParcelMerges = orderMapper.getSendParcel(user_id);
        redisCache.setCacheList("sendList:"+user_id,orderParcelMerges);
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

        if(orderParcelMerge.getUser_id()!=0){
            parcel.setBelonged_id(orderParcelMerge.getUser_id());
            orderList.setUser_id(orderParcelMerge.getUser_id());
        }
        else {
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getName,orderParcelMerge.getSendName())
                    .eq(User::getContact,orderParcelMerge.getSendContact());
            User user = userMapper.selectOne(userLambdaQueryWrapper);
            if (!Objects.isNull(user)){
                parcel.setBelonged_id(user.getUserId());
                orderList.setUser_id(user.getUserId());
            }
        }

        parcelMapper.insert(parcel);
        int parcelId = parcel.getParcelId();

        orderList.setOrderType(orderParcelMerge.getOrderType());
        if(!Objects.isNull(orderParcelMerge.getDateTime())){
            orderList.setDateTime(orderParcelMerge.getDateTime());

            LambdaQueryWrapper<Staff> staffLambdaQueryWrapper = new LambdaQueryWrapper<>();
            staffLambdaQueryWrapper.eq(Staff::getHub_id,orderParcelMerge.getHub_id())
                    .eq(Staff::getWork,"配送员");
            List<Staff> staffList = staffMapper.selectList(staffLambdaQueryWrapper);
            int index = (int) (Math.random()* staffList.size());
            Staff staff = staffList.get(index);
            String newAffair = "";
            if(Objects.isNull(staff.getAffair())){
                newAffair ="需于" + orderParcelMerge.getDateTime() + "揽收快件" + parcelId;
            }
            else{
                newAffair = staff.getAffair() + "," + "需于" + orderParcelMerge.getDateTime() + "揽收快件" + parcelId;
            }
            staff.setAffair(newAffair);
            staffMapper.updateById(staff);
        }
        orderList.setPrice(orderParcelMerge.getPrice());

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
        Parcel parcel = parcelMapper.selectById(orderList.getParcel_id());
        if (!parcel.getState().equals("等待揽收")){
            return Result.errorResult(AppHttpCodeEnum.PARCEL_OUT);
        }
        parcelMapper.deleteById(orderList.getParcel_id());

        orderList.setDel_flag(1);
        orderMapper.updateById(orderList);
        return Result.okResult();
    }

    @Override
    public Result getSearchSendList(HttpServletRequest request,Integer parcelId,String word){
        int userId = -1;
        try {
            userId = JwtUtil.getUserId(request);
        } catch (Exception e) {
            return Result.okResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        if(parcelId == 0){
            List<OrderParcelMerge> orderParcelMergeList = redisCache.getCacheList("sendList:"+userId);
            List<OrderParcelMerge> orderParcelMerges = new ArrayList<>();
            for(OrderParcelMerge orderParcelMerge : orderParcelMergeList){
                if(orderParcelMerge.getSendName().contains(word) || orderParcelMerge.getSendAddress().contains(word) ||
                orderParcelMerge.getReceiveName().contains(word) || orderParcelMerge.getReceiveAddress().contains(word)){
                    orderParcelMerges.add(orderParcelMerge);
                }
            }
            Set<OrderParcelMerge> orderParcelMergeSet = new HashSet<>(orderParcelMerges);
            return Result.okResult(orderParcelMergeSet);
        }
        else {
            List<OrderParcelMerge> orderParcelMerge = orderMapper.getSendParcelByParcelId(parcelId);
            if(orderParcelMerge.size() == 0){
                return Result.errorResult(AppHttpCodeEnum.PARCEL_NOT_FOUND);
            }
            return Result.okResult(orderParcelMerge);
        }
    }

    @Override
    public Result getSendListByHub(Integer pageNum, Integer pageSize, int hub_id){
        List<OrderParcelMerge> orderParcelMergeList = orderMapper.getSendParcelByHub(hub_id);
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, orderParcelMergeList.size());
        List<OrderParcelMerge> orderParcelMergeList1 = orderParcelMergeList.subList(startIndex,endIndex);
        PagesDto<OrderParcelMerge> orderParcelMergePagesDto = PageUtils.listToPageDTO(orderParcelMergeList,pageNum,pageSize);
        orderParcelMergePagesDto.setDataList(orderParcelMergeList1);
        return Result.okResult(orderParcelMergePagesDto);
    }

}
