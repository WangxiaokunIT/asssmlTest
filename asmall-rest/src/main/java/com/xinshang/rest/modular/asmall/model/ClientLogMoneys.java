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
 * 客户资金流水
 * </p>
 *
 * @author jiangyizheng
 * @since 2019-10-23
 */
@TableName("tb_client_log_moneys")
@Data
public class ClientLogMoneys extends Model<ClientLogMoneys> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 客户id
     */
    @TableField("client_id")
    private Long clientId;
    /**
     * 金额（元）
     */
    private BigDecimal money;

    /**
     * 流水来源：1：充值 2：消费 3：提现 4:退款
     */
    @TableField("data_src")
    private Integer dataSrc;
    /**
     * 流水标志：1：收入 2：支出
     */
    @TableField("money_type")
    private Integer moneyType;
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
    /**
     * 客户用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 对方
     */
    private String transferee;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
