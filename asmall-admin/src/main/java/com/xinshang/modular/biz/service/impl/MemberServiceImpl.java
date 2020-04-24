package com.xinshang.modular.biz.service.impl;


import com.allinpay.yunst.sdk.bean.YunRequest;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xinshang.config.properties.AllinPayProperties;
import com.xinshang.constant.Constants;
import com.xinshang.core.common.exception.BizExceptionEnum;
import com.xinshang.core.enums.BizTypeEnum;
import com.xinshang.core.enums.UserTypeEnum;
import com.xinshang.core.util.AllinPayUtil;
import com.xinshang.core.util.NoUtil;
import com.xinshang.modular.biz.dao.CommonConstants;
import com.xinshang.modular.biz.dto.APCreateMemberRespDTO;
import com.xinshang.modular.biz.dto.AllinPayQueryAccountDTO;
import com.xinshang.modular.biz.dto.AllinPayResponseDTO;
import com.xinshang.modular.biz.model.Account;
import com.xinshang.modular.biz.model.Member;
import com.xinshang.modular.biz.dao.MemberMapper;
import com.xinshang.modular.biz.service.IMemberService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.modular.biz.vo.AccountVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import java.math.BigDecimal;
import java.util.Optional;


/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-10-19
 */
@Service
@Slf4j
@AllArgsConstructor
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements IMemberService {

    private final MemberMapper memberMapper;

    private final AllinPayProperties allinPayProperties;

    /**
     * 更改会员状态
     *
     * @param username
     * @param state
     * @return
     */
    @Override
    public Boolean alertMemberState(String username, Integer state) {
        Member member = this.selectOne(new EntityWrapper<Member>().eq("username", username));
        member.setState(state);
        this.updateById(member);
        return true;
    }

    /**
     * 更改会员审核状态
     *
     * @param id
     * @param state
     * @return
     */
    @Override
    public Boolean alertMemberVipState(Long id, Integer state, Integer vipState) {
        Member member = memberMapper.selectById(id);
        member.setAuditStatus(state);
        member.setVip(vipState);
        Integer update = memberMapper.updateById(member);
        if (update == 0) {
            return false;
        }
        return true;
    }


    /**
     * 获取客户通联账户余额
     * @param bizUserId
     * @return
     */
    @Override
    public Account getAccount(String bizUserId) {
        log.info("获取通联账户余额:{}", bizUserId);
        YunRequest request = new YunRequest("OrderService","queryBalance");
        request.put("bizUserId", bizUserId);
        request.put("accountSetNo", allinPayProperties.getAccountSetNo());

        Optional<AllinPayResponseDTO<AllinPayQueryAccountDTO>> response = AllinPayUtil.request(request, AllinPayQueryAccountDTO.class);
        //通联同步失败一定要抛异常回滚数据库
        Assert.isTrue(Constants.SUCCESS_CODE.equals(response.get().getStatus()), () -> {
            log.warn("查询账户信息通联审核结果失败:{}", response);
            return response.get().getMessage();
        });

        AllinPayQueryAccountDTO signedValue = response.get().getSignedValue();
        Account account = new Account();
        BigDecimal totleAmoun = new BigDecimal((signedValue.getAllAmount() / 100.0) + "");
        BigDecimal freezenAmount = new BigDecimal((signedValue.getFreezenAmount() / 100.0) + "");
        account.setTotleAmount(totleAmoun);
        account.setAvailableBalance(totleAmoun.subtract(freezenAmount));
        account.setFreezingAmount(freezenAmount);
        return account;
    }

    /**
     * 老客户激活
     * @param member
     */
    @Override
    public void active(Member member) {

        //同步到通联
        YunRequest request = new YunRequest("MemberService", "createMember");
        String memberNo = NoUtil.generateCode(BizTypeEnum.MEMBER_NO, UserTypeEnum.CUSTOMER, member.getId());
        request.put("bizUserId", memberNo);
        //会员类型:[3:个人会员,2:企业会员]
        request.put("memberType", 3);
        //访问终端类型:[1:Mobile,2:PC]
        request.put("source", 1);
        log.info("用户信息同步到通联:{}", request);
        Optional<AllinPayResponseDTO<APCreateMemberRespDTO>> response = AllinPayUtil.request(request, APCreateMemberRespDTO.class);
        //通联同步失败一定要抛异常回滚数据库
        Assert.isTrue(CommonConstants.SUCCESS_CODE.equals(response.get().getStatus()), () -> {
            log.warn("用户信息同步到通联失败:{}", response);
            return BizExceptionEnum.USER_CREATION_FAILED.getMessage();
        });
        member.setBizUserId(memberNo);
        //默认创建用户实名认证状态为0[未认证]
        member.setRealNameState(0);
        member.setAllinpayUserId(response.get().getSignedValue().getUserId());
        //111111
        member.setPassword("8e98ff244e20386d9568968b4eec0590");
        member.setSalt("VTz5911599511");
        memberMapper.updateById(member);
    }

}
