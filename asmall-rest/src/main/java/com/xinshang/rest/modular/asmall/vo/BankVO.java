package com.xinshang.rest.modular.asmall.vo;

import com.xinshang.rest.modular.asmall.dto.MemberDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 银行卡信息
 */
@Data
@ApiModel("银行卡信息")
public class BankVO extends MemberDTO {

    /**
     * id
     */
    @ApiModelProperty(value = "用户ID", example = "1", required = true)
    private Integer id;

    /**
     * 银行卡号
     */
    @ApiModelProperty(value = "银行卡号", example = "6218002400001386235", required = true)
    private String bankCardNo;
    /**
     * 银行名称
     */
    @ApiModelProperty(value = "银行名称", example = "中国**银行", required = true)
    private String bankName;
    /**
     * 银行卡类型[1:借记卡,2:信用卡]
     */
    @ApiModelProperty(value = "银行卡类型", example = "1", required = true)
    private Integer cardType;
    /**
     * 账户属性[0:个人银行卡,1:企业对公账号]
     */
    @ApiModelProperty(value = "帐户属性", example = "0", required = true)
    private Integer bankCardPro;
    /**
     * 是否默认[0:否,1:是]
     */
    @ApiModelProperty(value = "是否默认", example = "0", required = true)
    private Integer isDefault;
    /**
     * 用户类型[1:用户,2:供应商]
     */
    @ApiModelProperty(value = "用户类型", example = "0", required = true)
    private Integer type;

    /**
     * 是否设置支付密码【0否1是】
     */
    @ApiModelProperty()
    private Integer setPwdState;

    /**
     * 可用余额
     */
    @ApiModelProperty(value = "可用余额")
    private BigDecimal availableBalance;



}
