package com.parcelhub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Carrier {

    @TableId(type = IdType.AUTO)
    private int carrierId;

    private int num;

    private int flats;

    private int maxCount;

    private int currentCount;

    private int hub_id;
}
