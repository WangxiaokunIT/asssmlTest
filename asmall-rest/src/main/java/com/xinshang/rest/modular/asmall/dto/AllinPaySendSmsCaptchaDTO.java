package com.xinshang.rest.modular.asmall.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author zhangjiajia
 */
@Data
@ApiModel(value="发送通联验证码参数",description="发送通联验证码参数")
public class AllinPaySendSmsCaptchaDTO {

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号",example="13361292948",required=true)
    private String phone;

    /**
     * 类型 9-绑定手机
     *     6-解绑手机
     */
    @ApiModelProperty(value = "验证码类型",example="9:绑定,6:解绑",required=true)
    private String verificationCodeType;

    /**
     * 商户系统用户标识
     */
    @ApiModelProperty(hidden = true)
    private String bizUserId;
}
