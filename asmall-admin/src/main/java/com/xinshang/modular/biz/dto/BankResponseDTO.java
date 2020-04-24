package com.xinshang.modular.biz.dto;

import lombok.Data;

/**
 * @author zhangjiajia
 */
@Data
public class BankResponseDTO {
    /**
     * 商户用户id
     */
    private String bizUserId;

    /**
     * 流水号
     */
    private String tranceNum;

    /**
     * 申请时间
     */
    private String transDate;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 银行代码
     */
    private String bankCode;

    /**
     * 银行卡类型
     */
    private Long cardType;

    /**
     * 银行预留手机
     */
    private String phone;
    /**
     * 银行卡号
     */
    private String cardNo;

    /**
     * 是否默认
     */
    private Integer isDefault;
}
