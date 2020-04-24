package com.xinshang.modular.biz.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 提现审核
 * </p>
 *
 * @author lvyingkai
 * @since 2019-10-22
 */
@TableName("tb_cash_out")
@Data
public class CashOut extends Model<CashOut> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 供应商id
     */
    @TableField("supplier_id")
    private Long supplierId;
    /**
     * 余额
     */
    private BigDecimal balance;
    /**
     * 提现
     */
    private BigDecimal money;
    /**
     * 状态 1申请  2审核通过  3审核不通过
     */
    private Integer state;
    /**
     * 银行卡id
     */
    @TableField("bank_card_id")
    private Long bankCardId;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 创建人
     */
    @TableField("create_user_id")
    private Long createUserId;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 备注审核未通过原因
     */
    private String remarks;

    /**
     * 提现申请人类型  1客户  2供应商
     */
    private Integer type;

    /**
     * 提现订单号
     */
    @TableField("biz_order_no")
    private String bizOrderNo;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "CashOut{" +
        "id=" + id +
        ", supplierId=" + supplierId +
        ", balance=" + balance +
        ", money=" + money +
        ", state=" + state +
        ", bankCardId=" + bankCardId +
        ", createTime=" + createTime +
        ", createUserId=" + createUserId +
        "}";
    }
}
