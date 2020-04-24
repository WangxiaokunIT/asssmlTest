package com.xinshang.rest.modular.asmall.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;

/**
 * <p>
 * 商品规格
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-11-25
 */
@Data
@ApiModel("商品规格信息")
public class ItemSpecsVO{

    private static final long serialVersionUID = 1L;

    /**
     * 产品编码
     */
    @ApiModelProperty(value = "产品编码")
    private String itemNo;
    /**
     * 规格编码
     */
    @ApiModelProperty(value = "规格编码")
    private String specsNo;
    /**
     * 规格
     */
    @ApiModelProperty(value = "规格")
    private String specsValues;

    /**
     * 库存
     */
    @ApiModelProperty(value = "库存")
    private Integer stock;
    /**
     * 价格
     */
    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    /**
     * VIP价格
     */
    @ApiModelProperty(value = "VIP价格")
    private BigDecimal vipDiscount;

    /**
     * 规格图片
     */
    private String[] images;




}
