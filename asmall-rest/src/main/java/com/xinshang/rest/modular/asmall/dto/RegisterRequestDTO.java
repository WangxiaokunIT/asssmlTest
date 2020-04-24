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
@ApiModel(value="注册参数",description="注册参数" )
public class RegisterRequestDTO {

    @ApiModelProperty(value = "手机号",example="13361292948",required=true)
    private String phoneNo;

    @ApiModelProperty(value = "验证码",example="123456",required=true)
    private String smsCaptcha;

    @ApiModelProperty(value = "密码",required=true)
    private String password;

    @ApiModelProperty(value = "确认密码",required=true)
    private String confirmPassword;

}
