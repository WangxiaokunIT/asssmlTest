package com.xinshang.modular.biz.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangjiajia
 */
@Data
public class AllinPaySyncOrderStateDTO implements Serializable {


    private static final long serialVersionUID = 7333940629648161827L;

    /**
     * 通商云订单号
     */
    private String orderNo;

    /**
     * 商户订单号（支付订单）
     */
    private String bizOrderNo;

    /**
     * 订单状态
     */
    private Long orderStatus;

    /**
     * 订单金额
     */
    private Long amount;

    /**
     * 商户系统用户标识，商户
     * 系统中唯一编号。
     * 付款人
     */
    private String buyerBizUserId;

}
