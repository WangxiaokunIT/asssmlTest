package com.xinshang.rest.modular.asmall.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zhangjiajia
 * 提现确认对象
 */
@Data
@ApiModel(value="提现确认",description="提现确认" )
public class CashWithdrawConfirmRequestDTO implements Serializable {

    private static final long serialVersionUID = -830800367243058049L;

    /**
     * 短信验证码
     */
    @NotNull
    @ApiModelProperty(value = "验证码",required=true)
    private String verificationCode;

    /**
     * 提现金额
     */
    @NotNull
    @ApiModelProperty(value = "提现金额",required=true)
    private BigDecimal amount;

    /**
     * 订单号
     */
    @NotNull
    @ApiModelProperty(value = "订单号",required=true)
    private String bizOrderNo;
}
