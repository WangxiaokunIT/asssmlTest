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
 * 银行卡信息
 * </p>
 *
 * @author sunhao
 * @since 2019-10-24
 */
@TableName("tb_bank")
@Data
@ApiModel("银行卡信息")
public class Bank extends Model<Bank> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "用户ID",example="1",required=true)
    private Integer id;
    /**
     * 用户id
     */
    @TableField("master_id")
    private Integer masterId;
    /**
     * 银行卡号
     */
    @TableField("bank_card_no")
    @ApiModelProperty(value = "银行卡号",example="6218002400001386235",required=true)
    private String bankCardNo;
    /**
     * 银行名称
     */
    @TableField("bank_name")
    @ApiModelProperty(value = "银行名称",example="中国**银行",required=true)
    private String bankName;
    /**
     * 银行代码
     */
    @TableField("bank_code")
    @ApiModelProperty(value = "银行代码")
    private String bankCode;
    /**
     * 银行卡类型[1:借记卡,2:信用卡]
     */
    @TableField("card_type")
    @ApiModelProperty(value = "银行卡类型",example="1",required=true)
    private Integer cardType;
    /**
     * 账户属性[0:个人银行卡,1:企业对公账号]
     */
    @TableField("bank_card_pro")
    @ApiModelProperty(value = "帐户属性",example="0",required=true)
    private Integer bankCardPro;
    /**
     * 银行预留手机
     */
    @TableField("phone")
    @ApiModelProperty(value = "预留手机")
    private String phone;
    /**
     * 是否默认[0:否,1:是]
     */
    @TableField("is_default")
    @ApiModelProperty(value = "是否默认",example="0",required=true)
    private Integer isDefault;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 用户类型[1:用户,2:供应商]
     */
    @ApiModelProperty(value = "用户类型",example="0",required=true)
    private Integer type;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
