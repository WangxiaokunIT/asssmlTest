package com.xinshang.rest.modular.asmall.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 规格属性名
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-11-25
 */
@Data
@ApiModel("属性值信息")
public class SpecsAttributeValueVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "所属属性")
    private Integer pid;
    /**
     * 属性值
     */
    @ApiModelProperty(value = "属性值")
    private String attributeValue;



}
