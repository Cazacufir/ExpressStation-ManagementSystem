package com.parcelhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parcelhub.dto.OrderParcelMerge;
import com.parcelhub.entity.*;
import com.parcelhub.mapper.*;
import com.parcelhub.service.ParcelService;
import com.parcelhub.utils.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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

    @Autowired
    DeliverHubMergeMapper deliverHubMergeMapper;

    @Autowired
    CompanyMapper companyMapper;

    @Autowired
    DeliverMapper deliverMapper;

    @Autowired
    HubMapper hubMapper;


    @Override
    public Result getReceiveParcel(int userId){
        User user = userMapper.selectById(userId);

        LambdaQueryWrapper<Parcel> parcelLambdaQueryWrapper = new LambdaQueryWrapper<>();
        parcelLambdaQueryWrapper.eq(Parcel::getReceiveName,user.getName())
                .eq(Parcel::getReceiveContact,user.getContact());
        List<Parcel> parcelList = parcelMapper.selectList(parcelLambdaQueryWrapper);

        if(parcelList.size() > 0){
            redisCache.setCacheList("receiveList:" + userId,parcelList);
            return Result.okResult(parcelList);
        }
        else {
            return Result.errorResult(AppHttpCodeEnum.PARCEL_NOT_FOUND);
        }
    }

    @Override
    public Result getExtraParcel(HttpServletRequest request){
        int userId = -1;
        try {
            userId = JwtUtil.getUserId(request);
        } catch (Exception e) {
            return Result.okResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        List<Parcel> parcelList = userParcelMergeMapper.getMoreParcel(userId);
        if(parcelList.size() > 0){
            return Result.okResult(parcelList);
        }
        return Result.errorResult(AppHttpCodeEnum.PARCEL_NOT_FOUND);
    }

    @Override
    public Result addExtraParcel(UserParcelMerge userParcelMerge){
        Parcel parcel = parcelMapper.selectById(userParcelMerge.getParcel_id());
        if(Objects.isNull(parcel)){
            return Result.errorResult(AppHttpCodeEnum.PARCEL_NOT_FOUND);
        }
        if(parcel.getBelonged_id() == userParcelMerge.getUser_id()){
            return Result.okResult2(parcel);
        }
        LambdaQueryWrapper<UserParcelMerge> userParcelMergeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userParcelMergeLambdaQueryWrapper.eq(UserParcelMerge::getUser_id,userParcelMerge.getUser_id())
                .eq(UserParcelMerge::getParcel_id,userParcelMerge.getParcel_id());
        UserParcelMerge userParcelMerge1 = userParcelMergeMapper.selectOne(userParcelMergeLambdaQueryWrapper);
        if(Objects.isNull(userParcelMerge1)){
            userParcelMergeMapper.insert(userParcelMerge);
            return Result.okResult(parcel);
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

    @Override
    public Result deleteExtraParcel(HttpServletRequest request,UserParcelMerge userParcelMerge){
        int userId = -1;
        try {
            userId = JwtUtil.getUserId(request);
        } catch (Exception e) {
            return Result.okResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        LambdaQueryWrapper<UserParcelMerge> userParcelMergeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userParcelMergeLambdaQueryWrapper.eq(UserParcelMerge::getUser_id,userId)
                .eq(UserParcelMerge::getParcel_id,userParcelMerge.getParcel_id());
        userParcelMergeMapper.delete(userParcelMergeLambdaQueryWrapper);
        return Result.okResult();
    }

    @Override
    public Result getRoute(int parcelId){
        QueryWrapper<Parcel> parcelQueryWrapper = new QueryWrapper<>();
        parcelQueryWrapper.select("route").eq("parcelId",parcelId);
        Parcel parcel = parcelMapper.selectOne(parcelQueryWrapper);
        if (Objects.isNull(parcel)){
            return Result.errorResult(AppHttpCodeEnum.PARCEL_NOT_FOUND);
        }
        return Result.okResult(parcel);
    }

    @Override
    public Result sendParcelByHub(Map<String,Integer> map){
        int parcelId = map.get("parcelId");
        int hub_id = map.get("hub_id");
        Parcel parcel = parcelMapper.selectById(parcelId);

        List<Deliver> deliverList =  redisCache.getCacheList("deliver_hub" + hub_id);
        if(deliverList.size() == 0){
            deliverList = deliverHubMergeMapper.getAllDeliverByHubId(hub_id);
            redisCache.setCacheList("deliver_hub" + hub_id,deliverList);
        }
        int index = (int) (Math.random()* deliverList.size());
        Deliver deliver = deliverList.get(index);
        deliver.setAffair("正在运送快递单号为 " + parcelId + " 的包裹");
        Company company = companyMapper.selectById(deliver.getCom_id());
        parcel.setCompany(company.getName());
        deliverMapper.updateById(deliver);

        Date now = new Date();
        parcel.setSendTime(now);
        parcel.setState("已揽收");
        String address = parcel.getSendAddress();
        List<Map<String, String>> result = AddressUtil.addressResolution(address);
        parcel.setCurrentDate(now);
        String city=  result.get(0).get("city");
        parcel.setCurrentCity(city);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(now);
        parcel.setRoute("已揽收" + "_" +  strDate + "_" + city);
        parcelMapper.updateById(parcel);

        return Result.okResult(parcel);
    }

    @Override
    public Result updateRoute(Parcel parcel){
        Parcel parcel1 = parcelMapper.selectById(parcel.getParcelId());
        String affair = null;
        if(parcel.getState().equals("派送中")){
            LambdaQueryWrapper<Company> companyLambdaQueryWrapper = new LambdaQueryWrapper<>();
            companyLambdaQueryWrapper.eq(Company::getName,parcel.getCompany());
            Company company = companyMapper.selectOne(companyLambdaQueryWrapper);
            String address = parcel.getReceiveAddress().split("_")[0];
            List<Hub> hubList = hubMapper.getReceiveHub(company.getComId(),address);
            System.out.println("+++++++++++");

            int hubIndex = (int)(Math.random()* hubList.size());
            Hub hub = hubList.get(hubIndex);
            System.out.println(hub);
            List<Deliver> deliverList =  redisCache.getCacheList("deliver_hub" + hub.getId());
            if(deliverList.size() == 0){
                deliverList = deliverHubMergeMapper.getAllDeliverByHubId(hub.getId());
                redisCache.setCacheList("deliver_hub" + hub.getId(),deliverList);
            }
            int index = (int) (Math.random()* deliverList.size());
            Deliver deliver = deliverList.get(index);
            deliver.setAffair("正在派送快递单号为 " + parcel1.getParcelId() + " 的包裹");
            affair = "快件正在派送中，有疑问请联系快递员【" + deliver.getName() + "，电话" + deliver.getContact() + "】";
            String newRoute = parcel1.getRoute() + "," + parcel.getRoute() + "_" + affair;
            parcel1.setDeliver_id(deliver.getDeliverId());
            parcel1.setHub_id(hub.getId());
            parcel1.setRoute(newRoute);
        }
        else {
            String newRoute = parcel1.getRoute() + "," + parcel.getRoute();
            parcel1.setRoute(newRoute);
        }

        parcel1.setCurrentCity(parcel.getCurrentCity());
        parcel1.setCurrentDate(parcel.getCurrentDate());

        parcel1.setState(parcel.getState());

        parcelMapper.updateById(parcel1);
        return Result.okResult(affair);
    }

    @Override
    public Result getSendingParcel(Integer pageNum,Integer pageSize,int hub_id){
        LambdaQueryWrapper<Parcel> parcelLambdaQueryWrapper = new LambdaQueryWrapper<>();
        parcelLambdaQueryWrapper.eq(Parcel::getHub_id,hub_id)
                .eq(Parcel::getState,"派送中");
        Page<Parcel> parcelPage = new Page<>(pageNum,pageSize);
        page(parcelPage,parcelLambdaQueryWrapper);
        int total = (int) parcelPage.getTotal();
        if (total == 0){
            return Result.errorResult(AppHttpCodeEnum.PARCEL_NOT_FOUND);
        }
        return Result.okResult(parcelPage);
    }
}
