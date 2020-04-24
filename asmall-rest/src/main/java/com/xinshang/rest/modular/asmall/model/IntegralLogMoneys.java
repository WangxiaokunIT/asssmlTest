package com.xinshang.rest.modular.asmall.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 客户积分流水
 * </p>
 *
 * @author zhousshuai
 * @since 2019-10-27
 */
@Data
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
