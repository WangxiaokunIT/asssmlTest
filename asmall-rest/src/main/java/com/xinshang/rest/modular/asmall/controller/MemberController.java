package com.xinshang.rest.modular.asmall.controller;

import cn.hutool.core.util.ReUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xinshang.rest.common.enums.BizExceptionEnum;
import com.xinshang.rest.common.util.FileUtil;
import com.xinshang.rest.common.util.JwtTokenUtil;
import com.xinshang.rest.common.util.R;
import com.xinshang.rest.config.constant.Constants;
import com.xinshang.rest.config.properties.OssProperties;
import com.xinshang.rest.modular.asmall.dto.*;
import com.xinshang.rest.modular.asmall.model.Bank;
import com.xinshang.rest.modular.asmall.model.Member;
import com.xinshang.rest.modular.asmall.service.IMemberService;
import com.xinshang.rest.modular.asmall.service.ISendMsgService;
import com.xinshang.rest.modular.asmall.vo.BankVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;


/**
 * @author sunha
 * @since 2019/10/249:32
 */
@RestController
@RequestMapping("/member")
@Api(value = "客户管理", tags = "客户相关接口")
@AllArgsConstructor
@Slf4j
public class MemberController {

    private final IMemberService memberService;
    private final JwtTokenUtil tokenUtil;
    private final OssProperties ossProperties;
    private final ISendMsgService iSendMsgService;
    private final JwtTokenUtil jwtTokenUtil;

    @ApiOperation(value = "根据ID查询用户信息")
    @PostMapping("getMemberById")
    public R<BankVO> getMemberById(HttpServletRequest request) {
        Member member = tokenUtil.getMemberFromRequest(request);
        BankVO bankVO = memberService.selectDetailbyId(member.getId());

        if (member != null) {
            return R.ok(bankVO);
        } else {
            return R.failed(500, "获取用户信息失败");
        }
    }

    @ApiOperation(value = "用户头像上传")
    @PostMapping("imageUpload")
    public R<Object> imageUpload(@RequestParam("file") MultipartFile file) {
        Map<String, Object> map = FileUtil.uploadFilePath(file, ossProperties);
        return R.ok(map);
    }

    /**
     * 补全会员信息+实名认证
     * @param request
     * @param dto
     * @return
     */
    @ApiOperation(value = "补全会员信息")
    @PostMapping("update")
    public R<Object> updateMember(HttpServletRequest request, @RequestBody MemberDTO dto) {
        Member member = tokenUtil.getMemberFromRequest(request);
        return memberService.updateMember(member, dto);
    }

    @ApiOperation(value = "申请成为会员")
    @PostMapping("apply")
    public R<Object> apply(HttpServletRequest request) {
        Member member = tokenUtil.getMemberFromRequest(request);
        if (member.getAuditStatus()==0){
            return R.failed("正在审核中");
        }
        EntityWrapper<Member> wrapper = new EntityWrapper<>();
        member.setAuditStatus(0);
        wrapper.eq("username", member.getUsername());
        boolean update = memberService.update(member, wrapper);
        if (update) {
            return R.ok("申请成功");
        } else {
            return R.failed("申请失败");
        }
    }

    /**
     * 通联短信验证码
     *
     * @param allinPaySendSmsCaptchaDTO
     * @return
     */
    @ApiOperation(value = "发送通联验证码", notes = "发送通联验证码", consumes = "application/json")
    @PostMapping("/smsAllinPayCaptcha")
    public R<String> SendAllinPaySMSCaptcha(@RequestBody AllinPaySendSmsCaptchaDTO allinPaySendSmsCaptchaDTO, HttpServletRequest request) {
        //验证手机号格式
        Assert.isTrue(ReUtil.isMatch("^1\\d{10}$", allinPaySendSmsCaptchaDTO.getPhone()), BizExceptionEnum.PHONE_NO_VALIDATION_FAILED.getMessage());
        //短信类型
        Assert.isTrue(ReUtil.isMatch("^[6|9]{1}$", allinPaySendSmsCaptchaDTO.getVerificationCodeType()), BizExceptionEnum.SMS_SEND_TYPE_FAILED.getMessage());
        Member member = tokenUtil.getMemberFromRequest(request);
        allinPaySendSmsCaptchaDTO.setBizUserId(member.getBizUserId());
        return iSendMsgService.sendAllinPaySmsCaptcha(allinPaySendSmsCaptchaDTO);
    }

    /**
     * 通联绑定手机
     *
     * @param allinPayBindingPhoneDTO
     * @return
     */
    @ApiOperation(value = "通联绑定手机", notes = "通联绑定手机", consumes = "application/json")
    @PostMapping("/allinPayBindingPhone")
    public R<String> allinPayBindingPhone(@RequestBody AllinPayBindingPhoneDTO allinPayBindingPhoneDTO, HttpServletRequest request) {
        //验证手机号格式
        Assert.isTrue(ReUtil.isMatch("^1\\d{10}$", allinPayBindingPhoneDTO.getPhone()), BizExceptionEnum.PHONE_NO_VALIDATION_FAILED.getMessage());
        //验证码格式
        Assert.isTrue(ReUtil.isMatch("^\\d{6}$", allinPayBindingPhoneDTO.getVerificationCode()), BizExceptionEnum.VERIFICATION_FORMAT_ERROR.getMessage());
        Member member = tokenUtil.getMemberFromRequest(request);
        allinPayBindingPhoneDTO.setBizUserId(member.getBizUserId());
        return iSendMsgService.allinPayBindingPhone(allinPayBindingPhoneDTO);
    }

    /**
     * 通联解除绑定手机
     *
     * @param allinPayBindingPhoneDTO
     * @return
     */
    @ApiOperation(value = "通联解除绑定手机", notes = "通联绑定手机", consumes = "application/json")
    @PostMapping("/allinPayUnBindingPhone")
    public R<String> allinPayUnBindingPhone(@RequestBody AllinPayBindingPhoneDTO allinPayBindingPhoneDTO, HttpServletRequest request) {
        Member member = tokenUtil.getMemberFromRequest(request);
        allinPayBindingPhoneDTO.setBizUserId(member.getBizUserId());
        return iSendMsgService.allinPayUnBindingPhone(allinPayBindingPhoneDTO);
    }

    @ApiOperation(value = "请求绑定银行卡")
    @PostMapping("applyBindBankCard")
    public R<Object> applyBindBankCard(HttpServletRequest request, @RequestBody BindBankDTO dto) {

        Member member = tokenUtil.getMemberFromRequest(request);
        return memberService.applyBindBankCard(member, dto);

    }

    @ApiOperation(value = "确认绑定银行卡")
    @PostMapping("bindBankCard")
    public R<Object> bindBankCard(HttpServletRequest request, @RequestBody BindBankDTO dto) {
        Member member = tokenUtil.getMemberFromRequest(request);
        return memberService.bindBankCard(member, dto);
    }

    @ApiOperation(value = "查询绑定银行卡")
    @PostMapping("bindBankCardList")
    public R<Object> bindBankCardList(HttpServletRequest request) {
        Member member = tokenUtil.getMemberFromRequest(request);
        List<Bank> banks = memberService.bindBankCardList(member);
        banks.forEach(bank -> bank.setBankCardNo(bank.getBankCardNo().replaceAll("(?<=\\d{4})\\d(?=\\d{4})", "*")));
        return R.ok(banks);
    }


    @ApiOperation(value = "解除绑定银行卡")
    @PostMapping("unbindBankCard")
    public R<Object> unbindBankCard(HttpServletRequest request, @RequestBody BindBankDTO bindBankDTO) {
        Member member = tokenUtil.getMemberFromRequest(request);
        return memberService.unbindBankCard(member, bindBankDTO);
    }

    @ApiOperation(value = "设置支付密码")
    @PostMapping("setPayPwd")
    @ApiImplicitParam(name = "jumpUrl", value = "跳转地址", required = true)
    @ResponseBody
    public R<Object> setPayPwd(HttpServletRequest request,@RequestBody Member temp) {
        Member member = tokenUtil.getMemberFromRequest(request);
        member.setJumpUrl(temp.getJumpUrl());
        return memberService.setPayPwd(member);
    }

    /**
     * 设置支付密码同步结果
     * @return
     */
    @ApiIgnore
    @GetMapping("/goSetPayPwdResult")
    public R goSetPayPwdResult(AllinPayResponseDTO<String> allinPayResponseDTO){
        log.info("设置支付密码结果:{}",allinPayResponseDTO);
        String status = allinPayResponseDTO.getStatus();
        if(Constants.SUCCESS_CODE.equals(status)){
            return R.ok("设置支付密码成功");
        }else{
           return R.failed("设置支付密码失败");
        }
    }

    @ApiOperation(value = "修改支付密码")
    @PostMapping("updatePayPwd")
    @ApiImplicitParam(name = "jumpUrl", value = "跳转地址", required = true)
    @ResponseBody
    public R<Object> updatePayPwd(HttpServletRequest request,@RequestBody Member temp) {
        Member member = tokenUtil.getMemberFromRequest(request);
        member.setJumpUrl(temp.getJumpUrl());
        return memberService.updatePayPwd(member);
    }
    /**
     * 修改支付密码结果
     * @return
     */
    @ApiIgnore
    @GetMapping("/goUpdatePayPwdResult")
    public R goUpdatePayPwdResult(AllinPayResponseDTO<String> allinPayResponseDTO){
        log.info("修改支付密码结果:{}",allinPayResponseDTO);
        String status = allinPayResponseDTO.getStatus();
        if(Constants.SUCCESS_CODE.equals(status)){
            return R.ok("修改支付密码成功");
        }else{
            return R.failed("修改支付密码失败");
        }
    }
    @ApiOperation(value = "重置支付密码")
    @PostMapping("resetPayPwd")
    @ApiImplicitParam(name = "jumpUrl", value = "跳转地址", required = true)
    @ResponseBody
    public R<Object> resetPayPwd(HttpServletRequest request,@RequestBody Member temp) {
        Member member = tokenUtil.getMemberFromRequest(request);
        member.setJumpUrl(temp.getJumpUrl());
        return memberService.resetPayPwd(member);
    }
    /**
     * 重置支付密码结果
     * @return
     */
    @ApiIgnore
    @GetMapping("/goResetPayPwdResult")
    public R goResetPayPwdResult(AllinPayResponseDTO<String> allinPayResponseDTO){
        log.info("重置支付密码结果:{}",allinPayResponseDTO);
        String status = allinPayResponseDTO.getStatus();
        if(Constants.SUCCESS_CODE.equals(status)){
            return R.ok("重置支付密码成功");
        }else{
            return R.failed("重置支付密码失败");
        }
    }

    /**
     * 通联电子协议签约
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "通联电子协议签约")
    @PostMapping("allinPaySignContract")
    @ApiImplicitParam(name = "jumpUrl", value = "跳转地址", required = true)
    public R allinPaySignContract(HttpServletRequest request, @RequestBody String jumpUrl) {
        Assert.hasText(jumpUrl, "跳转地址不能为空");
        JSONObject jsonObject = JSON.parseObject(jumpUrl);
        Member member = tokenUtil.getMemberFromRequest(request);
        return memberService.allinPaySignContract(member, jsonObject.get("jumpUrl").toString());
    }




    /**
     * 充值申请
     * @param dar
     * @return
     */
    @PostMapping("/depositApply")
    public R depositApply(@RequestBody DepositApplyRequestDTO dar,HttpServletRequest request) {
        log.info("充值申请:{}",dar);
        Member member = jwtTokenUtil.getMemberFromRequest(request);
        if(member==null){
            return R.failed(BizExceptionEnum.USER_DOES_NOT_EXIST.getMessage());
        }else{
            return R.ok(memberService.depositApply(dar,member));
        }

    }

    /**
     *  确认支付（后台确认+验证码）
     */
    @ApiOperation(value = "充值确认支付", notes = "充值确认支付【后台确认+验证码】")
    @PostMapping("/payConfirm")
    public R payConfirm(@RequestBody OrderDTO orderDTO, HttpServletRequest request) {
        Member member = jwtTokenUtil.getMemberFromRequest(request);
        if (member == null) {
            return R.failed("用户不存在");
        }
        orderDTO.setUserId(member.getId().toString());
        R r = memberService.payConfirm(orderDTO);
        return r;
    }


    /**
     * 提现申请
     * @param cwar
     * @return
     */
    @ApiOperation(value = "客户提现申请")
    @PostMapping("/cashWithdrawalApply")
    @ResponseBody
    public R<Map<String,String>> cashWithdrawalApply(HttpServletRequest request,@Valid @RequestBody CashWithdrawalApplyRequestDTO cwar) {
        log.info("客户提现申请:{}",cwar);
        Member member = tokenUtil.getMemberFromRequest(request);
        if (member == null) {
            return R.failed("用户不存在");
        }

        return memberService.cashWithdrawalApply(cwar,member);
    }


}
