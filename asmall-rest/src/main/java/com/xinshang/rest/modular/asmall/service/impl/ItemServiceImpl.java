package com.xinshang.rest.modular.asmall.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.rest.modular.asmall.dao.ItemMapper;
import com.xinshang.rest.modular.asmall.dto.ItemAndSpecsDTO;
import com.xinshang.rest.modular.asmall.model.Item;
import com.xinshang.rest.modular.asmall.service.IItemService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author sunhao
 * @since 2019-10-17
 */
@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements IItemService {


    @Autowired
    private  ItemMapper itemMapper;

    @Override
    public boolean alertItemState(Long id, Integer state) {

        Item item = itemMapper.selectById(id);
        item.setStatus(state);

        item.setUpdated(new Date());

        if (itemMapper.updateById(item) != 1) {
            return false;
        }
       /* //删除购物车缓存信息
        deleteAllCartRedisByItemId(id);*/
        return true;
    }

    @Override
    public ItemAndSpecsDTO selectByItemNumberOrSpecsNo(String itemNumber, String specsNo) {
       return itemMapper.selectByItemNumberOrSpecsNo(itemNumber,specsNo);

    }
}
