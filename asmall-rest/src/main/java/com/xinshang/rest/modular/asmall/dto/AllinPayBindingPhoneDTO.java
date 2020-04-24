package com.xinshang.rest.modular.asmall.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author zhangjiajia
 */
@Data
@ApiModel(value="通联绑定手机参数",description="通联绑定手机参数" )
public class AllinPayBindingPhoneDTO {

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号",example="13361292948",required=true)
    private String phone;

    /**
     * 类型 9-绑定手机
     *     6-解绑手机
     */
    @ApiModelProperty(value = "验证码",example="133612",required=true)
    private String verificationCode ;

    /**
     * 商户系统用户标识
     */
    @ApiModelProperty(hidden=true)
    private String bizUserId;
}
