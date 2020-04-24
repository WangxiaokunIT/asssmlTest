package com.xinshang.rest.modular.asmall.dto;

import com.xinshang.rest.factory.PageFactory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhangjiajia
 * @since 2019年11月28日13:03:49
 */
@Data
@ApiModel("商品查询信息")
public class ItemQueryDTO extends PageFactory {

    /**
     * 商品标题
     */
    @ApiModelProperty(value = "商品名称")
    private String title;

    /**
     * 商品类型
     */
    @ApiModelProperty(value = "商品类型")
    private Integer cid;

    /**
     * 售卖数量限制
     */
    @ApiModelProperty(value = "商品分类")
    private Integer categoryId;

}
