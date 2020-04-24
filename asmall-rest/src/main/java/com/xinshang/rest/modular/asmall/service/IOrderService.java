package com.xinshang.rest.modular.asmall.service;

import com.baomidou.mybatisplus.service.IService;
import com.xinshang.rest.common.util.R;
import com.xinshang.rest.modular.asmall.dto.AllinPayAsynResponseDTO;
import com.xinshang.rest.modular.asmall.dto.CreateOrderQueryDTO;
import com.xinshang.rest.modular.asmall.dto.OrderDTO;
import com.xinshang.rest.modular.asmall.dto.OrderListDTO;
import com.xinshang.rest.modular.asmall.model.Order;

import java.math.BigDecimal;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author wangxiaokun
 * @since 2019-10-17
 */
public interface IOrderService extends IService<Order> {

    /**
     * 功能描述: 完善订单发货信息
     *
     * @Param: [order]
     * @Return: boolean
     * @Auther: wangxiaokun
     * @Date: 2019/10/18 10:12
     * @Description:
     * @Modify:
     */
    boolean addDeliveryInfo(Order order);

    /**
     * 功能描述: 强制取消订单
     *
     * @Param: [orderId]
     * @Return: boolean
     * @Auther: wangxiaokun
     * @Date: 2019/10/18 10:33
     * @Description:
     * @Modify:
     */
    boolean cancel(String orderId);

    /**
     * 功能描述: H5端 创建订单
     *
     * @Param: [orderDTO]
     * @Return: boolean
     * @Auther: wangxiaokun
     * @Date: 2019/10/23 17:29
     * @Description:
     * @Modify:
     */
    R createOrder(CreateOrderQueryDTO createOrderQueryDTO);

    /**
     * 功能描述: 订单列表
     *
     * @Param: [orderDTO]
     * @Return: com.xinshang.rest.common.util.R
     * @Auther: wangxiaokun
     * @Date: 2019/11/18 11:48
     * @Description:
     * @Modify:
     */
    R orderList(OrderListDTO listDTO);

    /**
     * 功能描述: 商品支付
     *
     * @Param: [orderDTO]
     * @Return: com.xinshang.rest.common.util.R
     * @Auther: wangxiaokun
     * @Date: 2019/11/18 11:47
     * @Description:
     * @Modify:
     */
    R payGoods(OrderDTO orderDTO);

    /**
     * 功能描述: 支付确认
     *
     * @Param: [orderDTO]
     * @Return: com.xinshang.rest.common.util.R
     * @Auther: wangxiaokun
     * @Date: 2019/11/18 11:47
     * @Description:
     * @Modify:
     */
    R payConfirm(OrderDTO orderDTO);


    /**
     * 功能描述: 退款申请
     *
     * @Param: [orderDTO]
     * @Return: com.xinshang.rest.common.util.R
     * @Auther: wangxiaokun
     * @Date: 2019/11/18 11:08
     * @Description:
     * @Modify:
     */
    R payRefund(OrderDTO orderDTO);


    /**
     * 订单支付完成后逻辑操作 判断订单状态及减库存等
     * @param order
     * @param orderDTO
     * @return
     */
    R reduceOrder(Order order,OrderDTO orderDTO);

    /**
     * 获取总运费
     * @param createOrderQueryDTO
     * @return
     */
    BigDecimal getTotleFreight(CreateOrderQueryDTO createOrderQueryDTO);
}
