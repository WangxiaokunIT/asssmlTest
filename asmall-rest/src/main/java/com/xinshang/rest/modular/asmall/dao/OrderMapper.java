package com.xinshang.rest.modular.asmall.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xinshang.rest.modular.asmall.model.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author wangxiaokun
 * @since 2019-10-17
 */
@Repository
public interface OrderMapper extends BaseMapper<Order> {


    /**
     * 根据产品编码和省获取运费
     * @param itemNumber
     * @param province
     * @return
     */
    BigDecimal getFreight(@Param("itemNumber") String itemNumber, @Param("province") String province);
}
