package com.xinshang.rest.modular.asmall.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lyk
 */
@Data
@ApiModel("token校验结果")
public class TokenVO {

    @ApiModelProperty(value = "token")
    private String token;

    @ApiModelProperty(value = "是否过期")
    private Boolean state;

}
