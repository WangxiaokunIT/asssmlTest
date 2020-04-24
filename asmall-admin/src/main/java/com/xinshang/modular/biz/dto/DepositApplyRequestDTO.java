package com.xinshang.modular.biz.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * @author zhangjiajia
 * 充值申请对象
 */
@Data
public class DepositApplyRequestDTO {

    /**
     * 供应商id
     */
    private Integer id;

    /**
     * 银行卡号
     */
    private String bankCardNo;

    /**
     * 充值金额(元)
     */
    private BigDecimal amount;

    /**
     * 支付方式 1:快捷支付,2:网关支付,3:支付宝,4:微信
     */
    private Integer payMethod;

    /**
     * 网银类型
     */
    private String payType;

    /**
     * 微信openid
     */
    private String wechatOpenId;

    /**
     * 访客ip
     */
    private String visitorIp;

    /**
     * 前台跳转网址
     */
    private String jumpUrl;
}
