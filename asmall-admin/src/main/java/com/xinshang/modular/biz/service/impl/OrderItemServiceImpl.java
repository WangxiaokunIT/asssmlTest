package com.xinshang.modular.biz.service.impl;

import com.xinshang.modular.biz.model.OrderItem;
import com.xinshang.modular.biz.dao.OrderItemMapper;
import com.xinshang.modular.biz.service.IOrderItemService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
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
