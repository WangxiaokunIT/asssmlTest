package com.xinshang.rest.modular.asmall.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-10-19
 */
@TableName("tb_member")
@Data
@ApiModel("用户信息")
public class Member extends Model<Member> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "用户ID",required=true)
    private Long id;
    /**
     * 账号
     */
    @ApiModelProperty(value = "账号")
    private String username;

    /**
     * 密码，加密存储
     */
    private String password;
    /**
     * 注册手机号
     */
    @ApiModelProperty(value = "注册手机号",required=true)
    private String phone;
    /**
     * 注册邮箱
     */
    @ApiModelProperty(value = "注册邮箱",required=true)
    private String email;

    /**
     * 性别
     */
    private String sex;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 地址
     */
    private String address;
    /**
     * 状态[0:无效,1:有效]
     */
    private Integer state;
    /**
     * 头像
     */
    @ApiModelProperty(value = "头像",required=true)
    private String file;
    /**
     * 备注
     */
    private String description;
    /**
     * 积分余额
     */
    @ApiModelProperty(value = "积分余额",required=true)
    private Integer points;

    /**
     * 姓名
     */
    @TableField(value = "real_name")
    private String realName;

    /**
     * 是否是VIP 0否 1是
     */
    @ApiModelProperty(value = "是否是VIP",required=true)
    private Integer vip;
    /**
     * vip开始时间
     */
    @TableField("vip_start_time")
    private Date vipStartTime;
    /**
     * vip到期时间
     */
    @TableField("vip_end_time")
    private Date vipEndTime;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称",required=true)
    private String nickname;
    /**
     * 身份证号码
     */
    @TableField("card_number")
    @ApiModelProperty(value = "身份证号码",required=true)
    private String cardNumber;
    /**
     * 是否实名认证 0否 1是
     */
    @TableField("real_name_state")
    @ApiModelProperty(value = "是否实名认证",required=true)
    private Integer realNameState;
    /**
     * 是否绑定银行卡 0否 1是
     */
    @TableField("is_set_bank_state")
    @ApiModelProperty(value = "是否绑定银行卡")
    private Integer isSetBankState;
    /**
     * 是否设置支付密码 0否 1是
     */
    @TableField("set_pwd_state")
    @ApiModelProperty(value = "是否设置支付密码")
    private Integer setPwdState;
    /**
     * 证件类型[1,]
     */
    @TableField("documen_type")
    @ApiModelProperty(value = "证件类型",required=true)
    private Integer documenType;

    /**
     * 商户系统用户标识
     */
    @TableField("biz_user_id")
    @ApiModelProperty(value = "商户系统用户标识")
    private String bizUserId;

    /**
     * 通商云用户唯一标识
     */
    @TableField("allinpay_user_id")
    @ApiModelProperty(value = "通商云用户唯一标识")
    private String allinpayUserId;

    /**
     * 通商云合同编号
     */
    @TableField("contract_no")
    @ApiModelProperty(value = "通商云合同编号")
    private String contractNo;

    /**
     * 审核状态
     *
     * @return
     */
    @TableField("audit_status")
    @ApiModelProperty(value = "审核状态",required=true)
    private Integer auditStatus;

    /**
     * 微信用户的唯一标识
     */
    @ApiModelProperty(value = "微信用户的唯一标识")
    private String openid;

    /**
     * 密码盐
     */
    @ApiModelProperty(value = "密码盐")
    private String salt;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间",required=true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间",required=true)
    private Date updated;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    /**
     * 回调地址
     */
    @ApiModelProperty(value = "回调地址")
    @TableField(exist = false)
    private String jumpUrl;

}
