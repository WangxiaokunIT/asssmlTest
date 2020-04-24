package com.xinshang.rest.modular.asmall.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 加盟表
 * </p>
 *
 * @author zhoushuai
 * @since 2019-10-23
 */
@Data
public class JoininInfo {

    private static final long serialVersionUID = 1L;

    private Long joinId;
    /**
     * 客户
     */
    private Long customId;
    /**
     * 招募信息
     */
    private Integer projectId;
    /**
     * 投资金额
     */
    private BigDecimal investmentAmount;
    /**
     * 投资时间
     */
    private Date investmentTime;
    /**
     * 支付方式
     */
    private Integer paymentMethod;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人ID
     */
    private Integer createUserId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 手机号
     */
    private String  phone;
    /**
     * 项目编号
     */
    private String  number;
    /**
     * 项目名
     */
    private String  name;

    /**
     * 身份证号
     */
    private String cardNumber;

    /**
     * 供用商名称
     */
    private String sname;

    /**
     * 理财人电子邮箱
     */
    private String email;

    /**
     * 供应商地址
     */
    private String company_address;

    /**
     * 法人代表
     */
    private String legal_name;

    /**
     * 合同跳转地址
     */
    private String contractUrl;

    /**
     * 订单ID
     */
    private String bizOrderNo;

    /**
     * 代理开始时间
     */
    private Date startRecordTime;


    /**
     * 代理结束时间
     */
    private Date endRecordTime;

    private  Integer repaymentMethod;


}
