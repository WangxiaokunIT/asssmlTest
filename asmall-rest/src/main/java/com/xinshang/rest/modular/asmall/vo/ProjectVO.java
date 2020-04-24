package com.xinshang.rest.modular.asmall.vo;

import com.xinshang.rest.modular.asmall.model.Project;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 项目增强显示对象
 * @author lyk
 */
@Data
public class ProjectVO extends Project {
    /**
     * 创建人姓名
     */
    private String createUserName;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 归还方式
     */
    private String repaymentMethodName;

    /**
     * 投资总金额
     */
    private BigDecimal investmentAmount;
}
