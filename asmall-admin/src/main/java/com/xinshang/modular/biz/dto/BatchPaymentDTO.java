package com.xinshang.modular.biz.dto;

import lombok.Data;

import java.util.List;

/**
 * 批量托管代付列表
 * @author lyk
 */
@Data
public class BatchPaymentDTO {
    /**
     * 商户订单号（支付订单）
     */
    private String bizOrderNo;

    /**
     * 商户系统用户标识，商户系统中唯一编号。
     * 托管代收订单中指定的收款方。
     */
    private String bizUserId;

    /**
     * 收款方的账户集编号
     */
    private String accountSetNo;

    /**
     * 后台通知地址
     */
    private String backUrl;

    /**
     * 金额，单位：分
     */
    private Long amount;

    /**
     * 手续费，单位：分。
     * 内扣，如果不存在，则填 0。
     * 如 amount 为 100，fee 为 2，实际到账金额为 98。
     */
    private Long fee;

    /**
     * 源托管代收订单付款信息
     * 最多支持 100 个
     */
    private List<PaymentOrderDTO> collectPayList;
}
