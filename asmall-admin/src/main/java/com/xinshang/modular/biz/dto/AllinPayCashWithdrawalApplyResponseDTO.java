package com.xinshang.modular.biz.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zhangjiajia
 * 提现申请对象
 */
@Data
public class AllinPayCashWithdrawalApplyResponseDTO implements Serializable {

    private static final long serialVersionUID = 1169532454054357089L;
    /**
     * 商户系统用户标识
     */
    private String bizUserId;
    /**
     * 支付状态
     * 成功：success
     * 进行中：pending
     * 失败：fail
     */
    private String payStatus;

    /**
     * 支付失败信息
     */
    private String payFailMessage;

    /**
     * 通商云订单号
     */
    private String orderNo;

    /**
     * 商户订单号（支付订单）
     */
    private String bizOrderNo;

}
