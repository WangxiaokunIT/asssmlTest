package com.xinshang.modular.biz.model;

import com.baomidou.mybatisplus.enums.IdType;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xinshang.core.annotation.DictField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

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
public class Member extends Model<Member> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 账号
     */
    private String username;
    /**
     * 密码，加密存储
     */
    private String password;
    /**
     * 注册手机号
     */
    private String phone;
    /**
     * 注册邮箱
     */
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
    private String file;
    /**
     * 备注
     */
    private String description;
    /**
     * 积分余额
     */
    private Integer points;

    /**
     * 姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 是否是VIP 0否 1是
     */
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
    private String nickname;
    /**
     * 身份证号码
     */
    @TableField("card_number")
    private String cardNumber;
    /**
     * 证件类型
     */
    @DictField("identity_type")
    @TableField("documen_type")
    private String documenType;

    /**
     * 商户系统用户标识
     */
    @TableField("biz_user_id")
    private String bizUserId;

    /**
     * 通商云用户唯一标识
     */
    @TableField("allinpay_user_id")
    private String allinpayUserId;

    /**
     * 通商云合同编号
     */
    @TableField("contract_no")
    private String contractNo;

    /**
     * 审核状态
     *
     * @return
     */
    @TableField("audit_status")
    private Integer auditStatus;
    /**
     * 是否实名认证
     *
     * @return
     */
    @TableField("real_name_state")
    private Integer realNameState;
    /**
     * 是否设置支付密码
     *
     * @return
     */
    @TableField("set_pwd_state")
    private Integer setPwdState;
    /**
     * 是否绑定银行卡
     *
     * @return
     */
    @TableField("is_set_bank_state")
    private Integer isSetBankState;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created;

    /**
     * 微信用户的唯一标识
     */
    @TableField("openid")
    private String openid;

    /**
     * 密码盐
     */
    @TableField("salt")
    private String salt;

    /**
     * 更新时间
     */
    private Date updated;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
