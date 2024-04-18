package com.parcelhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parcelhub.entity.CarrierFlat;
import com.parcelhub.entity.Parcel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CarrierFlatMapper extends BaseMapper<CarrierFlat> {

    @Select("SELECT p.* " +
            "FROM parcel p " +
            "INNER JOIN carrier_flat cf ON cf.parcel_id = p.parcelId " +
            "WHERE cf.carrier_id = #{carrierId} AND p.state = '待取件' ")
    List<Parcel> getCarrierParcelList(int carrierId);
}
