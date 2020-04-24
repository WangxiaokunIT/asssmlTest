package com.xinshang.rest.modular.asmall.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.rest.common.util.JwtTokenUtil;
import com.xinshang.rest.common.util.R;
import com.xinshang.rest.modular.asmall.dto.LogDTO;
import com.xinshang.rest.modular.asmall.dto.ProjectDTO;
import com.xinshang.rest.modular.asmall.model.ClientLogMoneys;
import com.xinshang.rest.modular.asmall.model.IntegralLogMoneys;
import com.xinshang.rest.modular.asmall.model.Member;
import com.xinshang.rest.modular.asmall.model.Project;
import com.xinshang.rest.modular.asmall.service.IIntegralLogMoneysService;
import com.xinshang.rest.modular.asmall.service.ILogService;
import com.xinshang.rest.modular.asmall.service.IProjectService;
import com.xinshang.rest.modular.asmall.vo.ProjectVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 资金流水
 * @author jyz
 */
@RestController
@RequestMapping("/log")
@Api(value = "资金流水管理",tags = "资金流水相关接口")
@Slf4j
public class LogController {

    @Autowired
    private ILogService logService;
    @Autowired
    private IIntegralLogMoneysService iIntegralLogMoneysService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @ApiOperation(value = "获取客户资金流水列表")
    @PostMapping("showClientLog")
    public R showClientLog(@RequestBody LogDTO logDTO, HttpServletRequest request) {
        log.info("获取客户资金流水列表参数:{}",logDTO);
        Member member = jwtTokenUtil.getMemberFromRequest(request);
        if (member == null) {
            return R.failed("用户不存在");
        }
        logDTO.setBizUserId(member.getBizUserId());
        return R.ok(logService.showClientLog(logDTO));

    }

    @ApiOperation(value = "获取客户积分流水列表")
    @PostMapping("showIntergraLog")
    public R showIntergraLog(@RequestBody LogDTO logDTO, HttpServletRequest request) {
        log.info("获取客户积分流水列表参数:{}",logDTO);
        Member member = jwtTokenUtil.getMemberFromRequest(request);
        Page<IntegralLogMoneys> page = new Page<>((logDTO.getOffset() / logDTO.getLimit() + 1), logDTO.getLimit());
        if (member == null) {
            return R.failed("用户不存在");
        }
        logDTO.setClientId(member.getId());
        page.setRecords(iIntegralLogMoneysService.showIntergraLog(logDTO, page));
        return R.ok(page);
    }
}
