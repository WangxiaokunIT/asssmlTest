package com.xinshang.rest.modular.asmall.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单状态记录
 * </p>
 *
 * @author wangxiaokun
 * @since 2019-10-24
 */
@TableName("tb_order_state_record")
@Data
@ApiModel("订单状态变更信息")
public class OrderStateRecord extends Model<OrderStateRecord> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID",required=true)
    private Long id;
    /**
     * 订单ID
     */
    @TableField("order_id")
    @ApiModelProperty(value = "订单ID")
    private String orderId;
    /**
     * 操作人,自动为system
     */
    @TableField("operator_user")
    @ApiModelProperty(value = "操作人,自动为system")
    private String operatorUser;
    /**
     * 操作时间
     */
    @TableField("operator_time")
    @ApiModelProperty(value = "操作时间")
    private Date operatorTime;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private Integer state;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 是否当前状态
     */
    @TableField("is_current")
    @ApiModelProperty(value = "是否当前状态")
    private boolean isCurrent;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "OrderStateRecord{" +
        "id=" + id +
        ", orderId=" + orderId +
        ", operatorUser=" + operatorUser +
        ", operatorTime=" + operatorTime +
        ", state=" + state +
        ", remark=" + remark +
        ", isCurrent=" + isCurrent +
        "}";
    }
}
