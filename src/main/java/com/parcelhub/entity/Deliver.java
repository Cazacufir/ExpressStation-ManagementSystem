package com.parcelhub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Deliver implements Serializable {
    @TableId(type = IdType.AUTO)
    private int deliverId;

    private String name;

    private int age;

    private String sex;

    private String contact;

    private String affair;

    private int com_id;

    @TableField(exist = false)
    private String comName;

}
