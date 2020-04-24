package com.xinshang.modular.biz.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.xinshang.core.base.tips.SuccessTip;
import com.xinshang.core.base.tips.Tip;
import com.xinshang.modular.biz.dto.*;
import com.xinshang.modular.biz.model.Supplier;
import com.xinshang.modular.biz.vo.SupplierVO;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 供应商 服务类
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-10-21
 */
public interface ISupplierService extends IService<Supplier> {

    /**
     * 模糊查询供应商列表
     * @param search
     * @return
     */
    List<Supplier> searchSupplier(String search);

    /**
     * 添加供应商
     * @param supplier
     */
    Tip add(Supplier supplier);

    /**
     * 激活供应商
     * @param supplier
     */
    Tip active(Supplier supplier);

    /**
     * 发送通联绑定手机验证码
     * @param allinPaySendSmsCaptchaDTO
     * @return
     */
    Object sendAllinPaySmsCaptcha(AllinPaySendSmsCaptchaDTO allinPaySendSmsCaptchaDTO);

    /**
     * 通联绑定手机
     * @param allinPayBindingPhoneDTO
     * @return
     */
    Object allinPayBindingPhone(AllinPayBindingPhoneDTO allinPayBindingPhoneDTO);

    /**
     * 获取与该供应商相关的所有信息
     * @param id
     * @return
     */
    Map<String,Object> getAllInfoById(Integer id);

    /**
     * 实名认证
     * @return
     */
    Object realNameAuthentication(RealNameAuthDTO realNameAuthDTO);


    /**
     * 通联电子协议签约
     * @param bizUserId
     * @return
     */
    Object allinPaySignContract(String bizUserId);

    /**
     * 客户签约通联电子协议异步回调方法
     * @param allinPayAsynResponseDTO
     */
    void supplierAsynSignContract(AllinPayAsynResponseDTO allinPayAsynResponseDTO);

    /**
     * 申请绑定银行卡
     * @param dto
     * @return
     */
    Object applyBindBankCard(BindBankDTO dto);

    /**
     * 确认绑定银行卡
     * @param dto
     * @return
     */
    Object bindBankCard(BindBankDTO dto);

    /**
     * 设置企业信息
     * @param setCompanyInfoDTO
     * @return
     */
    Object setCompanyInfo(SetCompanyInfoDTO setCompanyInfoDTO);

    /**
     * 解除银行卡
     * @param id
     * @return
     */
    Object relieveBindingBankCard(Integer id);

    /**
     * 分页查询供应商
     * @param page
     * @param supplier
     * @return
     */
    Page<SupplierVO> selectPageInfo(Page<SupplierVO> page, Supplier supplier);

    /**
     * 解锁/锁定会员
     * @param id
     * @return
     */
    Object lockOrUnlockMember(Integer id);

    /**
     * 生成支付密码操作url
     * @param id
     * @return
     */
    Map<String, String> generatePayPwdOptUrl(Integer id);

    /**
     * 充值申请
     * @param dar
     * @return
     */
    Map<String, String> depositApply(DepositApplyRequestDTO dar);

    /**
     * 提现申请
     * @param cwar
     * @return
     */
    Tip cashWithdrawalApply(CashWithdrawalApplyRequestDTO cwar);

    /**
     * 充值确认
     * @param depositConfirmRequestDTO
     *
     */
    Tip depositConfirmPayment(DepositConfirmRequestDTO depositConfirmRequestDTO);


    /**
     * 查询企业信息审核结果
     * @param bizUserId
     * @return
     */
    Object companyVerifyResult(String bizUserId);

}
