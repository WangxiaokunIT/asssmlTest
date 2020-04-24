package com.xinshang.rest.modular.asmall.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xinshang.rest.modular.asmall.dto.ItemAndSpecsDTO;
import com.xinshang.rest.modular.asmall.model.Item;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author sunhao
 * @since 2019-10-17
 */
@Repository
public interface ItemMapper extends BaseMapper<Item> {

    /**
     * 根据产品编号和规格编号查询商品信息
     * @param itemNumber
     * @param specsNo
     * @return
     */
    ItemAndSpecsDTO selectByItemNumberOrSpecsNo(@Param("itemNumber") String itemNumber, @Param("specsNo") String specsNo);
}
