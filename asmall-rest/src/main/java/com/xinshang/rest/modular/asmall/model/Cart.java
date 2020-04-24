package com.xinshang.rest.modular.asmall.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Exrickx
 */
@ApiModel("购物车")
@Data
public class Cart implements Serializable {


    /**
     * 产品编号
     */
    @ApiModelProperty(value = "产品编号",required=true)
    private String itemNumber;

    /**规格编号**/
    @ApiModelProperty(value = "规格编号")
    private String itemSpecsNo;

    /**
     * 全选
     */
    @ApiModelProperty(value = "全选")
    private String checked;

    /**
     * 产品数量
     */
    @ApiModelProperty(value = "产品数量",required=true)
    private int productNum;

    /**
     * 是否选中
     */
    @ApiModelProperty(value = "是否选中")
    private Boolean selected;

    /**
     * 邮费
     */
    @ApiModelProperty(value = "邮费")
    private Integer freight;

    /**
     * 客户id
     */
    private Long memberId;

}
