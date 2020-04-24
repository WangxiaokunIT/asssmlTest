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
import java.math.BigDecimal;

/**
 * <p>
 * 订单单项商品表
 * </p>
 *
 * @author wangxiaokun
 * @since 2019-10-17
 */
@TableName("tb_order_item")
@ApiModel("订单单项商品")
@Data
public class OrderItem extends Model<OrderItem> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "订单单项id",required=true)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 订单id
     */
    @TableField("order_id")
    @ApiModelProperty(value = "订单ID")
    private String orderId;
    /**
     * 商品id
     */
    @TableField("item_id")
    @ApiModelProperty(value = "商品ID")
    private Long itemId;

    /**
     * 商品id
     */
    @TableField("item_number")
    @ApiModelProperty(value = "商品编号")
    private String itemNumber;

    /**
     * 商品购买数量
     */
    @ApiModelProperty(value = "商品购买数量")
    private Integer num;
    /**
     * 商品标题
     */
    @ApiModelProperty(value = "商品标题")
    private String title;
    /**
     * 商品单价
     */
    @ApiModelProperty(value = "商品单价")
    private BigDecimal price;
    /**
     * 商品总金额
     */
    @ApiModelProperty(value = "商品总金额")
    @TableField("total_fee")
    private BigDecimal totalFee;
    /**
     * 商品图片地址
     */
    @TableField("pic_path")
    @ApiModelProperty(value = "商品图片地址")
    private String picPath;

    /**
     * 规格编号
     */
    @TableField("item_specs_no")
    @ApiModelProperty(value = "规格编号")
    private String itemSpecsNo;

    /**
     * 规格
     */
    @TableField("specs_values")
    @ApiModelProperty(value = "规格")
    private String specsValues;

    @TableField("specs_id")
    private Integer specsId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
        "id=" + id +
        ", orderId=" + orderId +
        ", itemId=" + itemId +
        ", num=" + num +
        ", title=" + title +
        ", price=" + price +
        ", totalFee=" + totalFee +
        ", picPath=" + picPath +
        "}";
    }
}
