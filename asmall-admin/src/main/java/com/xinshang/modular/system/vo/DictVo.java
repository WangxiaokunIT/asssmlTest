package com.xinshang.modular.system.vo;

import lombok.Data;

/**
 * @author lvyingkai
 * @date 2018年11月23日 11:18:36
 * 字典表Vo
 */
@Data
public class DictVo {
    /**
     * 主键id
     */
    private Integer id;
    /**
     * 排序
     */
    private Integer num;
    /**
     * 父级字典
     */
    private Integer pId;
    /**
     * 名称
     */
    private String name;
    /**
     * 编码
     */
    private String code;
    /**
     * 备注
     */
    private String remark;
    /**
     * 父code
     */
    private String pidCode;

}
