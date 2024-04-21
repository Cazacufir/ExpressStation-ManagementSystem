package com.parcelhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parcelhub.entity.Parcel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DelayMapper extends BaseMapper<com.parcelhub.entity.Delay> {

    @Select("SELECT p.*,d.* " +
            "FROM parcel p " +
            "INNER JOIN delay d ON p.parcelId = d.parcel_id " +
            "WHERE p.hub_id = #{hub_id} AND p.state = '待取件'")
    List<Parcel> getDelayParcelByHub(int hub_id);
}
