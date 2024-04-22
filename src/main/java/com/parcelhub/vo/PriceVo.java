package com.parcelhub.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceVo {
    private Date OrderDate;

    private BigDecimal TotalPrice;
}
