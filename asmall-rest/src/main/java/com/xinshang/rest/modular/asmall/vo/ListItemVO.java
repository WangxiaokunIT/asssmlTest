package com.xinshang.rest.modular.asmall.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * @author sunha
 * @since 2019/10/1818:02
 */
@Data
@ApiModel("列表商品信息")
public class ListItemVO implements Serializable {

    private static final long serialVersionUID = -7901695046500633803L;
    /**
     * 商品标题
     */
    @ApiModelProperty(value = "商品标题")
    private String title;

    /**
     * 商品标题
     */
    @ApiModelProperty(value = "商品编号")
    private String itemNumber;

    /**
     * 商品描述
     */
    @ApiModelProperty(value = "商品简述")
    private String sellPoint;

    /**
     * 商品价格
     */
    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;

    /**
     * VIP价格
     */
    @ApiModelProperty(value = "VIP价格")
    private BigDecimal vipDiscount;

    /**
     * 销量
     */
    @ApiModelProperty(value = "销量")
    private Integer numAll;

    /**
     * 图片
     */
    @ApiModelProperty(value = "商品图片")
    private String image;



}
