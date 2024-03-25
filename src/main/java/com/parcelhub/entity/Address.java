package com.parcelhub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.lang.reflect.Array;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Serializable {
    @TableId(type = IdType.AUTO)
    private String addressId;

    private String name;

    private String contact;

    private String address;

    private int user_id;

    @TableField(exist = false)
    private Array addressList;
}
