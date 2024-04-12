package com.parcelhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parcelhub.entity.Hub;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HubMapper extends BaseMapper<Hub> {
    @Select("SELECT h.* " +
            "FROM hub h " +
            "INNER JOIN com_hub_merge chm ON chm.hub_id = h.id " +
            "WHERE chm.com_id = #{comId} AND h.address LIKE '%${address}%' ")
    List<Hub> getReceiveHub(int comId,String address);
}
