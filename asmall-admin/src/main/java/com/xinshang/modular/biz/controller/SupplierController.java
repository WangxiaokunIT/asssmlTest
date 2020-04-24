package com.xinshang.modular.biz.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xinshang.constant.Constants;
import com.xinshang.core.base.controller.BaseController;
import com.xinshang.core.base.tips.Tip;
import com.xinshang.core.support.DateTime;
import com.xinshang.modular.biz.dto.*;
import com.xinshang.modular.biz.model.*;
import com.xinshang.modular.biz.service.*;
import com.xinshang.modular.biz.vo.BankVO;
import com.xinshang.modular.biz.vo.Suggest;
import com.xinshang.modular.biz.vo.SupplierVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import com.xinshang.core.support.BeanKit;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.core.common.constant.factory.PageFactory;

import java.math.BigDecimal;
import java.util.*;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import com.xinshang.core.log.LogObjectHolder;


/**
 * @title:供应商控制器
 *
 * @author: zhangjiajia
 * @since: 2019-10-21 18:44:47
 */
@Controller
@RequestMapping("/supplier")
@Slf4j
public class SupplierController extends BaseController {

    private String PREFIX = "/biz/supplier/";

    @Autowired
    private ISupplierService supplierService;

    @Autowired
    private IBankService bankService;

    @Autowired
    private IJoininService joininService;

    /**
     * 跳转到供应商首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "supplier.html";
    }

    /**
     * 跳转到添加供应商
     */
    @RequestMapping("/supplier_add")
    public String supplierAdd() {
        return PREFIX + "supplier_add.html";
    }

    /**
     * 跳转到修改供应商
     */
    @RequestMapping("/supplier_detail/{supplierId}")
    public String supplierDetail(@PathVariable Integer supplierId, Model model) {
        Map<String, Object> allInfoById = supplierService.getAllInfoById(supplierId);
        Supplier temp = (Supplier) allInfoById.get("supplier");
        model.addAttribute("item",allInfoById.get("supplier"));
        model.addAttribute("account",allInfoById.get("account"));
        LogObjectHolder.me().set(temp);
        return temp.getType()==1?PREFIX + "supplier_company_view.html":PREFIX + "supplier_user_view.html";
    }

    /**
     * 跳转到修改供应商
     */
    @RequestMapping("/supplier_update/{supplierId}")
    public String supplierUpdate(@PathVariable Integer supplierId, Model model) {
        Map<String, Object> allInfoById = supplierService.getAllInfoById(supplierId);
        Supplier temp = (Supplier) allInfoById.get("supplier");
        model.addAttribute("item",allInfoById.get("supplier"));
        model.addAttribute("account",allInfoById.get("account"));
        LogObjectHolder.me().set(temp);
        return PREFIX + "supplier_update.html";
    }

    /**
     * 获取供应商分页列表
     */
    @RequestMapping(value = "/pageList")
    @ResponseBody
    public Object pageList(Supplier supplier) {
        Page<SupplierVO> page = new PageFactory<SupplierVO>().defaultPage();

        return supplierService.selectPageInfo(page,supplier);
    }

    /**
     * 获取供应商列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(Supplier supplier) {
        Map<String, Object> beanMap = BeanKit.beanToMap(supplier,true);
        EntityWrapper<Supplier> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return supplierService.selectList(wrapper);
    }

    /**
     * 新增供应商
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Supplier supplier) {
        return supplierService.add(supplier);
    }

    /**
     * 删除供应商
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam String supplierIds) {
        if(StrUtil.isNotBlank(supplierIds)) {
            supplierService.deleteBatchIds(Arrays.asList(supplierIds.split(",")));
        }
        return SUCCESS_TIP;
    }

    /**
     * 激活供应商
     */
    @RequestMapping(value = "/active")
    @ResponseBody
    public Object active(@RequestParam String supplierIds) {
        if(StrUtil.isNotBlank(supplierIds)) {

            EntityWrapper<Supplier> ew = new EntityWrapper();
            ew.in("id",Arrays.asList(supplierIds.split(",")));
            List<Supplier> suppliers = supplierService.selectList(ew);
            for (Supplier supplier : suppliers) {
                supplierService.active(supplier);
            }
        }
        return SUCCESS_TIP;
    }


    /**
     * 修改供应商
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Supplier supplier) {
        supplierService.updateById(supplier);
        return SUCCESS_TIP;
    }

    /**
     * 供应商详情
     */
    @RequestMapping(value = "/detail/{supplierId}")
    @ResponseBody
    public Object detail(@PathVariable("supplierId") Integer supplierId) {

        return supplierService.selectById(supplierId);
    }

    /**
     * 检索
     */
    @RequestMapping(value = "/searchSupplier")
    @ResponseBody
    public Object searchSupplier(String search) {
        Suggest suggest = new Suggest();
        suggest.setCode(200);
        suggest.setMessage("成功");
        suggest.setRedirect("");
        if (StringUtils.isBlank(search)) {
            return suggest;
        }
        suggest.setValue(supplierService.searchSupplier(search));
        return suggest;
    }

    /**
     * 跳转绑定手机号页面
     */
    @RequestMapping("/openBindingPhone/{supplierId}")
    public String openBindingPhone(@PathVariable Integer supplierId, Model model) {
        Supplier supplier = supplierService.selectById(supplierId);
        model.addAttribute("item",supplier);
        LogObjectHolder.me().set(supplier);
        return PREFIX + "binding_phone.html";
    }

    /**
     * 发送通联短信验证码
     * @param allinPaySendSmsCaptchaDTO
     * @return
     */
    @PostMapping("/smsAllinPayCaptcha")
    @ResponseBody
    public Object SendAllinPaySMSCaptcha(AllinPaySendSmsCaptchaDTO allinPaySendSmsCaptchaDTO) {
        return supplierService.sendAllinPaySmsCaptcha(allinPaySendSmsCaptchaDTO);
    }


    /**
     * 通联绑定手机
     *
     * @param allinPayBindingPhoneDTO
     * @return
     */
    @PostMapping("/allinPayBindingPhone")
    @ResponseBody
    public Object allinPayBindingPhone(AllinPayBindingPhoneDTO allinPayBindingPhoneDTO) {
        return supplierService.allinPayBindingPhone(allinPayBindingPhoneDTO);
    }

    /**
     * 跳转到实名认证页面
     */
    @RequestMapping("/openRealNameAuth/{supplierId}")
    public String openRealNameAuth(@PathVariable Integer supplierId, Model model) {
        Supplier supplier = supplierService.selectById(supplierId);
        model.addAttribute("item",supplier);
        LogObjectHolder.me().set(supplier);
        return PREFIX + "real_name_auth.html";
    }

    /**
     * 实名认证
     * @param realNameAuthDTO
     * @return
     */
    @PostMapping("/realNameAuth")
    @ResponseBody
    public Object updateMember(RealNameAuthDTO realNameAuthDTO) {
        return supplierService.realNameAuthentication(realNameAuthDTO);
    }

    /**
     * 跳转到设置企业信息页面
     */
    @RequestMapping("/openSetCompanyInfo/{supplierId}")
    public String openSetCompanyInfo(@PathVariable Integer supplierId, Model model) {
        Supplier supplier = supplierService.selectById(supplierId);
        model.addAttribute("item",supplier);
        LogObjectHolder.me().set(supplier);
        return PREFIX + "set_company_info.html";
    }

    /**
     * 查询通联企业信息审核结果
     * @return
     */
    @PostMapping("/companyVerifyResult")
    @ResponseBody
    public Object companyVerifyResult(String bizUserId){
        return supplierService.companyVerifyResult(bizUserId);
    }

    /**
     * 通联电子协议签约
     * @return
     */
    @PostMapping("/allinPaySignContract")
    @ResponseBody
    public Object allinPaySignContract(String bizUserId){
        log.info("通联电子协议签约:{}",bizUserId);
        return supplierService.allinPaySignContract(bizUserId);
    }

    /**
     * 跳转通联电子协议签约结果
     * @return
     */
    @RequestMapping("/goSignContractPageResult")
    public String goSignContractPageResult(AllinPayAsynResponseDTO allinPayAsynResponseDTO, Model model){
        log.info("通联签订协议结果:{}",allinPayAsynResponseDTO);
        JSONObject signedValue = (JSONObject)JSON.parseObject(allinPayAsynResponseDTO.getRps()).get("signedValue");
        if(Constants.SUCCESS_CODE.equals(signedValue.get("result"))){
            Supplier supplier = supplierService.selectOne(new EntityWrapper<Supplier>().eq("biz_user_id", signedValue.get("bizUserId").toString()));
            supplier.setContractNo(signedValue.get("ContractNo").toString());
            supplierService.updateById(supplier);
            model.addAttribute("signContractResultMsg",signedValue.get("result"));
            model.addAttribute("msg","签订成功");
        }else{
            model.addAttribute("signContractResultMsg",signedValue.get("result"));
            model.addAttribute("msg","签订失败");
        }
        return PREFIX + "allin_pay_sync_result.html";
    }

    /**
     * 设置企业信息
     */
    @RequestMapping("/setCompanyInfo")
    @ResponseBody
    public Object setCompanyInfo(SetCompanyInfoDTO setCompanyInfoDTO) {
        log.info("设置企业信息:{}",setCompanyInfoDTO);
        return supplierService.setCompanyInfo(setCompanyInfoDTO);
    }

    /**
     * 跳转到银行卡列表
     */
    @RequestMapping("/bankList/{supplierId}")
    public String bankList(@PathVariable Integer supplierId, Model model) {
        log.info("跳转到银行卡列表:{}",supplierId);
        model.addAttribute("supplierId",supplierId);
        return PREFIX + "bank_list.html";
    }

    /**
     * 获取银行卡分页列表
     */
    @RequestMapping(value = "/bankPageList/{supplierId}")
    @ResponseBody
    public Object bankPageList(@PathVariable Integer supplierId) {
        log.info("获取银行卡分页列表:{}",supplierId);
        Page<BankVO> page = new PageFactory<BankVO>().defaultPage();
        return bankService.selectBankInfo(supplierId,2,page);
    }

    /**
     * 跳转到绑定银行卡页面
     */
    @RequestMapping("/openBindingBankCard/{supplierId}")
    public String openBindingBankCard(@PathVariable Integer supplierId, Model model) {
        log.info("跳转到绑定银行卡页面:{}",supplierId);
        Supplier supplier = supplierService.selectById(supplierId);
        model.addAttribute("item",supplier);
        LogObjectHolder.me().set(supplier);
        return PREFIX + "apply_bind_bank_card.html";
    }

    /**
     * 请求绑定银行卡
     * @param dto
     * @return
     */
    @PostMapping("/applyBindBankCard")
    @ResponseBody
    public Object applyBindBankCard(BindBankDTO dto) {
        log.info("请求绑定银行卡:{}",dto);
        return supplierService.applyBindBankCard(dto);
    }

    /**
     * 确认绑定银行卡
     * @param dto
     * @return
     */
    @PostMapping("/bindBankCard")
    @ResponseBody
    public Object bindBankCard(BindBankDTO dto) {
        log.info("确认绑定银行卡:{}",dto);
        return supplierService.bindBankCard(dto);
    }

    /**
     * 解除绑定银行卡
     * @param id
     * @return
     */
    @PostMapping("/relieveBindingBankCard")
    @ResponseBody
    public Object relieveBindingBankCard(Integer id) {
        log.info("解除绑定银行卡:{}",id);
        return supplierService.relieveBindingBankCard(id);
    }

    /**
     * 锁定/解锁会员
     * @param id
     * @return
     */
    @PostMapping("/lockOrUnlockMember")
    @ResponseBody
    public Object lockOrUnlockMember(Integer id) {
        log.info("锁定/解锁会员:{}",id);
        return supplierService.lockOrUnlockMember(id);
    }

    /**
     * 跳转到支付密码管理页面
     * @param supplierId
     * @return
     */
    @GetMapping("/openPayPwd/{supplierId}")
    public Object openPayPwd(@PathVariable Integer supplierId,Model model) {
        log.info("跳转到支付密码管理页面:{}",supplierId);
        Map<String,String> map = supplierService.generatePayPwdOptUrl(supplierId);
        model.addAllAttributes(map);
        return PREFIX + "pay_pwd_opt.html";
    }

    /**
     * 设置支付密码同步结果
     * @return
     */
    @GetMapping("/goSetPayPwdResult")
    public String goSetPayPwdResult(AllinPayResponseDTO<String> allinPayResponseDTO, Model model){
        log.info("设置支付密码同步返回结果:{}",allinPayResponseDTO);
        JSONObject signedValue = JSON.parseObject(allinPayResponseDTO.getSignedValue());
        if(Constants.SUCCESS_CODE.equals(signedValue.get("result"))){
            Supplier supplier = supplierService.selectOne(new EntityWrapper<Supplier>().eq("biz_user_id", signedValue.get("bizUserId").toString()));
            supplier.setIsSetPayPwd(1);
            supplierService.updateById(supplier);
            model.addAttribute("result",signedValue.get("result"));
            model.addAttribute("msg","设置支付密码成功");
        }else{
            model.addAttribute("result",signedValue.get("result"));
            model.addAttribute("msg","设置支付密码失败");
        }
        return PREFIX + "allin_pay_sync_result.html";
    }

    /**
     * 修改支付密码同步结果
     * @return
     */
    @GetMapping("/goUpdatePayPwdResult")
    public String goUpdatePayPwdResult(AllinPayResponseDTO<String> allinPayResponseDTO, Model model){
        log.info("修改支付密码同步返回结果:{}",allinPayResponseDTO);
        JSONObject signedValue = JSON.parseObject(allinPayResponseDTO.getSignedValue());
        if(Constants.SUCCESS_CODE.equals(signedValue.get("result"))){
            Supplier supplier = supplierService.selectOne(new EntityWrapper<Supplier>().eq("biz_user_id", signedValue.get("bizUserId").toString()));
            supplier.setIsSetPayPwd(1);
            supplierService.updateById(supplier);
            model.addAttribute("result",signedValue.get("result"));
            model.addAttribute("msg","修改支付密码成功");
        }else{
            model.addAttribute("result",signedValue.get("result"));
            model.addAttribute("msg","修改支付密码失败");
        }
        return PREFIX + "allin_pay_sync_result.html";
    }

    /**
     * 重置支付密码同步结果
     * @return
     */
    @GetMapping("/goResetPayPwdResult")
    public String goResetPayPwdResult(AllinPayResponseDTO<String> allinPayResponseDTO, Model model){
        log.info("重置支付密码同步返回结果:{}",allinPayResponseDTO);
        JSONObject signedValue = JSON.parseObject(allinPayResponseDTO.getSignedValue());
        if(Constants.SUCCESS_CODE.equals(signedValue.get("result"))){
            Supplier supplier = supplierService.selectOne(new EntityWrapper<Supplier>().eq("biz_user_id", signedValue.get("bizUserId").toString()));
            supplier.setIsSetPayPwd(1);
            supplierService.updateById(supplier);
            model.addAttribute("result",signedValue.get("result"));
            model.addAttribute("msg","重置支付密码成功");
        }else{
            model.addAttribute("result",signedValue.get("result"));
            model.addAttribute("msg","重置支付密码失败");
        }
        return PREFIX + "allin_pay_sync_result.html";
    }

    /**
     * 跳转到提现申请页面
     * @param supplierId
     * @return
     */
    @GetMapping("/openCashWithdrawal/{supplierId}")
    public Object openCashWithdrawal(@PathVariable Integer supplierId,Model model) {
        log.info("跳转到供应商提现申请页面:{}",supplierId);
        Map<String, Object> allInfoById = supplierService.getAllInfoById(supplierId);
        model.addAttribute("item",allInfoById.get("supplier"));
        model.addAttribute("account",allInfoById.get("account"));
        return PREFIX + "cash_withdrawal_apply.html";
    }

    /**
     * 从招募管理跳转到提现申请页面
     * @param supplierId
     * @return
     */
    @GetMapping("/openCashWithdrawalProject/{supplierId}/{projectId}")
    public Object openCashWithdrawalProject(@PathVariable Integer supplierId, @PathVariable Integer projectId, Model model) {
        log.info("跳转到供应商提现申请页面:{}",supplierId);
        Map<String, Object> allInfoById = supplierService.getAllInfoById(supplierId);
        model.addAttribute("item",allInfoById.get("supplier"));
        model.addAttribute("account",allInfoById.get("account"));
        // 加盟总金额
        EntityWrapper<Joinin> wrapper = new EntityWrapper<>();
        wrapper.eq("project_id", projectId);
        List<Joinin> joininList = joininService.selectList(wrapper);
        // 加盟总金额
        BigDecimal projectAccount = new BigDecimal("0.00");
        for(Joinin joinin : joininList) {
            projectAccount = projectAccount.add(joinin.getInvestmentAmount());

        }
        model.addAttribute("projectAccount", projectAccount);
        return PREFIX + "cash_withdrawal_apply_project.html";
    }

    /**
     * 跳转到提现申请页面
     * @param supplierId
     * @return
     */
    @GetMapping("/openDepositApply/{supplierId}")
    public Object openDepositApply(@PathVariable Integer supplierId,Model model) {
        log.info("跳转到供应商提现申请页面:{}",supplierId);
        Map<String, Object> allInfoById = supplierService.getAllInfoById(supplierId);
        model.addAttribute("item",allInfoById.get("supplier"));
        model.addAttribute("account",allInfoById.get("account"));
        return PREFIX + "deposit_apply.html";
    }

    /**
     * 充值申请
     * @param dar
     * @return
     */
    @PostMapping("/depositApply")
    @ResponseBody
    public Map<String,String> depositApply(DepositApplyRequestDTO dar) {
        log.info("供应商充值申请:{}",dar);
        return supplierService.depositApply(dar);
    }

    /**
     * 提现申请

     * @return
     */
    @PostMapping("/cashWithdrawalApply")
    @ResponseBody
    public Tip cashWithdrawalApply(CashWithdrawalApplyRequestDTO cwar) {
        log.info("供应商提现申请:{}",cwar);
       return supplierService.cashWithdrawalApply(cwar);
    }

    /**
     * 充值确认
     * @param depositConfirmRequestDTO
     * @return
     */
    @PostMapping("/depositConfirmPayment")
    @ResponseBody
    public Tip depositConfirmPayment(DepositConfirmRequestDTO depositConfirmRequestDTO) {
        log.info("供应商充值确认{}",depositConfirmRequestDTO);
       return supplierService.depositConfirmPayment(depositConfirmRequestDTO);
    }



}
