package com.xinshang.rest.modular.asmall.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 绑卡请求信息
 */
@Data
@ApiModel(value = "银行卡绑定", description = "银行卡绑定请求参数")
public class BankRequestDTO implements Serializable {

    @ApiModelProperty(value = "商户系统用户标识", required = true)
    private String bizUserId;

    @ApiModelProperty(value = "银行卡号", required = true)
    private String cardNo;

    @ApiModelProperty(value = "银行预留手机", required = true)
    private String phone;

    @ApiModelProperty(value = "姓名", required = true)
    private String name;

    @ApiModelProperty(value = "绑卡方式")
    private Long cardCheck;

    @ApiModelProperty(value = "证件类型", required = true)
    private String identityType;

    @ApiModelProperty(value = "证件号码", required = true)
    private String identityNo;

    @ApiModelProperty(value = "有效期")
    private String validate;

    @ApiModelProperty(value = "CVV2")
    private String cvv2;

    @ApiModelProperty(value = "是否安全卡")
    private String isSafeCard;

    @ApiModelProperty(value = "支付行号")
    private String unionBank;
}
