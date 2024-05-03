package com.parcelhub.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("com_hub_merge")
public class ComHubMerge implements Serializable {

    @TableId(type = IdType.AUTO)
    private int mapId;

    private int com_id;

    private int hub_id;

    private Date co_date;
}
