package com.xinshang.rest.modular.asmall.controller;

import com.xinshang.rest.common.util.JwtTokenUtil;
import com.xinshang.rest.common.util.R;
import com.xinshang.rest.modular.asmall.model.Member;
import com.xinshang.rest.modular.asmall.service.PersonalComputeService;
import com.xinshang.rest.modular.asmall.vo.PCMemberVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lyk
 */
@RestController
@RequestMapping("/pc")
@Api(value = "PC端接口",tags = "PC端使用的接口")
public class PersonalComputeController {

    @Autowired
    private PersonalComputeService personalComputeService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @ApiOperation(value = "获取用户信息")
    @PostMapping("/showMember")
    public R<PCMemberVO> showMember(HttpServletRequest request) {
        Member member = jwtTokenUtil.getMemberFromRequest(request);
        if (member == null) {
            return R.failed("用户不存在");
        }
        return R.ok(personalComputeService.showMember(member.getId()));
    }

}

