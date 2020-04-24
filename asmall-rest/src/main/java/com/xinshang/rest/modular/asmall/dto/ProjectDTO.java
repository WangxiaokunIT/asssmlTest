package com.xinshang.rest.modular.asmall.dto;

import com.xinshang.rest.factory.PageFactory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 项目查询对象
 * @author lyk
 */
@Data
public class ProjectDTO extends PageFactory {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键")
    @NotNull
    private Long id;



}
