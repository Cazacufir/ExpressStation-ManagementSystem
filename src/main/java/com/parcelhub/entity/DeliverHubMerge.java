package com.parcelhub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("deliver_hub_merge")
public class DeliverHubMerge implements Serializable {
    @TableId(type = IdType.AUTO)
    private int mapId;

    private int deliver_id;

    private int hub_id;
}
