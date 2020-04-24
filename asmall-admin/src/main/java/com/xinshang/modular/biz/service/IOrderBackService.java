package com.xinshang.modular.biz.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.modular.biz.model.OrderBack;
import com.baomidou.mybatisplus.service.IService;
import org.apache.poi.ss.formula.functions.T;

/**
 * <p>
 * 订单退货记录表 服务类
 * </p>
 *
 * @author daijunye
 * @since 2019-10-23
 */
public interface IOrderBackService extends IService<OrderBack> {
    public String refundConfirm(OrderBack orderDTO) ;
    public  void addOpt(String orderId);

}
