package com.parcelhub.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminInfoVo {
    private int adminId;

    private String password;

    private String contact;

    private int role;

    private int hub_id;
}
