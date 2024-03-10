package com.parcelhub.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Staff implements Serializable {

    @TableId(type = IdType.AUTO)
    private int staffId;

    private String name;

    private int age;

    private String sex;

    private String work;

    private String contact;

    private Date joinDate;

    private int hub_id;
}
