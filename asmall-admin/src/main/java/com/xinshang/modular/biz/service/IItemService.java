package com.xinshang.modular.biz.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.modular.biz.dto.ItemDTO;
import com.xinshang.modular.biz.model.Item;
import com.baomidou.mybatisplus.service.IService;
import com.xinshang.modular.biz.vo.ItemVO;

import java.util.List;


/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author sunhao
 * @since 2019-10-17
 */
public interface IItemService extends IService<Item> {

    boolean alertItemState(Long id, Integer state, Integer bannerState) ;

    /**
     * 获取商品出售的数量
     * @return
     */
    List<ItemVO> showItemNum();

    /**
     * 添加商品
     * @param item
     */
    void add(Item item);

    /**
     * 修改商品
     * @param item
     */
    void update(Item item);

    /**
     * 分页查询商品
     * @param page
     * @param item
     * @return
     */
    Page<Item> selectItemPage(Page<Item> page, ItemDTO item);
}
