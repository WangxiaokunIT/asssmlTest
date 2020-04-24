package com.xinshang.rest.modular.asmall.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 认证的请求dto
 *
 * @author fengshuonan
 * @Date 2017/8/24 14:00
 */
@Data
@ApiModel(value="token参数",description="token请求参数" )
public class AuthCaptchaRequestDTO {

    @ApiModelProperty(value = "手机号",example="13361292948",required=true)
    private String phoneNo;

    @ApiModelProperty(value = "验证码",example="123456",required=true)
    private String smsCaptcha;

}
