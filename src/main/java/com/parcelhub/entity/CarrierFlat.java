package com.parcelhub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("carrier_flat")
public class CarrierFlat {

    @TableId(type = IdType.AUTO)
    private int mapId;

    private int flat;

    private String parcelCode;

    private int parcel_id;

    private int carrier_id;

    private int del_flag;
}
