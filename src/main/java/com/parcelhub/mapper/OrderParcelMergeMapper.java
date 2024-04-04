package com.parcelhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parcelhub.dto.OrderParcelMerge;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderParcelMergeMapper extends BaseMapper<OrderParcelMerge> {

    @Select("SELECT p.* , o.* " +
            "FROM parcel p " +
            "INNER JOIN orderlist o ON o.parcel_id = p.parcelId " +
            "WHERE o.user_id = #{userId} AND o.orderTime < #{now} AND o.orderTime > #{days}")
    List<OrderParcelMerge> getRecentSend(int userId, LocalDateTime now, LocalDateTime days);
}
