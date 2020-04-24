package com.xinshang.rest.modular.asmall.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-10-19
 */
@Data
@ApiModel("用户详情")
public class MemberDTO {

    @ApiModelProperty(value = "用户Id")
    private Long memberId;
    /**
     * 账号
     */
    @ApiModelProperty(value = "账号")
    private String username;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private  String realName;
    /**
     * 注册手机号
     */
    @ApiModelProperty(value = "注册手机号", required = true)
    private String phone;
    /**
     * 注册邮箱
     */
    @ApiModelProperty(value = "注册邮箱")
    private String email;
    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String file;
    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickname;
    /**
     * 身份证号码
     */
    @ApiModelProperty(value = "身份证号", example = "411424199998885555", required = true)
    private String cardNumber;
    /**
     * 是否是VIP
     */
    @ApiModelProperty(value = "是否是VIP")
    private Integer vip;

    /**
     * 账户余额
     */
    @ApiModelProperty(value = "账户余额")
    private BigDecimal balance;

    /**
     * 积分余额
     */
    @ApiModelProperty(value = "积分余额")
    private Integer points;

    /**
     * 是否实名认证 0否 1是
     */
    @ApiModelProperty(value = "是否实名认证")
    private Integer realNameState;
    /**
     * 证件类型[1,]
     */
    @ApiModelProperty(value = "证件类型")
    private Integer documenType;

    /**
     * 商户系统用户标识
     */
    @ApiModelProperty(value = "商户系统用户标识")
    private String bizUserId;

    /**
     * 通联电子协议编号
     */
    @ApiModelProperty(value = "通联电子协议编号")
    private String contractNo;

    /**
     * openid
     */
    @ApiModelProperty(value = "openid")
    private String openid;


}
