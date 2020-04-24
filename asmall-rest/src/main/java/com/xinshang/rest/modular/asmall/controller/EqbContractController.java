package com.xinshang.rest.modular.asmall.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xinshang.rest.common.util.JwtTokenUtil;
import com.xinshang.rest.common.util.R;
import com.xinshang.rest.common.util.wk.helper.exception.DefineException;
import com.xinshang.rest.config.properties.OssProperties;
import com.xinshang.rest.modular.asmall.dto.ContractViewRequestDTO;
import com.xinshang.rest.modular.asmall.dto.CreateContractRequestDTO;
import com.xinshang.rest.modular.asmall.model.EqbContract;
import com.xinshang.rest.modular.asmall.model.EqbDTO;
import com.xinshang.rest.modular.asmall.model.Member;
import com.xinshang.rest.modular.asmall.service.IEqbContractService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * e签宝合同
 * @author lyk
 */
@RestController
@RequestMapping("/eqbContract")
@Api(value = "合同管理",tags = "合同相关接口")
@Slf4j
public class EqbContractController {

    @Autowired
    private  OssProperties ossProperties;

    @Autowired
    private IEqbContractService eqbContractService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;



    /**
     * 查询合同
     * @param contractViewRequestDTO
     * @return
     */
    @ApiOperation(value = "获取合同详情")
    @PostMapping("contractView")
    public R contractView(HttpServletRequest request ,@RequestBody ContractViewRequestDTO contractViewRequestDTO) {
        log.info("获取合同详情");
       // Member member = jwtTokenUtil.getMemberFromRequest(request);
        EntityWrapper<EqbContract> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("join_id",contractViewRequestDTO.getJoinId());
        //根据项目id+客户id+合同类型 查询合同
        EqbContract eqbContract = eqbContractService.selectOne(entityWrapper);
        if(eqbContract == null){
            return  R.failed();
        }
        return R.ok(eqbContract);
    }

    @ApiOperation(value = "创建合同")
    @PostMapping("createContract")
    public R createContract(HttpServletRequest request ,@RequestBody CreateContractRequestDTO createContractRequestDTO) {
        log.info("创建合同");
        Member member = jwtTokenUtil.getMemberFromRequest(request);
        EqbContract eqbContract = eqbContractService.createContract(createContractRequestDTO,member);
        if(eqbContract == null){
            return R.failed();
        }
        return R.ok(eqbContract);
    }

    @ApiOperation(value = "合同异步通知")
    @RequestMapping("contractCallBack")
    public R contractCallBack(@RequestBody EqbDTO eqbDTO) {
        log.info("合同异步回调");

        boolean eqbContract = false;
        try {
            eqbContract = eqbContractService.eqbCallBack(eqbDTO, ossProperties);
        }catch (Exception e){
            log.info(e.getMessage());
            return R.failed();
        }
        if(!eqbContract){
            return R.failed();
        }
        return R.ok();
    }

    @ApiOperation(value = "查询合同状态")
    @GetMapping("queryStatus/{signId}")
    public R queryStatus(@PathVariable("signId")String signId) {
        log.info("创建合同");
        EntityWrapper<EqbContract> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("sign_id",signId);
        EqbContract eqbContract = eqbContractService.selectOne(entityWrapper);
        if(eqbContract == null){
            return R.failed();
        }
        return R.ok(eqbContract);
    }
}
