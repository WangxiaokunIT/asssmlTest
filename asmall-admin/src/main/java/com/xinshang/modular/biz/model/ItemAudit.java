package com.xinshang.modular.biz.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 商品发布审核表
 * </p>
 *
 * @author sunhao
 * @since 2019-10-18
 */
@TableName("tb_item_audit")
@Data
public class ItemAudit extends Model<ItemAudit> {

    private static final long serialVersionUID = 1L;

    /**
     * 商品审核ID
     */
    private Long id;
    /**
     * 商品ID
     */
    @TableField("product_id")
    private Long productId;
    /**
     * 审核状态[0待审核1已通过-1未通过]
     */
    @TableField("audit_status")
    private Integer auditStatus;
    /**
     * 审核描述
     */
    @TableField("audit_detail")
    private String auditDetail;
    /**
     * 审批人
     */
    @TableField("audit_user")
    private String auditUser;
    /**
     * 审批时间
     */
    @TableField("audit_time")
    private Date auditTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
