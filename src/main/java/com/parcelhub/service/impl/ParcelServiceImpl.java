package com.parcelhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parcelhub.dto.OrderParcelMerge;
import com.parcelhub.dto.PagesDto;
import com.parcelhub.entity.*;
import com.parcelhub.mapper.*;
import com.parcelhub.service.ParcelService;
import com.parcelhub.utils.*;
import com.parcelhub.vo.ReceiveParcelVo;
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

    @Autowired
    CarrierMapper carrierMapper;

    @Autowired
    CarrierFlatMapper carrierFlatMapper;

    @Autowired
    ReserveMapper reserveMapper;

    @Autowired
    StaffMapper staffMapper;


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

            int hubIndex = (int)(Math.random()* hubList.size());
            Hub hub = hubList.get(hubIndex);
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

    @Override
    public Result receiveParcelByHub(Parcel parcel){
        LambdaQueryWrapper<Carrier> carrierLambdaQueryWrapper = new LambdaQueryWrapper<>();
        carrierLambdaQueryWrapper.eq(Carrier::getHub_id,parcel.getHub_id());
        List<Carrier> carrierList = carrierMapper.selectList(carrierLambdaQueryWrapper);
        Carrier carrier1 = null;
        for(Carrier carrier : carrierList){
            if (carrier.getCurrentCount() < (carrier.getFlats() * carrier.getMaxCount())){
                carrier1 = carrier;
                break;
            }
        }
        if(Objects.isNull(carrier1)){
            return Result.errorResult(AppHttpCodeEnum.HUB_FULL);
        }

        CarrierFlat carrierFlat = new CarrierFlat();
        double currentCount =  carrier1.getCurrentCount();
        double maxCount =  carrier1.getMaxCount();
        int flatNum = (int) Math.ceil(currentCount / maxCount);
        if(flatNum == 0){
            flatNum = 1;
        }

        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            // 生成随机数，范围为 0-9
            int randomNumber = random.nextInt(10);
            result.append(randomNumber);
        }
        String code = carrier1.getNum() + "-" + flatNum + "-" + result;

        carrierFlat.setFlat(flatNum);
        carrierFlat.setParcelCode(code);
        carrierFlat.setParcel_id(parcel.getParcelId());
        carrierFlat.setCarrier_id(carrier1.getCarrierId());
        carrierFlatMapper.insert(carrierFlat);
        carrier1.setCurrentCount(carrier1.getCurrentCount() + 1);
        carrierMapper.updateById(carrier1);

        Deliver deliver = deliverMapper.selectById(parcel.getDeliver_id());
        deliver.setAffair("快递单号" + parcel.getParcelId() + "的包裹已派送完毕");
        deliverMapper.updateById(deliver);

        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(now);
        parcel.setArrivalTime(strDate);
        parcel.setCode(code);
        parcel.setState("待取件");

        Hub hub = hubMapper.selectById(parcel.getHub_id());
        String hubStr = "已入库" + "_" + now + "您的快递已派送至<" + hub.getName() + "代收点;自提点联系方式:" + hub.getContact()
                + ">,请凭取件码" + code + "及时到代收点领取";
        String newRoute = parcel.getRoute() + "," + hubStr;
        parcel.setRoute(newRoute);
        parcelMapper.updateById(parcel);
        return Result.okResult();
    }

    @Override
    public Result getAllParcelByHub(Integer pageNum,Integer pageSize,int hub_id){
        LambdaQueryWrapper<Parcel> parcelLambdaQueryWrapper = new LambdaQueryWrapper<>();
        parcelLambdaQueryWrapper.eq(Parcel::getHub_id,hub_id);
        Page<Parcel> parcelPage = new Page<>(pageNum,pageSize);
        page(parcelPage,parcelLambdaQueryWrapper);
        int total = (int) parcelPage.getTotal();
        if(total == 0){
            return Result.errorResult(AppHttpCodeEnum.PARCEL_NOT_FOUND);
        }
        return Result.okResult(parcelPage);
    }

    @Override
    public Result getReceivedParcel(Parcel parcel){
//        LambdaQueryWrapper<Parcel> parcelLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        parcelLambdaQueryWrapper.eq(Parcel::getReceiveName,parcel.getReceiveName())
//                .eq(Parcel::getReceiveContact,parcel.getReceiveContact())
//                .eq(Parcel::getState,"待取件");
        List<Parcel> parcelList = parcelMapper.getReceivedParcel(parcel.getReceiveName(),parcel.getReceiveContact());
        if(parcelList.size() == 0){
            return Result.errorResult(AppHttpCodeEnum.PARCEL_NOT_FOUND);
        }
        List<ReceiveParcelVo> receiveParcelVos = BeanCopyUtils.copyBeanList(parcelList, ReceiveParcelVo.class);
        Map<String,List<Parcel>> mergedParcel = new HashMap<>();
        for (Parcel parcel1 : parcelList){
            String[] route = parcel1.getRoute().split(",");
            String hubName = route[route.length - 2];
            int startIndex = hubName.indexOf("<") + 1;
            int endIndex = hubName.indexOf("代");
            String key = hubName.substring(startIndex,endIndex);
//            String key = parcel1.getReceiveName() + "_" + parcel1.getReceiveContact();
            mergedParcel.computeIfAbsent(key, k -> new ArrayList<>()).add(parcel1);
        }
        for (ReceiveParcelVo receiveParcelVo : receiveParcelVos){
            String[] route = receiveParcelVo.getRoute().split(",");
            String hubName = route[route.length - 2];
            int startIndex = hubName.indexOf("<") + 1;
            int endIndex = hubName.indexOf("代");
            String key = hubName.substring(startIndex,endIndex);
            receiveParcelVo.setParcel(mergedParcel.get(key));
            receiveParcelVo.setHubName(key);
        }
        Set<ReceiveParcelVo> receiveParcelVoSet = new HashSet<>(receiveParcelVos);
        return Result.okResult(receiveParcelVoSet);
    }

    @Override
    public Result receiveSingleParcel(int parcelId){
        Parcel parcel = parcelMapper.selectById(parcelId);
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(now);

        LambdaQueryWrapper<CarrierFlat> carrierFlatLambdaQueryWrapper = new LambdaQueryWrapper<>();
        carrierFlatLambdaQueryWrapper.eq(CarrierFlat::getParcel_id,parcelId);
        CarrierFlat carrierFlat = carrierFlatMapper.selectOne(carrierFlatLambdaQueryWrapper);
        int carrierId = carrierFlat.getCarrier_id();
        carrierFlatMapper.deleteById(carrierFlat);

        Carrier carrier = carrierMapper.selectById(carrierId);
        carrier.setCurrentCount(carrier.getCurrentCount() - 1);
        carrierMapper.updateById(carrier);

        LambdaQueryWrapper<Reserve> reserveLambdaQueryWrapper = new LambdaQueryWrapper<>();
        reserveLambdaQueryWrapper.eq(Reserve::getParcel_id,parcelId);
        Reserve reserve = reserveMapper.selectOne(reserveLambdaQueryWrapper);
        if(!Objects.isNull(reserve)){
            Staff staff = staffMapper.selectById(reserve.getStaff_id());
            String[] staffAffair = staff.getAffair().split(",");
            Parcel parcel1 = parcelMapper.selectById(parcelId);
            for(int i = 0; i < staffAffair.length; i++){
                if(staffAffair[i].contains(parcel1.getCode())){
                    staffAffair[i] = "";
                }
            }
            String result = String.join(",", staffAffair);
            staff.setAffair(result);
            strDate = reserve.getDateTime();
//            staffMapper.updateById(staff);
        }

        parcel.setReceiveTime(strDate);
        parcel.setState("已签收");
        String str = "已签收" + "_" + now + "已签收，收件人已取走";
        String newRoute = parcel.getRoute() + "," + str;
        parcel.setRoute(newRoute);
        parcelMapper.updateById(parcel);
        return Result.okResult();
    }

    @Override
    public Result receiveParcelByUser(List<Parcel> parcels){
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(now);

        for(Parcel parcel : parcels){
            Parcel parcel1 = parcelMapper.selectById(parcel.getParcelId());
            parcel1.setReceiveTime(strDate);
            parcel1.setState("已签收");
            String str = "已签收" + "_" + now + "已签收，收件人已取走";
            String newRoute = parcel.getRoute() + "," + str;
            parcel1.setRoute(newRoute);
            parcelMapper.updateById(parcel1);

            LambdaQueryWrapper<CarrierFlat> carrierFlatLambdaQueryWrapper = new LambdaQueryWrapper<>();
            carrierFlatLambdaQueryWrapper.eq(CarrierFlat::getParcel_id,parcel.getParcelId());
            CarrierFlat carrierFlat = carrierFlatMapper.selectOne(carrierFlatLambdaQueryWrapper);
            int carrierId = carrierFlat.getCarrier_id();
            carrierFlatMapper.deleteById(carrierFlat);

            LambdaQueryWrapper<Reserve> reserveLambdaQueryWrapper = new LambdaQueryWrapper<>();
            reserveLambdaQueryWrapper.eq(Reserve::getParcel_id,parcel.getParcelId());
            Reserve reserve = reserveMapper.selectOne(reserveLambdaQueryWrapper);
            if(!Objects.isNull(reserve)){
                Staff staff = staffMapper.selectById(reserve.getStaff_id());
                String[] staffAffair = staff.getAffair().split(",");
                Parcel parcel2 = parcelMapper.selectById(parcel.getParcelId());
                for(int i = 0; i < staffAffair.length; i++){
                    if(staffAffair[i].contains(parcel2.getCode())){
                        staffAffair[i] = "";
                    }
                }
                String result = String.join(",", staffAffair);
                staff.setAffair(result);
                staffMapper.updateById(staff);
            }

            Carrier carrier = carrierMapper.selectById(carrierId);
            carrier.setCurrentCount(carrier.getCurrentCount() - 1);
            carrierMapper.updateById(carrier);
        }
        return Result.okResult();
    }

    @Override
    public Result getAllReserveParcel(Integer pageNum,Integer pageSize,int hub_id){
        List<Parcel> parcelList = parcelMapper.getReserveParcel(hub_id);
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, parcelList.size());
        List<Parcel> parcelList1 = parcelList.subList(startIndex,endIndex);
        PagesDto<Parcel> parcelPagesDto = PageUtils.listToPageDTO(parcelList,pageNum,pageSize);
        parcelPagesDto.setDataList(parcelList1);
        if(parcelPagesDto.getTotalElements() == 0){
            return Result.errorResult(AppHttpCodeEnum.PARCEL_NOT_FOUND);
        }
        return Result.okResult(parcelPagesDto);
    }
}
