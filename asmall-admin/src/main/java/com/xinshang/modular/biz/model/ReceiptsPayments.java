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
 * 账户收支明细
 * </p>
 *
 * @author jyz
 * @since 2019-11-05
 */
@TableName("tb_receipts_payments")
@Data
public class ReceiptsPayments extends Model<ReceiptsPayments> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 收支明细流水号
     */
    @TableField("trade_no")
    private String tradeNo;
    /**
     * 账户集名称
     */
    @TableField("account_set_name")
    private String accountSetName;
    /**
     * 变更时间
     */
    @TableField("change_time")
    private String changeTime;
    /**
     * 现有金额
     */
    @TableField("cur_amount")
    private BigDecimal curAmount;
    /**
     * 原始金额
     */
    @TableField("ori_amount")
    private BigDecimal oriAmount;
    /**
     * 变更金额
     */
    @TableField("chg_amount")
    private BigDecimal chgAmount;
    /**
     * 现有冻结金额
     */
    @TableField("cur_freezen_amount")
    private BigDecimal curFreezenAmount;
    /**
     * 商户订单号（支付订单）
     */
    @TableField("biz_order_no")
    private String bizOrderNo;
    /**
     * 备注
     */
    @TableField("extend_info")
    private String extendInfo;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    private String dateStart;

    private String dateEnd;

    /**
     * 1 平台，2客户或供应商
     */
    @TableField(exist = false)
    private Integer queryType;

    /**
     * 消费类型
     */
    @TableField(exist = false)
    private String type;

    /**
     *
     */
    @TableField(exist = false)
    private String bizUserId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
