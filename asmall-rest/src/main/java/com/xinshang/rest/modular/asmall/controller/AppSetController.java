package com.xinshang.rest.modular.asmall.controller;

import com.xinshang.rest.common.util.R;
import com.xinshang.rest.modular.asmall.model.AppSet;
import com.xinshang.rest.modular.asmall.service.IAppSetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * app信息
 *
 * @author zhangjiajia
 */
@RestController
@RequestMapping("/appset")
@Api(value = "app设置相关接口",tags = "app设置相关接口")
@AllArgsConstructor
public class AppSetController {

    private IAppSetService iAppSetService;

    /**
     * 获取平台协议
     * @return
     */
    @ApiOperation(value = "获取平台协议", notes = "获取平台协议")
    @GetMapping("/user_agreement")
    public R<String> getUserAgreement() {
        AppSet appSet = iAppSetService.selectById(1);
        return R.ok(appSet.getUserAgreement());
    }

    /**
     * 获取隐私协议
     * @return
     */
    @ApiOperation(value = "获取隐私协议", notes = "获取隐私协议")
    @GetMapping("/privacy_protocol")
    public R<String> getPrivacyProtocol() {
        AppSet appSet = iAppSetService.selectById(1);
        return R.ok(appSet.getPrivacyProtocol());
    }

    /**
     * 获取代理协议
     * @return
     */
    @ApiOperation(value = "获取代理协议", notes = "获取代理协议")
    @GetMapping("/agent_protocol")
    public R<String> getAgentProtocol() {
        AppSet appSet = iAppSetService.selectById(1);
        return R.ok(appSet.getAgentProtocol());
    }

    /**
     * 获取vip权益
     * @return
     */
    @ApiOperation(value = "获取VIP权益", notes = "获取VIP权益")
    @GetMapping("/vip_description")
    public R<String> getVipDescription() {
        AppSet appSet = iAppSetService.selectById(1);
        return R.ok(appSet.getVipDescription());
    }

    /**
     * 获取代理权益
     * @return
     */
    @ApiOperation(value = "获取代理权益", notes = "获取代理权益")
    @GetMapping("/agent_description")
    public R<String> getAgentDescription() {
        AppSet appSet = iAppSetService.selectById(1);
        return R.ok(appSet.getAgentDescription());
    }


}
