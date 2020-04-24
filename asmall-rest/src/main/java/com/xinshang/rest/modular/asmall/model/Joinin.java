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
 * 加盟表
 * </p>
 *
 * @author zhoushuai
 * @since 2019-10-23
 */
/**
 * <p>
 * 加盟表
 * </p>
 *
 * @author zhoushuai
 * @since 2019-10-25
 */
@TableName("tb_joinin")
@Data
public class Joinin extends Model<Joinin> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "join_id", type = IdType.AUTO)
    private Long joinId;
    /**
     * 客户id
     */
    @TableField("custom_id")
    private Long customId;
    /**
     * 招募信息id
     */
    @TableField("project_id")
    private Long projectId;
    /**
     * 投资金额
     */
    @TableField("investment_amount")
    private BigDecimal investmentAmount;
    /**
     * 投资时间
     */
    @TableField("investment_time")
    private Date investmentTime;
    /**
     * 支付方式
     */
    @TableField("payment_method")
    private Integer paymentMethod;
    /**
     * 状态
     */
    private Integer Status;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 创建人ID
     */
    @TableField("create_user_id")
    private Integer createUserId;

    /**
     * 订单ID
     */
    @TableField("biz_order_no")
    private String bizOrderNo;

    @Override
    protected Serializable pkVal() {
        return this.joinId;
    }

}
