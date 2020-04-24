package com.xinshang.rest.modular.asmall.dto;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "供应商id",example="1",required=true)
    private Integer id;

    /**
     * 银行卡号
     */
    @ApiModelProperty(value = "银行卡号",example="1",required=true)
    private String bankCardNo;

    /**
     * 银行卡号Id
     */
    @ApiModelProperty(value = "银行卡号Id",example="1",required=true)
    private Integer bankId;

    /**
     * 充值金额(元)
     */
    @ApiModelProperty(value = "充值金额",example="1",required=true)
    private BigDecimal amount;

    /**
     * 支付方式 1:快捷支付,2:网关支付,3:支付宝,4:微信
     */
    @ApiModelProperty(value = "支付方式",example="1",required=true)
    private Integer payMethod;

    /**
     * 网银类型
     */
    @ApiModelProperty(value = "网银类型",example="1",required=true)
    private String payType;

    /**
     * 微信openid
     */
    @ApiModelProperty(value = "微信openid",example="1",required=true)
    private String wechatOpenId;

    /**
     * 访客ip
     */
    @ApiModelProperty(value = "访客ip",example="1",required=true)
    private String visitorIp;

    /**'
     * 回调地址
     */
    @ApiModelProperty(value = "回调地址",example="1",required=true)
    private  String jumpUrl;
}
