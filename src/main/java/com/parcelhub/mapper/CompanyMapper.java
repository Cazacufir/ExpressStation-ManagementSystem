package com.parcelhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.parcelhub.entity.Company;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CompanyMapper extends BaseMapper<Company> {

    @Select("SELECT name from company " +
    "WHERE comId = #{comId}")
    String getName(int comId);
}
