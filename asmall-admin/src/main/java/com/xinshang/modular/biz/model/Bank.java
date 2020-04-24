package com.xinshang.modular.biz.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 银行卡信息
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-11-12
 */
@Data
@TableName("tb_bank")
public class Bank extends Model<Bank> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 所属客户id/供应商id
     */
    @TableField("master_id")
    private Integer masterId;
    /**
     * 银行卡号
     */
    @TableField("bank_card_no")
    private String bankCardNo;
    /**
     * 银行名称
     */
    @TableField("bank_name")
    private String bankName;
    /**
     * 银行卡类型[1:借记卡,2:信用卡]
     */
    @TableField("card_type")
    private Integer cardType;
    /**
     * 账户属性[0:个人银行卡,1:企业对公账号]
     */
    @TableField("bank_card_pro")
    private Integer bankCardPro;
    /**
     * 是否默认[0:否,1:是]
     */
    @TableField("is_default")
    private Integer isDefault;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 用户类型[1:用户,2:供应商]
     */
    private Integer type;
    /**
     * 银行代码
     */
    @TableField("bank_code")
    private String bankCode;
    /**
     * 预留手机号
     * @return
     */
    private String phone;

    /**
     * 商户系统用户标识
     * @return
     */
    @TableField("biz_user_id")
    private String bizUserId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
