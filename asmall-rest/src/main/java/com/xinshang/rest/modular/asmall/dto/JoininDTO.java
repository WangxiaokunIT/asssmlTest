package com.xinshang.rest.modular.asmall.dto;

import com.xinshang.rest.factory.PageFactory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 加盟表
 * </p>
 *
 * @author zhoushuai
 * @since 2019-10-27
 */
@Data
public class JoininDTO extends PageFactory {

    /**
     * 客户ID
     */
    @ApiModelProperty(value = "客户ID",example="1",required=true)
    private Long customId;
    /**
     * 招募ID
     */
    @ApiModelProperty(value = "招募ID",example="1",required=true)
    private Long projectId;
    /**
     * 招募金额
     */
    @ApiModelProperty(value = "招募金额",example="10000.00",required=true)
    private BigDecimal investmentAmount;

    /**
     *     支付方式payType 1 收银宝网关支付 2收银宝快捷支付 3 微信JS支付（公众号）_集团
     */
    @ApiModelProperty(value = "支付方式1 收银宝网关支付 2收银宝快捷支付 3 微信JS支付（公众号）_集团 4 余额",example="1",required=true)
    private Integer payType;

    /**
     * 银行卡号Id
     */
    @ApiModelProperty(value = "银行卡号ID",example="1",required=true)
    private Integer bankId;

    /**
     * 微信支付必传参数
     */
    @ApiModelProperty(value = "微信支付必传参数",example="1",required=true)
    private String openId;

    /**
     *     短信验证码【用于后台支付确认】
     */
    @ApiModelProperty(value = "短信验证码",example="1",required=true)
    private String verificationCode;

    /**
     *     订单申请的商户订单号 （加盟）
     */
    @ApiModelProperty(value = "加盟的商户订单号",example="1",required=true)
    private String bizOrderNo;
    /**
     *     交易编号
     */
    @ApiModelProperty(value = "交易编号",example="1",required=true)
    private String tradeNo;
    /**
     * 支付金额
     */
    @ApiModelProperty(value = "支付金额",example="1",required=true)
    private String payMent;

    /**
     * 合同ID
     */
    @ApiModelProperty(value = "合同ID",example="1",required=true)
    private String signId;

    @ApiModelProperty(value = "跳转地址",required=false)
    private String jumpUrl;
}
