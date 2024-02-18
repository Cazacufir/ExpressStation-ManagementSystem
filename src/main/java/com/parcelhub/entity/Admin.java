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
@TableName("admin")
public class Admin implements Serializable {

    @TableId(type = IdType.AUTO)
    private int adminId;

    private String password;

    private String contact;

    private int role;

    private int hub_id;

    private int staff_id;

}
