package com.xinshang.modular.biz.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-10-23
 */
@TableName("tb_eqb_contract")
public class EqbContract extends Model<EqbContract> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 项目主键
     */
    @TableField("project_id")
    private Long projectId;
    /**
     * 客户主键
     */
    @TableField("member_id")
    private Long memberId;
    /**
     * 存证明id
     */
    @TableField("sign_id")
    private String signId;
    /**
     * 合同地址
     */
    @TableField("contract_url")
    private String contractUrl;
    /**
     * 文件名称
     */
    @TableField("file_name")
    private String fileName;
    /**
     * 1.募集合同
     */
    private Integer type;
    /**
     * 失效时间
     */
    @TableField("failure_time")
    private String failureTime;
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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }

    public String getContractUrl() {
        return contractUrl;
    }

    public void setContractUrl(String contractUrl) {
        this.contractUrl = contractUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getFailureTime() {
        return failureTime;
    }

    public void setFailureTime(String failureTime) {
        this.failureTime = failureTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "EqbContract{" +
        "id=" + id +
        ", projectId=" + projectId +
        ", memberId=" + memberId +
        ", signId=" + signId +
        ", contractUrl=" + contractUrl +
        ", fileName=" + fileName +
        ", type=" + type +
        ", failureTime=" + failureTime +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
