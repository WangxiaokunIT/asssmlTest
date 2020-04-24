package com.xinshang.rest.modular.asmall.dto;

import lombok.Data;

/**
 * @Auther: wangxiaokun
 * @Date: 2019/11/18 11:20
 * @Description:
 */
@Data
public class OrderRefundPayRespDTO {

    /**
     * 支付状态成功 是：success 进行中：pending 失败：fail 退款到银行卡/微信/支付宝成功、失败 时会发订单结果通知商户。
     */
    private String payStatus;
    /**
     * 支付失败信息 否： 只有 payStatus 为 fail 时有效
     */
    private String payFailMessage;
    /**
     * 通商云订单编号 是
     */
    private String orderNo;
    /**
     * 商户订单号（支付订 单） 是
     */
    private String bizOrderNo;
    /**
     * 本次退款总金额 是
     */
    private Long amount;
    /**
     * 代金券退款金额 否
     */
    private Long couponAmount;
    /**
     * 手续费退款金额 否
     */
    private Long feeAmount;
    /**
     * 扩展信息 否
     */
    private String extendInfo;
}
