package com.parcelhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.parcelhub.entity.Delay;
import com.parcelhub.entity.Reserve;
import com.parcelhub.entity.Staff;
import com.parcelhub.mapper.DelayMapper;
import com.parcelhub.mapper.ReserveMapper;
import com.parcelhub.mapper.StaffMapper;
import com.parcelhub.service.ReserveService;
import com.parcelhub.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ReserveServiceImpl extends ServiceImpl<ReserveMapper, Reserve> implements ReserveService {

    @Autowired
    ReserveMapper reserveMapper;

    @Autowired
    StaffMapper staffMapper;

    @Autowired
    DelayMapper delayMapper;

    @Override
    public Result addReserve(List<Map<String,Object>> maps){

        for(Map<String,Object> map : maps){
            Reserve reserve = new Reserve();
            int parcel_id = (int) map.get("parcelId");
            int hub_id =(int) map.get("hub_id");

            LambdaQueryWrapper<Staff> staffLambdaQueryWrapper = new LambdaQueryWrapper<>();
            staffLambdaQueryWrapper.eq(Staff::getHub_id,hub_id)
                    .eq(Staff::getWork,"配送员");
            List<Staff> staffList = staffMapper.selectList(staffLambdaQueryWrapper);

            int index = (int) (Math.random()* staffList.size());
            Staff staff = staffList.get(index);
            String newAffair = "";
            if(Objects.isNull(staff.getAffair())){
                newAffair ="需于" + map.get("dateTime") + "派送快件" + map.get("code");
            }
            else{
                newAffair = staff.getAffair() + "," + "需派送快件" + map.get("code");
            }
            staff.setAffair(newAffair);
            staffMapper.updateById(staff);

            reserve.setDateTime((String) map.get("dateTime"));
            reserve.setStaff_id(staff.getStaffId());
            reserve.setParcel_id(parcel_id);
            save(reserve);

            LambdaQueryWrapper<Delay> delayLambdaQueryWrapper = new LambdaQueryWrapper<>();
            delayLambdaQueryWrapper.eq(Delay::getParcel_id,parcel_id);
            delayMapper.delete(delayLambdaQueryWrapper);
        }

        return Result.okResult();
    }
}
