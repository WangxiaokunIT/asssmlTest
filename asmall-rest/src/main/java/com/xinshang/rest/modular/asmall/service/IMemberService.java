package com.xinshang.rest.modular.asmall.service;

import com.baomidou.mybatisplus.service.IService;
import com.xinshang.rest.common.util.R;
import com.xinshang.rest.modular.asmall.dto.*;
import com.xinshang.rest.modular.asmall.model.Account;
import com.xinshang.rest.modular.asmall.model.Bank;
import com.xinshang.rest.modular.asmall.model.Member;
import com.xinshang.rest.modular.asmall.vo.BankVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-10-19
 */
public interface IMemberService extends IService<Member> {

    /**
     * 根据ID查询用户信息
     *
     * @param id
     * @return
     */
    BankVO selectDetailbyId(Long id);

    /**
     * 用户手机号认证
     *
     * @param authCaptchaRequestDTO
     * @return
     */
    R<AuthResponseDTO> memberAuth(AuthCaptchaRequestDTO authCaptchaRequestDTO);

    /**
     * 用户微信认证
     *
     * @param weChatUserInfoDTO
     * @return
     */
    R<AuthResponseDTO> memberWeChatAuth(WeChatUserInfoDTO weChatUserInfoDTO);

    /**
     * 实名认证
     *
     * @param member
     * @return
     */
    R<Object> updateMember(Member member, MemberDTO dto);

    /**
     * 申请绑定银行卡
     *
     * @param member
     * @param dto
     * @return
     */
    R<Object> applyBindBankCard(Member member, BindBankDTO dto);

    /**
     * 确认绑定银行卡
     *
     * @param member
     * @param dto
     * @return
     */
    R<Object> bindBankCard(Member member, BindBankDTO dto);


    /**
     * 查询绑定银行卡列表
     *
     * @param member
     * @return
     */
    List<Bank> bindBankCardList(Member member);

    /**
     * 解除绑定银行卡
     *
     * @param member
     * @param bindBankDTO
     * @return
     */
    R<Object> unbindBankCard(Member member, BindBankDTO bindBankDTO);

    /**
     * 设置支付密码
     *
     * @param member
     * @return
     */
    R<Object> setPayPwd(Member member);

    /**
     * 客户设置支付密码异步回调方法
     *
     * @param allinPayAsynResponseDTO
     */
    void memberAsynSetPwdContract(AllinPayAsynResponseDTO allinPayAsynResponseDTO);

    /**
     * 修改支付密码
     *
     * @param member
     * @return
     */
    R<Object> updatePayPwd(Member member);
    /**
     * 修改支付密码异步回调方法
     *
     * @param allinPayAsynResponseDTO
     * @return
     */
    void memberAsynUpdatePwdContract(AllinPayAsynResponseDTO allinPayAsynResponseDTO);

    /**
     * 重置支付密码
     *
     * @param member
     * @return
     */
    R<Object> resetPayPwd(Member member);
    /**
     * 重置支付密码异步回调方法
     *
     * @param allinPayAsynResponseDTO
     * @return
     */
    void memberAsynResetPwdContract(AllinPayAsynResponseDTO allinPayAsynResponseDTO);

    /**
     * 通联电子协议签约
     *
     * @param member
     * @param jumpUrl
     * @return
     */
    R allinPaySignContract(Member member, String jumpUrl);

    /**
     * 客户签约通联电子协议异步回调方法
     *
     * @param allinPayAsynResponseDTO
     */
    void memberAsynSignContract(AllinPayAsynResponseDTO allinPayAsynResponseDTO);

    /**
     * 用户认证
     * @param authPasswordRequestDTO
     * @return
     */
    R<AuthResponseDTO> memberAuth(AuthPasswordRequestDTO authPasswordRequestDTO);

    /**
     * 用户注册
     * @param registerRequestDTO
     * @return
     */
    R<String> register(RegisterRequestDTO registerRequestDTO);

    /**
     * 提现申请
     * @param cwar
     * @return
     */
    R<Map<String,String>> cashWithdrawalApply(CashWithdrawalApplyRequestDTO cwar,Member member);


    /**
     * 充值申请
     * @param dar
     * @return
     */
    Map<String, String> depositApply(DepositApplyRequestDTO dar, Member member);

    /**
     * 功能描述: 支付确认
     *
     * @Param: [orderDTO]
     * @Return: com.xinshang.rest.common.util.R
     * @Auther: wangxiaokun
     * @Date: 2019/11/18 11:47
     * @Description:
     * @Modify:
     */
    R payConfirm(OrderDTO orderDTO);

    /**
     * 重置本系统登录密码
     * @param registerRequestDTO
     * @return
     */
    R<String> resetPassword(RegisterRequestDTO registerRequestDTO);

    Account getAccount(String bizUserId);
}
