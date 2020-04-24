package com.xinshang.rest.modular.asmall.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 账号信息
 * </p>
 *
 * @author zhoushuai
 * @since 2019-10-25
 */
@Data
@TableName("tb_account")
public class Account extends Model<Account> {

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
     * 账号所属类型[1:客户,2:供应商]
     */
    private Integer type;
    /**
     * 总金额
     */
    @TableField("totle_amount")
    private BigDecimal totleAmount;
    /**
     * 冻结金额
     */
    @TableField("freezing_amount")
    private BigDecimal freezingAmount;
    /**
     * 可用余额
     */
    @TableField("available_balance")
    private BigDecimal availableBalance;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
