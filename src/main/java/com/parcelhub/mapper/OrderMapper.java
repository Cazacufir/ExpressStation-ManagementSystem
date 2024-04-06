package com.parcelhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parcelhub.dto.OrderParcelMerge;
import com.parcelhub.entity.OrderList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<OrderList> {

    @Select("SELECT o.*, p.* " +
            "FROM orderlist o " +
            "INNER JOIN parcel p ON o.parcel_id = p.parcelId " +
            "WHERE o.user_id = #{user_id} AND o.del_flag = 0;")
    List<OrderParcelMerge> getSendParcel(int user_id);

    @Select("SELECT o.*, p.* " +
            "FROM orderlist o " +
            "INNER JOIN parcel p ON o.parcel_id = p.parcelId " +
            "WHERE p.parcelId = #{parcelId} AND o.del_flag = 0;")
    List<OrderParcelMerge> getSendParcelByParcelId(int parcelId);
}
