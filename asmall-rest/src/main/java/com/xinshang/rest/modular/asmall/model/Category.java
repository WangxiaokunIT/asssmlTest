package com.xinshang.rest.modular.asmall.model;


import lombok.Data;

/**
 * <p>
 * 商品分类
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-11-25
 */
@Data
public class Category {

    /**
     * id
     */
    private Integer id;
    /**
     * 上级分类
     */
    private Integer parentId;
    /**
     * 名称
     */
    private String name;
    /**
     * 图标
     */
    private String icon;
    /**
     * 排序
     */
    private Integer sortNum;
    /**
     * 级别
     */
    private Integer level;

    private String isOpen;
    /**
     * 索引值
     */
    private String seq;

}
