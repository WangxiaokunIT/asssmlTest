package com.xinshang.rest.modular.asmall.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 认证的请求dto
 *
 * @author fengshuonan
 * @Date 2017/8/24 14:00
 */
@Data
@ApiModel(value="创建合同",description="创建合同" )
public class CreateContractRequestDTO {

    @NotNull
    @ApiModelProperty(value = "项目ID",example="1",required=true)
    private Long projectId;


    @NotNull
    @ApiModelProperty(value = "合同类型",example="1",required=true)
    private Integer type;

    @NotNull
    @ApiModelProperty(value = "投资金额",example="0.0000",required=true)
    private BigDecimal money;

    @NotNull
    @ApiModelProperty(value = "加盟id",example="1")
    private Long JoinId;




}
