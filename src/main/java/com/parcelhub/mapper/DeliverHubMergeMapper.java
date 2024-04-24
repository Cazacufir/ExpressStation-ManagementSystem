package com.parcelhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parcelhub.entity.Deliver;
import com.parcelhub.entity.DeliverHubMerge;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DeliverHubMergeMapper extends BaseMapper<DeliverHubMerge> {

    @Select("SELECT d.*, dhm.* from deliver_hub_merge dhm " +
    "INNER JOIN deliver d ON dhm.deliver_id = d.deliverId " +
    "INNER JOIN hub h ON dhm.hub_id = h.id " +
    "WHERE dhm.hub_id = #{hubId}")
    List<Deliver> getAllDeliverByHubId(int hubId);

    @Select("Select d.*,dhm.* FROM deliver d " +
            "INNER JOIN deliver_hub_merge dhm ON dhm.deliver_id = d.deliverId " +
            "WHERE d.com_id = #{com_id} AND dhm.hub_id = #{hub_id} ")
    List<Deliver> deleteDeliverByEndWithHub(int hub_id,int com_id);
}
