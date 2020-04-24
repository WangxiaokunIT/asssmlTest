package com.xinshang.modular.biz.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zhangjiajia
 * 提现申请对象
 */
@Data
public class CashWithdrawConfirmRequestDTO implements Serializable {


    private static final long serialVersionUID = -3176665759207659884L;
    /**
     * 商户系统用户标识，商户
     * 系统中唯一编号。
     */
    private String bizUserId;

    /**
     * 订单申请的商户订单号
     * （支付订单）
     */
    private String bizOrderNo;

    /**
     * 交易编号
     */
    private String tradeNo;

    /**
     * 短信验证码
     */
    private String verificationCode;

    /**
     * ip 地址
     */
    private String consumerIp;

    /**
     * 供应商id
     */
    private Integer supplierId;

    /**
     * 充值金额
     */
    private BigDecimal amount;

}
