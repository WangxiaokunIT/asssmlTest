package com.xinshang.modular.biz.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * <p>
 * 审核记录表
 * </p>
 *
 * @author lvyingkai
 * @since 2019-10-18
 */
@TableName("tb_examine")
public class Examine extends Model<Examine> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 审核类型 1招募发布
     */
    private Integer type;
    /**
     * 审批的项目id
     */
    @TableField("project_id")
    private Long projectId;
    /**
     * 审核状态 1 申请 2审核通过 3审核不通过 4取消
     */
    private Integer state;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 申请人id/审核人id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 申请人/审核人姓名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 创建时间
     */
    @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Examine{" +
        "id=" + id +
        ", type=" + type +
        ", projectId=" + projectId +
        ", state=" + state +
        ", remarks=" + remarks +
        ", userId=" + userId +
        ", userName=" + userName +
        ", createTime=" + createTime +
        "}";
    }
}
