package com.xinshang.rest.modular.asmall.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 认证的请求dto
 *
 * @author fengshuonan
 * @Date 2017/8/24 14:00
 */
@Data
@ApiModel(value="理财人查看合同",description="理财人查看合同" )
public class ContractViewRequestDTO {

//    @NotNull
//    @ApiModelProperty(value = "项目ID",example="1",required=true)
//    private String projectId;
//
//    @ApiModelProperty(value = "理财人ID",example="1")
//    private String memberId;

    @NotNull
    @ApiModelProperty(value = "合同类型",example="1",required=true)
    private String type;

    @NotNull
    @ApiModelProperty(value = "加盟id",example="1",required=true)
    private Long JoinId;


}
