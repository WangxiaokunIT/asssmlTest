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
@ApiModel("属性信息")
public class SpecsAttributeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "属性id")
    private Integer id;
    /**
     * 属性名
     */
    @ApiModelProperty(value = "属性名")
    private String attributeName;



}
