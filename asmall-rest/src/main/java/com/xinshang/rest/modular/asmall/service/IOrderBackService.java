package com.xinshang.rest.modular.asmall.service;

import com.baomidou.mybatisplus.service.IService;
import com.xinshang.rest.common.util.R;
import com.xinshang.rest.modular.asmall.dto.OrderBackDTO;
import com.xinshang.rest.modular.asmall.dto.OrderDTO;
import com.xinshang.rest.modular.asmall.dto.OrderListDTO;
import com.xinshang.rest.modular.asmall.model.Order;
import com.xinshang.rest.modular.asmall.model.OrderBack;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author daijunye
 * @since 2019-10-17
 */
public interface IOrderBackService extends IService<OrderBack> {

    /**
     * 功能描述: 完善订单发货信息
     *
     * @Param: [order]
     * @Return: boolean
     * @Auther: daijunye
     * @Date: 2019/10/18 10:12
     * @Description:
     * @Modify:
     */


    /**
     * 功能描述: 强制取消订单
     *
     * @Param: [orderId]
     * @Return: boolean
     * @Auther: daijunye
     * @Date: 2019/10/18 10:33
     * @Description:
     * @Modify:
     */
    boolean cancel(String orderId);

    R createOrder(OrderBackDTO orderbackDTO);
    R orderBackOrderId(OrderBackDTO orderbackDTO);
    R orderList(OrderListDTO orderDTO);
}
