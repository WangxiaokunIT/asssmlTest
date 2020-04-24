package com.xinshang.modular.biz.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 客户资金流水
 * </p>
 *
 * @author jiangyizheng
 * @since 2019-10-23
 */
@TableName("tb_client_log_moneys")
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
     * 金额（元）
     */
    private BigDecimal balance;
    /**
     * 流水来源：1：充值 2：消费 3：提现 4：退款
     */
    @TableField("data_src")
    private Integer dataSrc;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

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


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
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

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ClientLogMoneys{" +
        "id=" + id +
        ", clientId=" + clientId +
        ", money=" + money +
        ", dataSrc=" + dataSrc +
        ", moneyType=" + moneyType +
        ", tradeNo=" + tradeNo +
        ", deleteFlg=" + deleteFlg +
        ", createTime=" + createTime +
        ", remark=" + remark +
        ", userName=" + userName +
        "}";
    }
}
