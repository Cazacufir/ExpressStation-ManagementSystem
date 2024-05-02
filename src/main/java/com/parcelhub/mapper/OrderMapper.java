package com.parcelhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parcelhub.dto.OrderParcelMerge;
import com.parcelhub.entity.OrderList;
import com.parcelhub.vo.PriceVo;
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
            "WHERE p.parcelId = #{parcelId} AND o.user_id = #{userId} AND o.del_flag = 0;")
    List<OrderParcelMerge> getSendParcelByParcelId(int parcelId,int userId);

    @Select("SELECT o.*, p.* " +
            "FROM orderlist o " +
            "INNER JOIN parcel p ON o.parcel_id = p.parcelId " +
            "WHERE p.parcelId = #{parcelId} AND p.state = '等待揽收' AND o.hub_id = #{hub_id} AND o.del_flag = 0;")
    List<OrderParcelMerge> getSendParcelByParcelIdByHub(int parcelId,int hub_id);

    @Select("SELECT o.*, p.* " +
            "FROM orderlist o " +
            "INNER JOIN parcel p ON o.parcel_id = p.parcelId " +
            "WHERE o.hub_id = #{hub_id} AND o.del_flag = 0 AND p.state LIKE '%等待揽收%' ;")
    List<OrderParcelMerge> getSendParcelByHub(int hub_id);

    @Select("SELECT DATE(orderTime) AS OrderDate, SUM(price) AS TotalPrice " +
            "FROM orderlist o " +
            "WHERE OrderTime BETWEEN DATE_SUB(CURDATE(), INTERVAL 10 DAY) AND CURDATE() AND o.hub_id = #{hub_id} " +
            "AND o.del_flag = 0 " +
            "GROUP BY OrderDate")
    List<PriceVo> getPriceByHub(int hub_id);

    @Select("SELECT o.*, p.* " +
            "FROM orderlist o " +
            "INNER JOIN parcel p ON o.parcel_id = p.parcelId " +
            "WHERE o.hub_id = #{hub_id} AND o.del_flag = 0 AND p.state NOT LIKE '%等待揽收%' ;")
    List<OrderParcelMerge> getOutedParcelByHub(int hub_id);

    @Select("SELECT o.*, p.* " +
            "FROM orderlist o " +
            "INNER JOIN parcel p ON o.parcel_id = p.parcelId " +
            "WHERE p.parcelId = #{parcelId} AND p.state NOT LIKE '等待揽收' AND o.hub_id = #{hub_id} AND o.del_flag = 0;")
    List<OrderParcelMerge> getOutedParcelByParcelIdByHub(int parcelId,int hub_id);
}
