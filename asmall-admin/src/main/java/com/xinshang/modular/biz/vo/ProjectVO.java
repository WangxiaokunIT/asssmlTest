package com.xinshang.modular.biz.vo;

import cn.hutool.db.DaoTemplate;
import com.xinshang.modular.biz.model.Project;
import lombok.Data;

import java.util.Date;

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
     * 份数
     */
    private String numberOfCopies;

    /**
     * 总份数
     */
    private String totalNumber;

    /**
     * 系统时间
     */
    private Date systemTime = new Date();
}
