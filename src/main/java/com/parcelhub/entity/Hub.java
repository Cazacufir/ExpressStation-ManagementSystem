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
public class Hub implements Serializable {

    @TableId(type = IdType.AUTO)
    private int id;

    private String name;

    private String contact;

    private String address;

    private String open_time;

    private String close_time;

    private Date setupDate;
}
