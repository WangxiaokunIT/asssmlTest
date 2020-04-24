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
 * 设置信息
 * </p>
 *
 * @author lvyingkai
 * @since 2019-11-26
 */
@TableName("tb_app_set")
@Data
public class AppSet extends Model<AppSet> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户协议
     */
    @TableField("user_agreement")
    private String userAgreement;
    /**
     * 隐私协议
     */
    @TableField("privacy_protocol")
    private String privacyProtocol;

    /**
     * 代理协议
     */
    @TableField("agent_protocol")
    private String agentProtocol;

    /**
     * vip说明
     */
    @TableField("vip_description")
    private String vipDescription;

    /**
     * 代理说明
     */
    @TableField("agent_description")
    private String agentDescription;
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


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
