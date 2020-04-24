package com.xinshang.rest.modular.asmall.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@ApiModel("认证响应结果")
@AllArgsConstructor
public class MemberResponseDTO {

    @ApiModelProperty(value = "商户系统用户标识", required = true)
    private String bizUserId;
    @ApiModelProperty(value = "姓名", required = true)
    private String name;
    @ApiModelProperty(value = "证件类型", example = "1", required = true)
    private Long identityType;
    @ApiModelProperty(value = "证件号码", required = true)
    private String identityNo;

}
