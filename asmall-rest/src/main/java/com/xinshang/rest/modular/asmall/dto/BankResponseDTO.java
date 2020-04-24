package com.xinshang.rest.modular.asmall.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@ApiModel("请求响应结果")
@AllArgsConstructor
public class BankResponseDTO {
    @ApiModelProperty(value = "商户系统用户标识", required = true)
    private String bizUserId;

    @ApiModelProperty(value = "流水号")
    private String tranceNum;

    @ApiModelProperty(value = "申请时间")
    private String transDate;

    @ApiModelProperty(value = "银行名称", required = true)
    private String bankName;

    @ApiModelProperty(value = "银行代码", required = true)
    private String bankCode;

    @ApiModelProperty(value = "银行卡类型", required = true)
    private Long cardType;

}
