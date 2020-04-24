package com.xinshang.modular.biz.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 首页数据统计
 * @author lyk
 *
 */
@Data
public class HomeVO {

    private String time;

    private BigDecimal num1;

    private BigDecimal num2;

    private BigDecimal num3;
}
