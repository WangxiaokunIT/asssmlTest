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
 * 
 * </p>
 *
 * @author wangxiaokun
 * @since 2019-11-04
 */
@TableName("tb_points")
public class Points extends Model<Points> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户名
     */
    @TableField("user_id")
    private String userId;
    /**
     * 知豆
     */
    private String point;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 来源
     */
    private String source;
    /**
     * 操作人
     */
    @TableField("operate_personnel")
    private String operatePersonnel;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOperatePersonnel() {
        return operatePersonnel;
    }

    public void setOperatePersonnel(String operatePersonnel) {
        this.operatePersonnel = operatePersonnel;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Points{" +
        "id=" + id +
        ", userId=" + userId +
        ", point=" + point +
        ", createTime=" + createTime +
        ", remark=" + remark +
        ", source=" + source +
        ", operatePersonnel=" + operatePersonnel +
        "}";
    }
}
