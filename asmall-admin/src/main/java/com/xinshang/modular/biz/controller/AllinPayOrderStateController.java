package com.xinshang.modular.biz.controller;


import com.xinshang.core.base.controller.BaseController;
import com.xinshang.modular.biz.dto.AllinPaySyncOrderStateDTO;
import com.xinshang.modular.system.service.IAllinPayOrderStateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @title:通联订单状态管理
 *
 * @author: zhangjiajia
 * @since: 2019-11-12 14:40:57
 */
@Controller
@RequestMapping("/allinPayOrderState")
@AllArgsConstructor
public class AllinPayOrderStateController extends BaseController {

    private final IAllinPayOrderStateService iAllinPayOrderStateService;

    /**
     * 查询通联订单状态
     */
    @RequestMapping(value = "/getOrderDetail/{bizOrderNo}")
    @ResponseBody
    public AllinPaySyncOrderStateDTO getOrderDetail(@PathVariable("bizOrderNo") String bizOrderNo) {
       return iAllinPayOrderStateService.getOrderDetail(bizOrderNo);
    }

}
