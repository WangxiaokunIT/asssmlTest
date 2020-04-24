package com.xinshang.rest.modular.asmall.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.rest.modular.asmall.dao.OrderItemMapper;
import com.xinshang.rest.modular.asmall.model.OrderItem;
import com.xinshang.rest.modular.asmall.service.IOrderItemService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单单项商品表 服务实现类
 * </p>
 *
 * @author wangxiaokun
 * @since 2019-10-17
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements IOrderItemService {

}
