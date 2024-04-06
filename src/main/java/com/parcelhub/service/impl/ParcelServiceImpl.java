package com.parcelhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parcelhub.dto.OrderParcelMerge;
import com.parcelhub.entity.Parcel;
import com.parcelhub.entity.User;
import com.parcelhub.entity.UserParcelMerge;
import com.parcelhub.mapper.OrderParcelMergeMapper;
import com.parcelhub.mapper.ParcelMapper;
import com.parcelhub.mapper.UserMapper;
import com.parcelhub.mapper.UserParcelMergeMapper;
import com.parcelhub.service.ParcelService;
import com.parcelhub.utils.AppHttpCodeEnum;
import com.parcelhub.utils.JwtUtil;
import com.parcelhub.utils.RedisCache;
import com.parcelhub.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ParcelServiceImpl extends ServiceImpl<ParcelMapper, Parcel> implements ParcelService {

    @Autowired
    ParcelMapper parcelMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserParcelMergeMapper userParcelMergeMapper;

    @Autowired
    OrderParcelMergeMapper orderParcelMergeMapper;

    @Autowired
    private RedisCache redisCache;


    @Override
    public Result getReceiveParcel(int userId){
        User user = userMapper.selectById(userId);

        LambdaQueryWrapper<Parcel> parcelLambdaQueryWrapper = new LambdaQueryWrapper<>();
        parcelLambdaQueryWrapper.eq(Parcel::getReceiveName,user.getName())
                .eq(Parcel::getReceiveContact,user.getContact());
        List<Parcel> parcelList = parcelMapper.selectList(parcelLambdaQueryWrapper);
        redisCache.setCacheList("receiveList:" + userId,parcelList);
        if(parcelList.size() > 0){
            return Result.okResult(parcelList);
        }
        else {
            return Result.errorResult(AppHttpCodeEnum.PARCEL_NOT_FOUND);
        }
    }

    @Override
    public Result getExtraParcel(int userId){
        List<Parcel> parcelList = userParcelMergeMapper.getMoreParcel(userId);
        if(parcelList.size() > 0){
            return Result.okResult(parcelList);
        }
        return Result.errorResult(AppHttpCodeEnum.PARCEL_NOT_FOUND);
    }

    @Override
    public Result addExtraParcel(UserParcelMerge userParcelMerge){
        LambdaQueryWrapper<UserParcelMerge> userParcelMergeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userParcelMergeLambdaQueryWrapper.eq(UserParcelMerge::getUser_id,userParcelMerge.getUser_id())
                .eq(UserParcelMerge::getParcel_id,userParcelMerge.getParcel_id());
        UserParcelMerge userParcelMerge1 = userParcelMergeMapper.selectOne(userParcelMergeLambdaQueryWrapper);
        if(Objects.isNull(userParcelMerge1)){
            userParcelMergeMapper.insert(userParcelMerge);
            return Result.okResult();
        }
        else {
            return Result.errorResult(AppHttpCodeEnum.PARCEL_EXIST);
        }
    }

    @Override
    public Result getRecentSendParcel(int userId){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime days = now.plusDays(-7);
        LambdaQueryWrapper<OrderParcelMerge> orderParcelMergeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        orderParcelMergeLambdaQueryWrapper.eq(OrderParcelMerge::getUser_id,userId)
                .between(OrderParcelMerge::getOrderTime,days,now);
        List<OrderParcelMerge> orderParcelMergeList = orderParcelMergeMapper.getRecentSend(userId,now,days);

        if (orderParcelMergeList.size() > 0){
            return Result.okResult(orderParcelMergeList);
        }
        else {
            return Result.errorResult(AppHttpCodeEnum.PARCEL_NOT_FOUND);
        }
    }

    @Override
    public Result getRecentReceiveParcel(Parcel parcel){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime days = now.minusDays(7);
        LambdaQueryWrapper<Parcel> parcelLambdaQueryWrapper = new LambdaQueryWrapper<>();
        parcelLambdaQueryWrapper.eq(Parcel::getReceiveName,parcel.getReceiveName())
                .eq(Parcel::getReceiveContact,parcel.getReceiveContact())
                .between(Parcel::getSendTime,days,now);
        List<Parcel> parcelList = parcelMapper.selectList(parcelLambdaQueryWrapper);

        if (parcelList.size() > 0){
            return Result.okResult(parcelList);
        }
        else {
            return Result.errorResult(AppHttpCodeEnum.PARCEL_NOT_FOUND);
        }
    }

    @Override
    public Result getSearchParcel(int parcelId){
        Parcel parcel = parcelMapper.selectById(parcelId);
        if(Objects.isNull(parcel)){
            return Result.errorResult(AppHttpCodeEnum.PARCEL_NOT_FOUND);
        }
        return Result.okResult(parcel);
    }

    @Override
    public Result getSearchReceiveList(HttpServletRequest request, Integer parcelId, String word){
        int userId = -1;
        try {
            userId = JwtUtil.getUserId(request);
        } catch (Exception e) {
            return Result.okResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        if(parcelId == 0){
            List<Parcel> parcelList = redisCache.getCacheList("receiveList:" + userId);
            List<Parcel> parcels = new ArrayList<>();
            for(Parcel parcel : parcelList){
                if(parcel.getSendName().contains(word)||parcel.getSendAddress().contains(word)||
                parcel.getReceiveName().contains(word)||parcel.getReceiveAddress().contains(word)){
                    parcels.add(parcel);
                }
            }
            Set<Parcel> parcelSet = new HashSet<>(parcels);
            return Result.okResult(parcelSet);
        }
        else {
            LambdaQueryWrapper<Parcel> parcelLambdaQueryWrapper = new LambdaQueryWrapper<>();
            parcelLambdaQueryWrapper.eq(Parcel::getParcelId,parcelId);
            List<Parcel> parcelList = parcelMapper.selectList(parcelLambdaQueryWrapper);
            if(parcelList.size() == 0){
                return Result.errorResult(AppHttpCodeEnum.PARCEL_NOT_FOUND);
            }
            return Result.okResult(parcelList);
        }
    }
}
