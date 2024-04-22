package com.parcelhub.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountParcelVo {
    private Long currentParcel;

    private Long pendingSendParcel;

    private Long pendingReceiveParcel;

    private Long sendParcel;

    private Long receiveParcel;

    private Long overdue;
}
