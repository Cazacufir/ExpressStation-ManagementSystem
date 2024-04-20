package com.parcelhub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reserve implements Serializable {
    @TableId(type = IdType.AUTO)
    private int reserveId;

    private String dateTime;

    private int parcel_id;

    private int staff_id;
}
