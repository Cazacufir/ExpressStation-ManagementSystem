package com.parcelhub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("orderlist")
public class OrderList implements Serializable {
    @TableId(type = IdType.AUTO)
    private int orderId;

    private String orderType;

    private String dateTime;

    private BigDecimal price;

    private Date orderTime;

    private int user_id;

    private int hub_id;

    private int parcel_id;

    private int del_flag;
}
