package com.xinshang.rest.modular.asmall.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * <p>
 * 支付密码信息
 * </p>
 *
 * @author sunhao
 * @since 2019-10-24
 */
@Data
@ApiModel("支付密码信息")
public class PayPwdResponseDTO {

    @ApiModelProperty(value = "商户系统用户标识", required = true)
    private String bizUserId;
    @ApiModelProperty(value = "手机号码", required = true)
    private String phone;
    @ApiModelProperty(value = "姓名", required = true)
    private String name;
    @ApiModelProperty(value = "证件类型", example = "1", required = true)
    private Long identityType;
    @ApiModelProperty(value = "证件号码", required = true)
    private String identityNo;
    @ApiModelProperty(value = "设置密码之后，跳转返回 的页面地址",required = true)
    private String jumpUrl;
    @ApiModelProperty(value = "后台通知地址", required = true)
    private String backUrl;
    @ApiModelProperty(value = "设置支付密码结果", required = true)
    private String result;

}
