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
import com.parcelhub.vo.CountParcelVo;
import com.parcelhub.vo.ReceiveParcelVo;
import com.parcelhub.vo.UserCountsVo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    @Autowired
    DelayMapper delayMapper;

    @Autowired
    OrderMapper orderMapper;


    @Override
    public Result getReceiveParcel(int userId){
        User user = userMapper.selectById(userId);

        LambdaQueryWrapper<Parcel> parcelLambdaQueryWrapper = new LambdaQueryWrapper<>();
        parcelLambdaQueryWrapper.eq(Parcel::getReceiveName,user.getName())
                .eq(Parcel::getReceiveContact,user.getContact())
                .eq(Parcel::getIf_del_user,0);
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
    public Result sendParcelByHub(Map<String,Object> map){
        int parcelId =(int) map.get("parcelId");
        int hub_id =(int) map.get("hub_id");
        Parcel parcel = parcelMapper.selectById(parcelId);
//        Date now = new Date();
        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
        parcel.setLogo(company.getLogo());
        deliverMapper.updateById(deliver);

        LambdaQueryWrapper<OrderList> orderListLambdaQueryWrapper = new LambdaQueryWrapper<>();
        orderListLambdaQueryWrapper.eq(OrderList::getParcel_id,parcelId);
        OrderList orderList = orderMapper.selectOne(orderListLambdaQueryWrapper);

        if(orderList.getOrderType().equals("上门取件")){
            LambdaQueryWrapper<Staff> staffLambdaQueryWrapper = new LambdaQueryWrapper<>();
            staffLambdaQueryWrapper.eq(Staff::getHub_id,hub_id)
                    .eq(Staff::getWork,"配送员");
            List<Staff> staffList = staffMapper.selectList(staffLambdaQueryWrapper);
            Staff staff = null;
            String[] affairs = null;
            for(Staff staff1 : staffList){
                if (!Objects.isNull(staff1.getAffair())){
                    if (staff1.getAffair().contains("" + parcelId)){
                        staff = staff1;
                        affairs = staff1.getAffair().split(",");
                        for(int i = 0; i < affairs.length; i++){
                            if(affairs[i].contains("" + parcelId)){
                                affairs[i] = "";
                                break;
                            }
                        }
                        String result = String.join(",", affairs);
                        result = result.substring(1,result.length() - 1);
                        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                        System.out.println(result);
                        staff.setAffair(result);
                        staffMapper.updateById(staff);
                        break;
                    }
                }
            }

            String datePart = orderList.getDateTime().split(" ")[0];
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try{
                Calendar parsedDate = Calendar.getInstance();
                parsedDate.setTime(format.parse(datePart));
                now.set(Calendar.YEAR, parsedDate.get(Calendar.YEAR));
                now.set(Calendar.MONTH, parsedDate.get(Calendar.MONTH));
                now.set(Calendar.DAY_OF_MONTH, parsedDate.get(Calendar.DAY_OF_MONTH));
            }
            catch (java.text.ParseException e){
                System.out.println(e);
            }
        }
        Date dateDate = now.getTime();

        parcel.setSendTime(dateDate);
        parcel.setState("已揽收");
        String address = parcel.getSendAddress();
        List<Map<String, String>> result = AddressUtil.addressResolution(address);
        parcel.setCurrentDate(dateDate);
        String city = result.get(0).get("city");
        parcel.setCurrentCity(city);
        String strDate = sdf.format(dateDate);
//        if(!Objects.isNull(parcel.getCurrentDate())){
//            parcel.setRoute("已揽收" + "_" +  parcel.getCurrentDate() + "_" + city);
//        }
//        else{
//            parcel.setRoute("已揽收" + "_" +  strDate + "_" + city);
//        }
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
    public Result searchSendingParcel(int hub_id,int parcelId,String word){
        if (parcelId == 0){
            LambdaQueryWrapper<Parcel> parcelLambdaQueryWrapper = new LambdaQueryWrapper<>();
            parcelLambdaQueryWrapper.eq(Parcel::getHub_id, hub_id)
                    .eq(Parcel::getState, "派送中")
                    .and(wrapper -> wrapper
                            .like(Parcel::getSendName, word)
                            .or().like(Parcel::getSendAddress, word)
                            .or().like(Parcel::getReceiveName, word)
                            .or().like(Parcel::getReceiveAddress, word)
                            .or().like(Parcel::getType, word)
                            .or().like(Parcel::getCompany, word)
                    );
            List<Parcel> parcelList = parcelMapper.selectList(parcelLambdaQueryWrapper);
            if (parcelList.size() == 0){
                return Result.errorResult(AppHttpCodeEnum.PARCEL_NOT_FOUND);
            }
            return Result.okResult(parcelList);
        }
        else {
            LambdaQueryWrapper<Parcel> parcelLambdaQueryWrapper = new LambdaQueryWrapper<>();
            parcelLambdaQueryWrapper.eq(Parcel::getHub_id,hub_id)
                    .eq(Parcel::getParcelId,parcelId);
            List<Parcel> parcelList = parcelMapper.selectList(parcelLambdaQueryWrapper);
            if (parcelList.size() == 0){
                return Result.errorResult(AppHttpCodeEnum.PARCEL_NOT_FOUND);
            }
            return Result.okResult(parcelList);
        }
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
        String hubStr = "待取件" + "_" + strDate + "_" + parcel.getCurrentCity() + "_" + "您的快递已派送至<" + hub.getName() + "代收点;自提点联系方式:" + hub.getContact()
                + ">，请凭取件码" + code + "及时到代收点领取";
        String newRoute = parcel.getRoute() + "," + hubStr;
        parcel.setRoute(newRoute);
        parcelMapper.updateById(parcel);
        return Result.okResult();
    }

    @Override
    public Result getAllParcelByHub(Integer pageNum,Integer pageSize,int hub_id){
        LambdaQueryWrapper<Parcel> parcelLambdaQueryWrapper = new LambdaQueryWrapper<>();
        parcelLambdaQueryWrapper.eq(Parcel::getHub_id,hub_id)
                .eq(Parcel::getState,"待取件")
                .eq(Parcel::getIf_del,"0");
        Page<Parcel> parcelPage = new Page<>(pageNum,pageSize);
        page(parcelPage,parcelLambdaQueryWrapper);
        int total = (int) parcelPage.getTotal();
        if(total == 0){
            return Result.errorResult(AppHttpCodeEnum.PARCEL_NOT_FOUND);
        }
        return Result.okResult(parcelPage);
    }

    @Override
    public Result getGotParcelByHub(Integer pageNum,Integer pageSize,int hub_id){
        LambdaQueryWrapper<Parcel> parcelLambdaQueryWrapper = new LambdaQueryWrapper<>();
        parcelLambdaQueryWrapper.eq(Parcel::getHub_id,hub_id)
                .eq(Parcel::getState,"已签收")
                .eq(Parcel::getIf_del,"0");
        Page<Parcel> parcelPage = new Page<>(pageNum,pageSize);
        page(parcelPage,parcelLambdaQueryWrapper);
        int total = (int) parcelPage.getTotal();
        if(total == 0){
            return Result.errorResult(AppHttpCodeEnum.PARCEL_NOT_FOUND);
        }
        return Result.okResult(parcelPage);
    }

    @Override
    public Result searchAllParcelByHub(int hub_id,int parcelId,String word){
        if (parcelId == 0){
            LambdaQueryWrapper<Parcel> parcelLambdaQueryWrapper = new LambdaQueryWrapper<>();
            parcelLambdaQueryWrapper.eq(Parcel::getHub_id, hub_id)
                    .eq(Parcel::getState, "待取件")
                    .and(wrapper -> wrapper
                            .like(Parcel::getSendName, word)
                            .or().like(Parcel::getSendAddress, word)
                            .or().like(Parcel::getReceiveName, word)
                            .or().like(Parcel::getReceiveAddress, word)
                            .or().like(Parcel::getType, word)
                            .or().like(Parcel::getCode, word)
                            .or().like(Parcel::getState, word)
                            .or().like(Parcel::getCompany, word)
                    );
            List<Parcel> parcelList = parcelMapper.selectList(parcelLambdaQueryWrapper);
            if (parcelList.size() == 0){
                return Result.errorResult(AppHttpCodeEnum.PARCEL_NOT_FOUND);
            }
            return Result.okResult(parcelList);
        }
        else{
            LambdaQueryWrapper<Parcel> parcelLambdaQueryWrapper = new LambdaQueryWrapper<>();
            parcelLambdaQueryWrapper.eq(Parcel::getHub_id,hub_id)
                    .eq(Parcel::getParcelId,parcelId);
            List<Parcel> parcelList = parcelMapper.selectList(parcelLambdaQueryWrapper);
            if (parcelList.size() == 0){
                return Result.errorResult(AppHttpCodeEnum.PARCEL_NOT_FOUND);
            }
            return Result.okResult(parcelList);
        }
    }

    @Override
    public Result searchGotParcelByHub(int hub_id,int parcelId,String word){
        if (parcelId == 0){
            LambdaQueryWrapper<Parcel> parcelLambdaQueryWrapper = new LambdaQueryWrapper<>();
            parcelLambdaQueryWrapper.eq(Parcel::getHub_id, hub_id)
                    .eq(Parcel::getState, "已签收")
                    .and(wrapper -> wrapper
                            .like(Parcel::getSendName, word)
                            .or().like(Parcel::getSendAddress, word)
                            .or().like(Parcel::getReceiveName, word)
                            .or().like(Parcel::getReceiveAddress, word)
                            .or().like(Parcel::getType, word)
                            .or().like(Parcel::getCode, word)
                            .or().like(Parcel::getState, word)
                            .or().like(Parcel::getCompany, word)
                    );
            List<Parcel> parcelList = parcelMapper.selectList(parcelLambdaQueryWrapper);
            if (parcelList.size() == 0){
                return Result.errorResult(AppHttpCodeEnum.PARCEL_NOT_FOUND);
            }
            return Result.okResult(parcelList);
        }
        else{
            LambdaQueryWrapper<Parcel> parcelLambdaQueryWrapper = new LambdaQueryWrapper<>();
            parcelLambdaQueryWrapper.eq(Parcel::getHub_id,hub_id)
                    .eq(Parcel::getParcelId,parcelId);
            List<Parcel> parcelList = parcelMapper.selectList(parcelLambdaQueryWrapper);
            if (parcelList.size() == 0){
                return Result.errorResult(AppHttpCodeEnum.PARCEL_NOT_FOUND);
            }
            return Result.okResult(parcelList);
        }
    }

    @Override
    public Result getReceivedParcel(Parcel parcel){
        List<Parcel> parcelList = parcelMapper.getReceivedParcel(parcel.getReceiveName(),parcel.getReceiveContact());
        if(parcelList.size() == 0){
            return Result.errorResult(AppHttpCodeEnum.PARCEL_NOT_FOUND);
        }
        List<ReceiveParcelVo> receiveParcelVos = BeanCopyUtils.copyBeanList(parcelList, ReceiveParcelVo.class);
        receiveParcelVos = new ArrayList<>(new HashSet<>(receiveParcelVos));
        Map<String,List<Parcel>> mergedParcel = new HashMap<>();
        for (Parcel parcel1 : parcelList){
            String[] route = parcel1.getRoute().split(",");
            String hubName = route[route.length - 1];
            int startIndex = hubName.indexOf("<") + 1;
            int endIndex = hubName.indexOf("代");
            String key = hubName.substring(startIndex,endIndex);
//            String key = parcel1.getReceiveName() + "_" + parcel1.getReceiveContact();
            mergedParcel.computeIfAbsent(key, k -> new ArrayList<>()).add(parcel1);
        }
//        Set<String> mergeSet = new HashSet<>(mergedParcel.keySet());
        for (ReceiveParcelVo receiveParcelVo : receiveParcelVos){
            String[] route = receiveParcelVo.getRoute().split(",");
            String hubName = route[route.length - 1];
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
    public Result getReceivedParcelByUser(Parcel parcel){
        LambdaQueryWrapper<Parcel> parcelLambdaQueryWrapper = new LambdaQueryWrapper<>();
        parcelLambdaQueryWrapper.eq(Parcel::getReceiveName,parcel.getReceiveName())
                .eq(Parcel::getReceiveContact,parcel.getReceiveContact())
                .eq(Parcel::getState,"待取件");
        List<Parcel> parcelList = parcelMapper.selectList(parcelLambdaQueryWrapper);
        return Result.okResult(parcelList);
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
                    break;
                }
            }
            String result = String.join(",", staffAffair);
            staff.setAffair(result);
            strDate = reserve.getDateTime();
            staffMapper.updateById(staff);
        }

        LambdaQueryWrapper<Delay> delayLambdaQueryWrapper = new LambdaQueryWrapper<>();
        delayLambdaQueryWrapper.eq(Delay::getParcel_id,parcelId);
        Delay delay = delayMapper.selectOne(delayLambdaQueryWrapper);
        if(!Objects.isNull(delay)){
            delayMapper.deleteById(delay);
        }

        parcel.setReceiveTime(strDate);
        parcel.setState("已签收");
        String str = "已签收" + "_" + strDate + "_" + "已签收，收件人已取走";
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
                        break;
                    }
                }
                String result = String.join(",", staffAffair);
                staff.setAffair(result);
                staffMapper.updateById(staff);
            }

            LambdaQueryWrapper<Delay> delayLambdaQueryWrapper = new LambdaQueryWrapper<>();
            delayLambdaQueryWrapper.eq(Delay::getParcel_id,parcel.getParcelId());
            Delay delay = delayMapper.selectOne(delayLambdaQueryWrapper);
            if(!Objects.isNull(delay)){
                delayMapper.deleteById(delay);
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

    @Override
    public Result returnParcel(int parcelId){
        Parcel parcel = parcelMapper.selectById(parcelId);
        String sendName = parcel.getSendName();
        String sendContact = parcel.getSendContact();
        String sendAddress = parcel.getSendAddress();
        String receiveName = parcel.getReceiveName();
        String receiveContact = parcel.getReceiveContact();
        String receiveAddress = parcel.getReceiveAddress();
        parcel.setSendName(receiveName);
        parcel.setSendContact(receiveContact);
        parcel.setSendAddress(receiveAddress);
        parcel.setReceiveName(sendName);
        parcel.setReceiveContact(sendContact);
        parcel.setReceiveAddress(sendAddress);

        List<Deliver> deliverList = deliverHubMergeMapper.getAllDeliverByHubId(parcel.getHub_id());
        int index = (int) (Math.random()* deliverList.size());
        Deliver deliver = deliverList.get(index);
        deliver.setAffair("正在运送快递单号为 " + parcelId + " 的包裹");
        Company company = companyMapper.selectById(deliver.getCom_id());
        parcel.setCompany(company.getName());
        deliverMapper.updateById(deliver);

        Date now = new Date();
        parcel.setSendTime(now);
        parcel.setState("已揽收");
        List<Map<String, String>> result = AddressUtil.addressResolution(sendAddress);
        parcel.setCurrentDate(now);
        String city = result.get(0).get("city");
        parcel.setCurrentCity(city);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(now);
        parcel.setRoute("已揽收" + "_" +  strDate + "_" + city);

        LambdaQueryWrapper<CarrierFlat> carrierFlatLambdaQueryWrapper = new LambdaQueryWrapper<>();
        carrierFlatLambdaQueryWrapper.eq(CarrierFlat::getParcel_id,parcelId);
        CarrierFlat carrierFlat = carrierFlatMapper.selectOne(carrierFlatLambdaQueryWrapper);
        int carrierId = carrierFlat.getCarrier_id();
        carrierFlatMapper.deleteById(carrierFlat);

        Carrier carrier = carrierMapper.selectById(carrierId);
        carrier.setCurrentCount(carrier.getCurrentCount() - 1);
        carrierMapper.updateById(carrier);

        parcelMapper.updateById(parcel);
        return Result.okResult();
    }

    @Override
    public Result countParcel(int hub_id){
        CountParcelVo countParcelVo = new CountParcelVo();
        LocalDate today = LocalDate.now();

        LambdaQueryWrapper<Parcel> parcelLambdaQueryWrapper = new LambdaQueryWrapper<>();
        parcelLambdaQueryWrapper.eq(Parcel::getHub_id,hub_id)
                .eq(Parcel::getState,"待取件");
        countParcelVo.setCurrentParcel(parcelMapper.selectCount(parcelLambdaQueryWrapper));

        LambdaQueryWrapper<Parcel> parcelLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        parcelLambdaQueryWrapper1.eq(Parcel::getHub_id,hub_id)
                .eq(Parcel::getState,"派送中");
        countParcelVo.setPendingReceiveParcel(parcelMapper.selectCount(parcelLambdaQueryWrapper1));

//        LambdaQueryWrapper<Parcel> parcelLambdaQueryWrapper2 = new LambdaQueryWrapper<>();
//        parcelLambdaQueryWrapper2.eq(Parcel::getHub_id,hub_id)
//                .like(Parcel::getState,"等待揽收");
//        countParcelVo.setPendingSendParcel(parcelMapper.selectCount(parcelLambdaQueryWrapper2));
        countParcelVo.setPendingSendParcel(parcelMapper.getPendingSendParcel(hub_id));

//        LambdaQueryWrapper<Parcel> parcelLambdaQueryWrapper3 = new LambdaQueryWrapper<>();
//        parcelLambdaQueryWrapper3.eq(Parcel::getHub_id,hub_id)
//                .between(Parcel::getSendTime,today.atStartOfDay(),today.atTime(LocalTime.MAX));
//        countParcelVo.setSendParcel(parcelMapper.selectCount(parcelLambdaQueryWrapper3));
        countParcelVo.setSendParcel(parcelMapper.getSendParcelOneDay(hub_id,today.atStartOfDay(),today.atTime(LocalTime.MAX)));

        LambdaQueryWrapper<Parcel> parcelLambdaQueryWrapper4 = new LambdaQueryWrapper<>();
        parcelLambdaQueryWrapper4.eq(Parcel::getHub_id,hub_id)
                .between(Parcel::getArrivalTime,today.atStartOfDay(),today.atTime(LocalTime.MAX));
        countParcelVo.setReceiveParcel(parcelMapper.selectCount(parcelLambdaQueryWrapper4));

        LambdaQueryWrapper<Parcel> parcelLambdaQueryWrapper5 = new LambdaQueryWrapper<>();
        parcelLambdaQueryWrapper5.eq(Parcel::getHub_id,hub_id)
                .eq(Parcel::getState,"待取件")
                .le(Parcel::getArrivalTime,today);
        countParcelVo.setOverdue(parcelMapper.selectCount(parcelLambdaQueryWrapper5));

        return Result.okResult(countParcelVo);
    }

    @Override
    public Result getKindsParcel(int hub_id){
        Map<String,Long> map = new HashMap<>();
        Long ReserveCount = parcelMapper.getReserve(hub_id);
        map.put("预约取件",ReserveCount);
        Long DelayCount = parcelMapper.getDelay(hub_id);
        map.put("延迟取件",DelayCount);

//        LambdaQueryWrapper<Parcel> parcelLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        parcelLambdaQueryWrapper.eq(Parcel::getHub_id,hub_id)
//                .eq(Parcel::getState,"等待揽收");
        Long SendParcel = parcelMapper.getPendingSendParcel(hub_id);
        map.put("待出库",SendParcel);

        LambdaQueryWrapper<Parcel> parcelLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        parcelLambdaQueryWrapper1.eq(Parcel::getHub_id,hub_id)
                .eq(Parcel::getState,"派送中");
        Long ReceiveParcel = parcelMapper.selectCount(parcelLambdaQueryWrapper1);
        map.put("待入库",ReceiveParcel);

        LambdaQueryWrapper<Parcel> parcelLambdaQueryWrapper2 = new LambdaQueryWrapper<>();
        parcelLambdaQueryWrapper2.eq(Parcel::getHub_id,hub_id)
                .eq(Parcel::getState,"待取件");
        Long NormalParcel = parcelMapper.selectCount(parcelLambdaQueryWrapper2);
        if (NormalParcel<0){
            NormalParcel = 0L;
        }
        map.put("正常待取",NormalParcel);

        return Result.okResult(map);
    }

    @Override
    public Result countUser(int hub_id){
        LocalDate today = LocalDate.now();
        List<UserCountsVo> userCountsVoList = parcelMapper.getSendUser(hub_id,today.minusDays(6),today.plusDays(1));
        List<UserCountsVo> userCountsVoList1 = parcelMapper.getReceiveUser(hub_id,today.minusDays(6),today.plusDays(1));
        Map<String,List<UserCountsVo>> map = new HashMap<>();
        map.put("send",userCountsVoList);
        map.put("receive",userCountsVoList1);
        return Result.okResult(map);
    }

    @Override
    public Result countSRParcel(int hub_id){
        LocalDate today = LocalDate.now();
        List<UserCountsVo> userCountsVoList = parcelMapper.getSendUser(hub_id,today.minusDays(6),today.plusDays(1));
        List<UserCountsVo> userCountsVoList1 = parcelMapper.getReceiveParcel(hub_id,today.minusDays(6),today.plusDays(1));
        Map<String,List<UserCountsVo>> map = new HashMap<>();
        map.put("sendParcel",userCountsVoList);
        map.put("receiveParcel",userCountsVoList1);
        return Result.okResult(map);
    }

    @Override
    public Result deleteParcelRecord(Parcel parcel){
        parcel.setIf_del(1);
        parcelMapper.updateById(parcel);
        return Result.okResult();
    }

    @Override
    public Result selectAllinHub(int hub_id,String start,String end){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date startDate = dateFormat.parse(start);
            Date endDate = dateFormat.parse(end);
            List<Parcel> parcelList = parcelMapper.selectAllWaiting(hub_id,startDate,endDate);
            if (parcelList.size() == 0){
                return Result.errorResult(AppHttpCodeEnum.PARCEL_NOT_FOUND);
            }

            return Result.okResult(parcelList);
        }
        catch (java.text.ParseException e){
            System.out.println(e);
        }
            return Result.okResult();
    }

    @Override
    public Result selectAllGot(int hub_id,String start,String end){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date startDate = dateFormat.parse(start);
            Date endDate = dateFormat.parse(end);
            List<Parcel> parcelList = parcelMapper.selectAllGot(hub_id,startDate,endDate);
            if (parcelList.size() == 0){
                return Result.errorResult(AppHttpCodeEnum.PARCEL_NOT_FOUND);
            }

            return Result.okResult(parcelList);
        }
        catch (java.text.ParseException e){
            System.out.println(e);
        }
        return Result.okResult();
    }

    @Override
    public Result outParcelList(List<Map<String,Object>> mapList){
        for (Map<String,Object> map : mapList){
            sendParcelByHub(map);
        }
        return Result.okResult();
    }

    @Override
    public Result inParcelList(List<Parcel> parcelList){
        for (Parcel parcel : parcelList){
             receiveParcelByHub(parcel);
        }
        return Result.okResult();
    }
}
