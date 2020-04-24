package com.xinshang.rest.modular.asmall.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 实名认证请求
 */
@Data
@ApiModel(value = "实名认证", description = "实名认证请求参数")
public class MemberRequestDTO implements Serializable {
    @ApiModelProperty(value = "商户系统用户标识", required = true)
    private String bizUserId;
    @ApiModelProperty(value = "通商云认证")
    private Boolean isAuth;
    @ApiModelProperty(value = "姓名", required = true)
    private String name;
    @ApiModelProperty(value = "证件类型", example = "1", required = true)
    private Long identityType;
    @ApiModelProperty(value = "证件号码", required = true)
    private String identityNo;
}
