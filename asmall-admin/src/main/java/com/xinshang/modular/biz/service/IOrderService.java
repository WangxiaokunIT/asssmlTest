package com.xinshang.modular.biz.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.modular.biz.model.Order;
import com.baomidou.mybatisplus.service.IService;
import org.apache.poi.ss.formula.functions.T;

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
     * 功能描述: 强制取消订单 [不退款]
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
     * 功能描述: 强制取消订单 [退款]
     *
     * @Param: [orderId]
     * @Return: boolean
     * @Auther: wangxiaokun
     * @Date: 2019/10/18 10:33
     * @Description:
     * @Modify:
     */
    boolean cancelFreight(String orderId);

}
