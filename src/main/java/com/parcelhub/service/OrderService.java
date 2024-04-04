package com.parcelhub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.parcelhub.dto.OrderParcelMerge;
import com.parcelhub.entity.OrderList;
import com.parcelhub.utils.Result;

public interface OrderService extends IService<OrderList> {
    Result getSendList(int user_id);

    Result addSendList(OrderParcelMerge orderParcelMerge);

    Result updateSendList(OrderParcelMerge orderParcelMerge);

    Result cancelSendList(int orderId);
}