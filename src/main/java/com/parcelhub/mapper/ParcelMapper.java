package com.parcelhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parcelhub.entity.Parcel;
import com.parcelhub.vo.UserCountsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Mapper
public interface ParcelMapper extends BaseMapper<Parcel> {
    @Select("SELECT p.*,r.*,d.* " +
            "FROM parcel p " +
            "LEFT JOIN reserve r ON p.parcelId = r.parcel_id " +
            "LEFT JOIN delay d ON p.parcelId = d.parcel_id " +
            "WHERE p.receiveName = #{receiveName} AND p.receiveContact = #{receiveContact} AND p.state = '待取件'")
    List<Parcel> getReceivedParcel(String receiveName,String receiveContact);

    @Select("SELECT p.*,r.*,s.* " +
            "FROM parcel p " +
            "INNER JOIN reserve r ON p.parcelId = r.parcel_id " +
            "INNER JOIN staff s ON s.staffId = r.staff_id " +
            "WHERE p.hub_id = #{hub_id}")
    List<Parcel> getReserveParcel(int hub_id);

    @Select("SELECT COUNT(parcelId) " +
            "FROM parcel p " +
            "INNER JOIN reserve r ON r.parcel_id = p.parcelId " +
            "WHERE p.hub_id = #{hub_id} ")
    Long getReserve(int hub_id);

    @Select("SELECT COUNT(parcelId) " +
            "FROM parcel p " +
            "INNER JOIN delay d ON d.parcel_id = p.parcelId " +
            "WHERE p.hub_id = #{hub_id} AND p.state = '待取件'")
    Long getDelay(int hub_id);

    @Select("SELECT o.orderTime AS OrderDate, COUNT(o.orderId) AS counts " +
            "FROM orderlist o " +
            "WHERE o.orderTime > #{start} AND o.orderTime < #{end} AND o.hub_id = #{hub_id} " +
            "AND o.del_flag = 0 ")
    List<UserCountsVo> getSendUser(int hub_id, LocalDate start, LocalDateTime end);

    @Select("SELECT p.receiveTime AS OrderDate, COUNT(parcelId) AS counts " +
            "FROM parcel p " +
            "WHERE p.receiveTime > #{start} AND p.receiveTime < #{end} AND p.hub_id = #{hub_id} " +
            "AND p.state = '已签收' ")
    List<UserCountsVo> getReceiveUser(int hub_id, LocalDate start, LocalDateTime end);

    @Select("SELECT p.receiveTime AS OrderDate, COUNT(parcelId) AS counts " +
            "FROM parcel p " +
            "WHERE p.receiveTime > #{start} AND p.receiveTime < #{end} AND p.hub_id = #{hub_id} " +
            "AND p.state = '已签收' " +
            "OR p.state = '待取件' ")
    List<UserCountsVo> getReceiveParcel(int hub_id,LocalDate start, LocalDateTime end);

    @Select("SELECT p.* " +
            "From parcel p " +
            "WHERE p.hub_id = #{hub_id} AND p.state = '待取件' AND p.arrivalTime > #{start} AND p.arrivalTime < #{end}")
    List<Parcel> selectAllWaiting(int hub_id, Date start, Date end);

    @Select("SELECT p.* " +
            "From parcel p " +
            "WHERE p.hub_id = #{hub_id} AND p.state = '已签收' AND p.receiveTime > #{start} AND p.receiveTime < #{end}")
    List<Parcel> selectAllGot(int hub_id, Date start, Date end);

    @Select("SELECT COUNT(parcelId) " +
            "FROM parcel p " +
            "INNER JOIN orderlist o ON p.parcelId = o.parcel_id " +
            "WHERE o.hub_id = #{hub_id} AND o.del_flag = 0 AND p.state LIKE '%等待揽收%' ;")
    Long getPendingSendParcel(int hub_id);

    @Select("SELECT COUNT(parcelId) " +
            "FROM parcel p " +
            "INNER JOIN orderlist o ON p.parcelId = o.parcel_id " +
            "WHERE o.hub_id = #{hub_id} AND p.sendTime > #{start} AND p.sendTime < #{end}")
    Long getSendParcelOneDay(int hub_id, LocalDateTime start, LocalDateTime end);
}
