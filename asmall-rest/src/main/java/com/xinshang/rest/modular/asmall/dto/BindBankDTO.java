package com.xinshang.rest.modular.asmall.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * <p>
 * 银行卡信息
 * </p>
 *
 * @author sunhao
 * @since 2019-10-24
 */
@Data
@ApiModel("绑定银行卡信息")
public class BindBankDTO {
    /**
     * 银行卡ID
     */
    @ApiModelProperty(value = "银行卡ID")
    private Integer bankId;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户ID", example = "1", required = true)
    private Integer masterId;
    /**
     * 银行卡号
     */
    @ApiModelProperty(value = "银行卡号", example = "6218002400001386235", required = true)
    private String bankCardNo;
    /**
     * 银行名称
     */
    @ApiModelProperty(value = "银行名称", example = "中国**银行", required = true)
    private String bankName;
    /**
     * 银行代码
     */
    @ApiModelProperty(value = "银行代码")
    private String bankCode;
    /**
     * 银行卡类型[1:借记卡,2:信用卡]
     */
    @ApiModelProperty(value = "银行卡类型", example = "1", required = true)
    private Integer cardType;
    /**
     * 账户属性[0:个人银行卡,1:企业对公账号]
     */
    @ApiModelProperty(value = "帐户属性", example = "0", required = true)
    private Integer bankCardPro;
    /**
     * 用户类型
     */
    @ApiModelProperty(value = "用户类型")
    private Integer type;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码", required = true)
    private String verificationCode;
    /**
     * 预留手机
     */
    @ApiModelProperty(value = "预留手机", required = true)
    private String phone;
    /**
     * 流水号
     */
    @ApiModelProperty(value = "流水号", required = true)
    private String tranceNum;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名", required = true)
    private String realName;
    /**
     * 是否默认
     */
    @ApiModelProperty(value = "是否默认")
    private Integer isDefault;
}
