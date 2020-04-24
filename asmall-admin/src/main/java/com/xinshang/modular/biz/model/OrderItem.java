package com.xinshang.modular.biz.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 订单单项商品表
 * </p>
 *
 * @author wangxiaokun
 * @since 2019-10-17
 */
@TableName("tb_order_item")
@Data
public class OrderItem extends Model<OrderItem> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 订单id
     */
    @TableField("order_id")
    private String orderId;
    /**
     * 商品id
     */
    @TableField("item_id")
    private Long itemId;
    /**
     * 商品购买数量
     */
    private Integer num;
    /**
     * 商品标题
     */
    private String title;
    /**
     * 商品单价
     */
    private BigDecimal price;
    /**
     * 商品总金额
     */
    @TableField("total_fee")
    private BigDecimal totalFee;
    /**
     * 商品图片地址
     */
    @TableField("pic_path")
    private String picPath;

    /**
     * 规格
     */
    @TableField("specs_values")
    private String specsValues;

    /**
     * 规格id
     */
    @TableField("specs_id")
    private Integer specsId;

    /**
     * 规格编号
     */
    @TableField("item_specs_no")
    private String itemSpecsNo;

    /**
     * 商品编号
     */
    @TableField("item_number")
    private String itemNumber;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
