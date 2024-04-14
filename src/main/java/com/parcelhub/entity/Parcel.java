package com.parcelhub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Parcel implements Serializable {
    @TableId(type = IdType.AUTO)
    private int parcelId;

    private String sendName;

    private String sendAddress;

    private String sendContact;

    private Date sendTime;

    private String receiveName;

    private String receiveAddress;

    private String receiveContact;

    private String type;

    private int weight;

    private String company;

    private String state;

    private String route;

    private Date currentDate;

    private String currentCity;

    private String arrivalTime;

    private Date receiveTime;

    private int belonged_id;

    private int hub_id;

    private int deliver_id;

    private String code;
}
