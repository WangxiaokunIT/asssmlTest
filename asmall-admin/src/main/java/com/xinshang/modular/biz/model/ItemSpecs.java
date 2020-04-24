package com.xinshang.modular.biz.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 商品规格
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-11-25
 */
@TableName("tb_item_specs")
@Data
public class ItemSpecs extends Model<ItemSpecs> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 产品id
     */
    @TableField("item_id")
    private Long itemId;
    /**
     * 产品编码
     */
    @TableField("item_no")
    private String itemNo;
    /**
     * 规格编码
     */
    @TableField("specs_no")
    private String specsNo;
    /**
     * 规格
     */
    @TableField("specs_values")
    private String specsValues;
    /**
     * 索引值
     */
    @TableField("sort_num")
    private Integer sortNum;
    /**
     * 库存
     */
    private Integer stock;
    /**
     * 价格
     */
    private BigDecimal price;
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
     * 规格图片
     */
    private String image;

    /**
     * VIP价格
     */
    @TableField("vip_discount")
    private BigDecimal vipDiscount;
    /**
     * 库存预警值
     */
    @TableField("stock_warning")
    private Integer stockWarning;

    /**
     *
     * @return
     */
    @TableField(exist = false)
    private String[] specsValuesArr;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
