package com.xinshang.rest.modular.asmall.model;


import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 预计收益
 * </p>
 *
 * @author zhoushuai
 * @since 2019-10-23
 */
@Data
public class JoinInner {

    private static final long serialVersionUID = 1L;


    /**
     * 投资金额
     */
    private Long investmentAmount;
    /**
     * 还款方式
     */
    private Integer repaymentMethod;
    /**
     * 招募周期
     */
    private Integer recruitmentCycle;
    /**
     * 单位（天、月）
     */
    private Integer unit;
    /**
     * 权益率
     */
    private BigDecimal equityRate;
    /**
     * 权益金额
     */
    private BigDecimal inner;
    /**
     * 积分
     */
    private Integer points;


}
