package com.parcelhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parcelhub.entity.Parcel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface ParcelMapper extends BaseMapper<Parcel> {
    @Select("SELECT p.*,r.* " +
            "FROM parcel p " +
            "LEFT JOIN reserve r ON p.parcelId = r.parcel_id " +
            "WHERE p.receiveName = #{receiveName} AND p.receiveContact = #{receiveContact} AND p.state = '待取件'")
    List<Parcel> getReceivedParcel(String receiveName,String receiveContact);
}