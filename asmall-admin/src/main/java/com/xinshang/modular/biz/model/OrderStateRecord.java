package com.xinshang.modular.biz.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 订单状态记录
 * </p>
 *
 * @author wangxiaokun
 * @since 2019-10-24
 */
@TableName("tb_order_state_record")
public class OrderStateRecord extends Model<OrderStateRecord> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 订单
     */
    @TableField("order_id")
    private String orderId;
    /**
     * 操作人,自动为system
     */
    @TableField("operator_user")
    private String operatorUser;
    /**
     * 操作时间
     */
    @TableField("operator_time")
    private Date operatorTime;
    /**
     * 状态
     */
    private Integer state;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否当前状态
     */
    @TableField("is_current")
    private Integer isCurrent;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOperatorUser() {
        return operatorUser;
    }

    public void setOperatorUser(String operatorUser) {
        this.operatorUser = operatorUser;
    }

    public Date getOperatorTime() {
        return operatorTime;
    }

    public void setOperatorTime(Date operatorTime) {
        this.operatorTime = operatorTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(Integer isCurrent) {
        this.isCurrent = isCurrent;
    }

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
