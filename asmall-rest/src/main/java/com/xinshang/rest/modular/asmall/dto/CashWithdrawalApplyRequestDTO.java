package com.xinshang.rest.modular.asmall.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zhangjiajia
 * 提现申请对象
 */
@Data
@ApiModel(value="提现申请",description="提现申请" )
public class CashWithdrawalApplyRequestDTO implements Serializable {

    private static final long serialVersionUID = 6497236027026206073L;

    /**
     * 银行卡id
     */
    @NotNull
    @ApiModelProperty(value = "银行卡id",required=true)
    private Integer bankId;


    /**
     * 提现金额
     */
    @NotNull
    @ApiModelProperty(value = "提现金额",required=true)
    private BigDecimal amount;
}
