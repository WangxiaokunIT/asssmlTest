package com.xinshang.modular.biz.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.modular.biz.dto.ItemDTO;
import com.xinshang.modular.biz.model.Item;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xinshang.modular.biz.vo.ItemVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author sunhao
 * @since 2019-10-17
 */
public interface ItemMapper extends BaseMapper<Item> {

    /**
     * 获取商品出售的数量
     * @return
     */
    List<ItemVO> showItemNum();

    /**
     * 分页查询商品
     * @param page
     * @param item
     * @return
     */
    List<Item> selectItemPage(@Param("page")Page<Item> page, @Param("item") ItemDTO item);
}
