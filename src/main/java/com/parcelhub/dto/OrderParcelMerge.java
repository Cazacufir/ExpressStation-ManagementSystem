package com.parcelhub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderParcelMerge implements Serializable {

    private int orderId;

    private String orderType;

    private String dateTime;

    private BigDecimal price;

    private Date orderTime;

    private int user_id;

    private int hub_id;

    private int parcel_id;

    private int del_flag;


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

    private String logo;

    private String state;

    private String route;

    private Date currentDate;

    private String currentCity;

    private String arrivalTime;

    private String receiveTime;

    private int belonged_id;

    private int deliver_id;

    private String code;
}
