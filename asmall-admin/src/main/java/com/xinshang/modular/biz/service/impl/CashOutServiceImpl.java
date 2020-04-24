package com.xinshang.modular.biz.service.impl;

import com.allinpay.yunst.sdk.bean.YunRequest;
import com.allinpay.yunst.sdk.util.RSAUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.config.properties.AllinPayCodeProperties;
import com.xinshang.config.properties.AllinPayProperties;
import com.xinshang.config.properties.SystemProperties;
import com.xinshang.constant.Constants;
import com.xinshang.core.enums.BizTypeEnum;
import com.xinshang.core.enums.UserTypeEnum;
import com.xinshang.core.util.AllinPayUtil;
import com.xinshang.core.util.NoUtil;
import com.xinshang.modular.biz.dao.CashOutMapper;
import com.xinshang.modular.biz.dto.AllinPayResponseDTO;
import com.xinshang.modular.biz.dto.BatchPaymentRespDTO;
import com.xinshang.modular.biz.dto.CashOutDTO;
import com.xinshang.modular.biz.model.Bank;
import com.xinshang.modular.biz.model.CashOut;
import com.xinshang.modular.biz.model.Member;
import com.xinshang.modular.biz.model.Supplier;
import com.xinshang.modular.biz.service.IBankService;
import com.xinshang.modular.biz.service.ICashOutService;
import com.xinshang.modular.biz.service.IMemberService;
import com.xinshang.modular.biz.utils.MoneyFormatTester;
import com.xinshang.modular.biz.vo.CashOutVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 提现审核 服务实现类
 * </p>
 *
 * @author lvyingkai
 * @since 2019-10-22
 */
@Slf4j
@Service
@AllArgsConstructor
public class CashOutServiceImpl extends ServiceImpl<CashOutMapper, CashOut> implements ICashOutService {

    private final CashOutMapper cashOutMapper;
    private final IMemberService memberService;
    private final AllinPayCodeProperties allinPayCodeProperties;
    private final AllinPayProperties allinPayProperties;
    private final IBankService bankService;
    private final SystemProperties systemProperties;

    @Override
    public List<CashOutVO> showCashOut(CashOutDTO param, Page<CashOutVO> page) {
        return cashOutMapper.showCashOut(param, page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCashOut(CashOut cashOut) throws Exception {
        int state = cashOut.getState();
        cashOut = cashOutMapper.selectById(cashOut.getId());
        cashOut.setState(state);
        if(cashOut.getState() == 2) {
            // 提现订单号
            String supplierOrder = NoUtil.generateCode(BizTypeEnum.CASH_WITHDRAWAL, UserTypeEnum.SUPPLIER, cashOut.getId());

            cashOut.setBizOrderNo(supplierOrder);
            cashOut.setState(null);
            cashOutMapper.updateById(cashOut);
            // 调用通联
            cashOut(cashOut);
            cashOut.setState(2);
        }
        cashOut.setUpdateTime(new Date());
        cashOutMapper.updateById(cashOut);
    }

    @Override
    public void updateCashOut(String bizOrderNo, Integer state) {
        EntityWrapper<CashOut> wrapper = new EntityWrapper<>();
        wrapper.eq("biz_order_no", bizOrderNo);
        List<CashOut> list = cashOutMapper.selectList(wrapper);
        Assert.isTrue(list == null || list.size() == 0, () -> "未找到订单号：" + bizOrderNo);
        CashOut cashOut = cashOutMapper.selectList(wrapper).get(0);
        if(state.equals(cashOut.getState())) {
            cashOut.setState(state);
            cashOutMapper.updateById(cashOut);
        }
    }

    /**
     * 提现调用通联接口
     */
    @Override
    public void cashOut(CashOut cashOut) throws Exception {
        Supplier supplier = null;
        Member member = null;
        // 提现订单号
        String supplierOrder = cashOut.getBizOrderNo();
        if(StringUtils.isEmpty(supplierOrder)) {
            supplierOrder = NoUtil.generateCode(BizTypeEnum.CASH_WITHDRAWAL, UserTypeEnum.SUPPLIER, cashOut.getId());
        }

        // vip客户
        if(cashOut.getType() == 1) {
            member = memberService.selectById(cashOut.getSupplierId());
        // 供应商
        }else {//TODO
            //supplier = supplierService.selectById(cashOut.getSupplierId());
        }

        log.info("url:{}",systemProperties.getProjectUrl());

        // 银行卡信息
        EntityWrapper<Bank> wrapper = new EntityWrapper<>();
        wrapper.eq("id", cashOut.getBankCardId());
        wrapper.eq("type", cashOut.getType());
        Bank bank = bankService.selectList(wrapper).get(0);

        //同步到通联
        YunRequest request = new YunRequest("OrderService", "withdrawApply");
        request.put("bizOrderNo", supplierOrder);
        if(cashOut.getType() == 1) {
            request.put("bizUserId", member.getBizUserId());
        } else {
            request.put("bizUserId", supplier.getBizUserId());
        }
        request.put("accountSetN", allinPayProperties.getAccountSetNo());
        request.put("amount", MoneyFormatTester.bigDecimal2Long(cashOut.getMoney()));
        request.put("fee", 0);

        request.put("backUrl", systemProperties.getProjectUrl() + "/allinPayAsynRespNotice/withdrawal");
        request.put("bankCardNo", RSAUtil.encrypt(bank.getBankCardNo()));
        request.put("bankCardPro", bank.getBankCardPro());
        request.put("withdrawType", allinPayCodeProperties.getWithdrawType());
        request.put("industryCode", allinPayCodeProperties.getOtherCode());
        request.put("industryName", allinPayCodeProperties.getOtherName());
        /**
         * Mobile 1 整型
         * PC 2 整型
         */
        request.put("source", 2);

        log.info("提现同步到通联:{}",request);
        Optional<AllinPayResponseDTO<BatchPaymentRespDTO>> response = AllinPayUtil.request(request,BatchPaymentRespDTO.class);
        //通联同步失败一定要抛异常回滚数据库
        Assert.isTrue(Constants.SUCCESS_CODE.equals(response.get().getStatus()),()->{
            log.warn("提现同步到通联失败:{}",response);
            return "提现失败";
        });
    }
}
