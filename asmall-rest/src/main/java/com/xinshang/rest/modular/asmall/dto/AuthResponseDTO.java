package com.xinshang.rest.modular.asmall.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 认证的响应结果
 *
 * @author fengshuonan
 * @Date 2017/8/24 13:58
 */
@Data
@ApiModel("认证响应结果")
@AllArgsConstructor
public class AuthResponseDTO implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    /**
     * jwt token
     */
    @ApiModelProperty(value = "token")
    private final String token;

    /**
     * 用于客户端混淆md5加密
     */
    @ApiModelProperty(value = "随机数")
    private final String randomKey;


}
