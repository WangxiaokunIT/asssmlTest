package com.xinshang.rest.modular.asmall.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.rest.common.enums.BizExceptionEnum;
import com.xinshang.rest.common.util.JwtTokenUtil;
import com.xinshang.rest.common.util.R;

import com.xinshang.rest.factory.PageFactory;
import com.xinshang.rest.modular.asmall.dto.*;
import com.xinshang.rest.modular.asmall.model.*;
import com.xinshang.rest.modular.asmall.service.*;
import com.xinshang.rest.modular.asmall.vo.JoininVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 加盟管理
 * @author zhoushuai
 */
@RestController
@RequestMapping("/joinin")
@Slf4j
@Api(value = "加盟管理",tags = "加盟相关接口")
public class JoininController {

    @Autowired
    private IJoininService ijoininService;

    @Autowired
    private IProjectService iprojectService;

    @Autowired
    private IRepayPlanService iRepayPlanService;

    @Autowired
    private IEqbContractService iEqbContractService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @ApiOperation(value = "获取加盟列表", notes = "获取所有加盟列表", nickname = "liukx")
    @PostMapping("showJoinin")
    public R showProject(HttpServletRequest request) {

        Page<JoininVO> page = new Page<>((1 / 1000 + 1), 1000);
        Member member = jwtTokenUtil.getMemberFromRequest(request);
        if(member==null){
            return R.failed(BizExceptionEnum.USER_DOES_NOT_EXIST.getMessage());
        }else {
            page.setRecords(ijoininService.showJoinin(member.getId(),page));
            return R.ok(page);
        }
    }

    @ApiOperation(value = "获取加盟详情", notes = "获取所有加盟详情", nickname = "liukx")
    @PostMapping("showJoininDetail")
    public R showJoininDetail( @RequestBody JoininDTO joininDto,HttpServletRequest request) {
         Member member = jwtTokenUtil.getMemberFromRequest(request);
        if(member==null){
            return R.failed(BizExceptionEnum.USER_DOES_NOT_EXIST.getMessage());
        }else {
            EntityWrapper<RepayPlan> wrapper = new EntityWrapper<>();
            wrapper.eq("member_id",member.getId());
            wrapper.eq("project_id",joininDto.getProjectId());
            wrapper.eq("is_delete",0);
            List<RepayPlan> joinin= iRepayPlanService.selectList(wrapper);

             return R.ok(joinin);
        }
    }

    @ApiOperation(value = "获取合同地址")
    @PostMapping("showContractUrl")
    public R showContractUrl(@RequestBody JoininDTO joininDto,HttpServletRequest request) {
        Member member = jwtTokenUtil.getMemberFromRequest(request);
        if(member==null){
            return R.failed(BizExceptionEnum.USER_DOES_NOT_EXIST.getMessage());
        }else {
            EntityWrapper<EqbContract> wrapper = new EntityWrapper<>();
            wrapper.eq("member_id",member.getId());
            wrapper.eq("project_id",joininDto.getProjectId());
            EqbContract eqbContract= iEqbContractService.selectOne(wrapper);

            return R.ok(eqbContract);
        }
    }



    @ApiOperation(value = "添加加盟信息", notes = "添加加盟信息", nickname = "liukx")
    @PostMapping("addJoinin")
    public R addJoinin(@RequestBody JoininDTO joininDto, HttpServletRequest request) {
        Member member = jwtTokenUtil.getMemberFromRequest(request);

        if(member==null){
            return R.failed(BizExceptionEnum.USER_DOES_NOT_EXIST.getMessage());
        }else{
            joininDto.setCustomId(member.getId());
            if(member.getState()==0){
                return R.failed(BizExceptionEnum.USER_IS_NOT_ALLOWED_TO_LOG_IN.getMessage());
            }
        }
        synchronized (this){

            if(joininDto.getProjectId()==null){
                return R.failed(400,"招募ID不能为空");
            }
            Project project=iprojectService.selectById(joininDto.getProjectId());
            if(project==null){
                return R.failed(400,"招募信息不存在");
            }else{
                if(project.getState()!=2){
                    return R.failed(400,"招募结束");
                }
            }
            if(joininDto.getInvestmentAmount()==null){
                return R.failed(400,"投资金额不能为空");
            }

            List<Joinin> joinList=ijoininService.selectList(new EntityWrapper<Joinin>().eq("project_id", joininDto.getProjectId()));
            BigDecimal investmentAmount= new BigDecimal("0");
            for(int i=0;i<joinList.size();i++){
                BigDecimal investmentAmount1=joinList.get(i).getInvestmentAmount();
                investmentAmount=investmentAmount.add(investmentAmount1);
            }
            //剩余投资金额
            BigDecimal syInvestmentAmount=project.getMaxMoney().subtract(investmentAmount);
            if(syInvestmentAmount.compareTo(joininDto.getInvestmentAmount()) == -1){
                return R.failed(400,"投资金额不能大于剩余金额");
            }
        }
            R r=ijoininService.payGoods(joininDto);

        return r;

    }


    /**
     *  确认支付（后台确认+验证码）
     */
    @ApiOperation(value = "加盟确认支付", notes = "加盟确认支付【后台确认+验证码】")
    @PostMapping("/payConfirm")
    public R payConfirm(@RequestBody JoininDTO joininDTO, HttpServletRequest request) {
        Member member = jwtTokenUtil.getMemberFromRequest(request);
        if (member == null) {
            return R.failed("用户不存在");
        }
        joininDTO.setCustomId(member.getId());
        R r = ijoininService.payConfirm(joininDTO);
        return r;
    }

    @ApiOperation(value = "预计收益")
    @PostMapping("joinInner")
    public R joinInner(@RequestBody JoinInner joinInner) {
        R r=ijoininService.joinInner(joinInner);
        return r;
    }



}
