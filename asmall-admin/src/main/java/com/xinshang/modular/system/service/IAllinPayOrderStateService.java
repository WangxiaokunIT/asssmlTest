package com.xinshang.modular.system.service;


import com.xinshang.modular.biz.dto.AllinPaySyncOrderStateDTO;

/**
 * 通联订单状态服务
 *
 * @author fengshuonan
 * @date 2017-04-27 17:00
 */
public interface IAllinPayOrderStateService {


    /**
     * 查询订单状态
     * @param bizOrderNo
     * @return
     */
    AllinPaySyncOrderStateDTO getOrderDetail(String bizOrderNo);
}
