package com.xinshang.modular.biz.dto;

import com.xinshang.core.common.constant.factory.PageFactory;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 提现申请查询条件
 * @author lyk
 */
@Data
public class JoininDTO extends PageFactory {

    /**
     * 状态
     */
    private Long joinId;
    /**
     * 状态
     */
    private Integer status;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 项目编号
     */
    private String number;

    /**
     * 项目名
     */
    private String name;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    private Date startTime1;
    private Date endTime1;


    /**
     * 客户ID
     */
     private Long customId;
    /**
     * 招募ID
     */
     private Long projectId;
    /**
     * 招募金额
     */
     private BigDecimal investmentAmount;

    /**
     *     支付方式payType 1 收银宝网关支付 2收银宝快捷支付 3 微信JS支付（公众号）_集团
     */
     private Integer payType;

    /**
     * 银行卡号Id
     */
     private Integer bankId;

    /**
     * 微信支付必传参数
     */
     private String openId;

    /**
     *     短信验证码【用于后台支付确认】
     */
     private String verificationCode;

    /**
     *     订单申请的商户订单号 （加盟）
     */
     private String bizOrderNo;

    private String bizUserId;
    /**
     *     交易编号
     */
     private String tradeNo;
    /**
     * 支付金额
     */
     private Long payMent;

     private String joinList;
}
