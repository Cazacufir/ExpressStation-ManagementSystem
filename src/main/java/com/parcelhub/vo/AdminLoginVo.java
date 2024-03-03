package com.parcelhub.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginVo {
    private String token;
    private AdminInfoVo adminInfoVo;
}
