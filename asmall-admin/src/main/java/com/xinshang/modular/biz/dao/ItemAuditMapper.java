package com.xinshang.modular.biz.dao;

import com.xinshang.modular.biz.model.ItemAudit;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 * 商品发布审核表 Mapper 接口
 * </p>
 *
 * @author sunhao
 * @since 2019-10-18
 */
public interface ItemAuditMapper extends BaseMapper<ItemAudit> {


    ItemAudit selectByProductId(Long itemId);
}
