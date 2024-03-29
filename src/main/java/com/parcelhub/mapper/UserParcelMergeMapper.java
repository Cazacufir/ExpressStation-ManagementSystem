package com.parcelhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parcelhub.entity.Parcel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserParcelMergeMapper extends BaseMapper<UserParcelMergeMapper> {

    @Select("SELECT p.* " +
    "FROM parcel p " +
    "INNER JOIN user_parcel_merge upm ON upm.parcel_id = p.parcelId " +
    "WHERE upm.user_id = #{userId}")
    List<Parcel> getMoreParcel(int userId);
}
