package com.xinshang.modular.biz.dto;

import lombok.Data;

/**
 * @author zhangjiajia
 */
@Data
public class SetCompanyInfoDTO {

    /**
     * 商户系统用户标识
     */
    private String bizUserId;

    /**
     * 企业名称
     */
    private String name;


    /**
     * 认证类型
     */
    private Integer authType;

    /**
     * 统一社会信用（一证）
     */
    private String uniCredit;

    /**
     * 营业执照号
     */
    private String businessLicense;

    /**
     * 组织机构代码
     */
    private String organizationCode;

    /**
     * 税务登记证
     */
    private String taxRegister;

    /**
     * 法人姓名
     */
    private String legalName;

    /**
     * 法人证件类型
     */
    private Integer identityType;

    /**
     * 法人证件号码
     */
    private String identityNo;

    /**
     * 法人手机号码
     */
    private String phone;

    /**
     * 法人对公账户
     */
    private String accountNo;
    /**
     * 开户银行名称
     */
    private String parentBankMame;
    /**
     * 开户行支行名称
     */
    private String bankName;
    /**
     * 支付行号
     */
    private String unionbank;


}
