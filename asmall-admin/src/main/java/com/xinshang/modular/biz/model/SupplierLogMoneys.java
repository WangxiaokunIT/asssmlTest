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
 * 供应商资金流水
 * </p>
 *
 * @author jiangyizheng
 * @since 2019-10-23
 */
@TableName("tb_supplier_log_moneys")
@Data
public class SupplierLogMoneys extends Model<SupplierLogMoneys> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 供应商id
     */
    @TableField("supplier_id")
    private Integer supplierId;
    /**
     * 金额（元）
     */
    private BigDecimal money;
    /**
     * 流水来源：1：充值 2：投资 3：提现
     */
    @TableField("data_src")
    private Integer dataSrc;
    /**
     * 流水标志：1：收入 2：支出
     */
    @TableField("money_type")
    private Integer moneyType;
    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;
    /**
     * 通联流水号
     */
    @TableField("trade_no")
    private String tradeNo;
    /**
     * 有效状态 ：1：有效 0：删除
     */
    @TableField("delete_flg")
    private Integer deleteFlg;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 流水备注
     */
    private String remark;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
