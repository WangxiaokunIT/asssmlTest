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
 * 客户积分流水
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-12-24
 */
@TableName("tb_integral_log_moneys")
public class IntegralLogMoneys extends Model<IntegralLogMoneys> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 客户id
     */
    @TableField("client_id")
    private Integer clientId;
    /**
     * 客户用户名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 积分
     */
    private Integer integral;
    /**
     * 积分来源：1：加盟 2：兑换
     */
    @TableField("data_src")
    private Integer dataSrc;
    /**
     * 积分标志：1：收入 2：支出
     */
    @TableField("money_type")
    private Integer moneyType;
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
     * 积分备注
     */
    private String remark;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public Integer getDataSrc() {
        return dataSrc;
    }

    public void setDataSrc(Integer dataSrc) {
        this.dataSrc = dataSrc;
    }

    public Integer getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(Integer moneyType) {
        this.moneyType = moneyType;
    }

    public Integer getDeleteFlg() {
        return deleteFlg;
    }

    public void setDeleteFlg(Integer deleteFlg) {
        this.deleteFlg = deleteFlg;
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "IntegralLogMoneys{" +
        "id=" + id +
        ", clientId=" + clientId +
        ", userName=" + userName +
        ", integral=" + integral +
        ", dataSrc=" + dataSrc +
        ", moneyType=" + moneyType +
        ", deleteFlg=" + deleteFlg +
        ", createTime=" + createTime +
        ", remark=" + remark +
        "}";
    }
}
