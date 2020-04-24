package com.xinshang.modular.biz.dto;

import lombok.Data;
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
@Data
public class MemberDTO {


    private Long id;
    /**
     * 用户名
     */
    private String username;

    private Date beginTime;

    private Date endTime;
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
     * 创建时间
     */
    private Date created;
    /**
     * 更新时间
     */
    private Date updated;
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
     * 账户余额
     */
    private BigDecimal balance;
    /**
     * 来源
     */
    private String source;
    /**
     * 推荐人昵称
     */
    private String recommendedNickname;
    /**
     * 推荐人手机
     */
    private String recommendedPhone;
    /**
     * 知豆余额
     */
    private Integer beanBalance;
    /**
     * 是否是VIP 0否 1是
     */
    private Integer vip;
    /**
     * vip开始时间
     */
    private Date vipStartTime;
    /**
     * vip到期时间
     */
    private Date vipEndTime;
    /**
     * 所属区域id
     */
    private Long area;
    /**
     * 所属区域name
     */
    private String areaName;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 身份证号码
     */
    private String cardNumber;
    /**
     * 审核状态
     */
    private Integer auditStatus;

    private String auditDetail;
}
