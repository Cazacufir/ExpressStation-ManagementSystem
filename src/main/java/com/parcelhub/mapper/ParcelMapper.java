package com.parcelhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parcelhub.entity.Parcel;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ParcelMapper extends BaseMapper<Parcel> {
}
