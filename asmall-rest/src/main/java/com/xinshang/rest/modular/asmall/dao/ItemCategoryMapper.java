package com.xinshang.rest.modular.asmall.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.rest.modular.asmall.model.Category;
import com.xinshang.rest.modular.asmall.model.ItemCategory;

import java.util.Calendar;
import java.util.List;

/**
 * <p>
 * 商品分类 Mapper 接口
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-11-25
 */
public interface ItemCategoryMapper extends BaseMapper<ItemCategory> {

    /**
     * 加载分类树
     * @return
     */
    List<Category> loadTree();
}
