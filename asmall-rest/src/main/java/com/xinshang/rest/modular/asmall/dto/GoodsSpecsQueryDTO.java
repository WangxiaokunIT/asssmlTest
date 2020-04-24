package com.xinshang.rest.modular.asmall.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("商品规格查询信息")
public class GoodsSpecsQueryDTO implements Serializable {

    private static final long serialVersionUID = -9027159169518541793L;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品编码")
    private String itemNumber;

    /**
     * 规格属性
     */
    @ApiModelProperty(value = "规格属性" ,example="{\"内存\": \"64G\", \"颜色\": \"蓝色\"}")
    private String specs;

}
