package com.xinshang.rest.modular.asmall.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * PC用户信息
 * @author lyk
 */
@Data
@ApiModel("用户信息")
public class PCMemberVO {

    @ApiModelProperty(value = "头像", required = true)
    private String file;

    @ApiModelProperty(value = "姓名", required = true)
    private String realName;

    @ApiModelProperty(value = "手机号", required = true)
    private String phone;

    @ApiModelProperty(value = "账户余额", required = true)
    private String balance;

    @ApiModelProperty(value = "实名认证{1:已实名，2：未实名}", required = true)
    private Integer realNameAuthentication;

    @ApiModelProperty(value = "绑定手机号{1:已绑定，2：未绑定}", required = true)
    private Integer bindMobileNumber;

    @ApiModelProperty(value = "绑定银行卡{1:已绑定，2：未绑定}", required = true)
    private Integer bindBankCard;

    @ApiModelProperty(value = "签订协议{1:已签订，2：未签订}", required = true)
    private Integer signingAgreement;

    @ApiModelProperty(value = "设置支付密码{1:已设置，2：未设置}", required = true)
    private Integer paymentPassword;

    @ApiModelProperty(value = "是否是vip{0:不是，1：是}", required = true)
    private Integer whetherVip;

}
