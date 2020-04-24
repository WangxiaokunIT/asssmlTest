package com.xinshang.rest.modular.asmall.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * @author wangxiaokun
 */
@Data
@ApiModel("要购买商品")
public class OrderBuyGoodsDTO implements Serializable {

    /**商品编号**/
    @ApiModelProperty(value = "商品编号")
    private String itemNumber;

    /**规格编号**/
    @ApiModelProperty(value = "规格编号")
    private String itemSpecsNo;

    /**商品数量**/
    @ApiModelProperty(value = "商品数量")
    private Integer productNum;


}
