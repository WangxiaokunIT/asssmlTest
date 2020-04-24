package com.xinshang.rest.modular.asmall.service;

import com.baomidou.mybatisplus.service.IService;
import com.xinshang.rest.modular.asmall.dto.ItemAndSpecsDTO;
import com.xinshang.rest.modular.asmall.model.Item;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author sunhao
 * @since 2019-10-17
 */
public interface IItemService extends IService<Item> {

    boolean alertItemState(Long id, Integer state) ;

    /**
     * 根据商品编号或者规格编号查询商品信息
     * @param itemNumber
     * @param specsNo
     * @return
     */
    ItemAndSpecsDTO selectByItemNumberOrSpecsNo(String itemNumber,String specsNo);

}
