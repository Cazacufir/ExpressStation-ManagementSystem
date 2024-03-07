package com.parcelhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parcelhub.entity.ComHubMerge;
import com.parcelhub.entity.Company;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ComHubMergeMapper extends BaseMapper<ComHubMerge> {
    @Select("SELECT c.* FROM com_hub_merge chm " +
            "INNER JOIN company c ON chm.com_id = c.comId " +
            "WHERE chm.hub_id = #{hubId}")
    List<Company> getCompanyByHubId(int hubId);
}
