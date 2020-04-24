package com.xinshang.rest.modular.asmall.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author wangxiaokun
 */
@Data
@ApiModel("购物车商品")
public class CartProduct implements Serializable {

    /**商品编号**/
    @ApiModelProperty(value = "商品编号")
    private String itemNumber;

    /**规格编号**/
    @ApiModelProperty(value = "规格编号")
    private String itemSpecsNo;

    /**商品数量**/
    @ApiModelProperty(value = "商品数量")
    private Integer productNum;

    /**商品名称**/
    @ApiModelProperty(value = "商品名称")
    private String productName;

    /**商品图片**/
    @ApiModelProperty(value = "商品图片")
    private String productImg;

    /**普通价格**/
    @ApiModelProperty(value = "普通价格")
    private BigDecimal price;

    /**
     * VIP价格
     */
    @ApiModelProperty(value = "VIP价格")
    private BigDecimal vipDiscount;

    /**
     * 商品规格
     */
    @ApiModelProperty(value = "商品规格")
    private String itemSpecsValues;

    /**
     * 运费
     */
    @ApiModelProperty(value = "运费")
    private BigDecimal freight;
}
