package com.xinshang.modular.biz.controller;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xinshang.core.base.controller.BaseController;
import com.xinshang.core.shiro.ShiroKit;
import com.xinshang.core.shiro.ShiroUser;
import com.xinshang.core.support.DateTime;
import com.xinshang.core.support.DateTimeKit;
import com.xinshang.modular.biz.dto.MemberDTO;
import com.xinshang.modular.biz.model.Account;
import com.xinshang.modular.biz.model.Bank;
import com.xinshang.modular.biz.model.Examine;
import com.xinshang.modular.biz.service.IBankService;
import com.xinshang.modular.biz.service.IExamineService;
import com.xinshang.modular.biz.vo.AccountVO;
import com.xinshang.modular.biz.vo.ExamineVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import com.xinshang.core.support.BeanKit;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.core.common.constant.factory.PageFactory;

import java.util.List;
import java.util.Map;
import java.util.Arrays;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import com.xinshang.core.log.LogObjectHolder;
import com.xinshang.modular.biz.model.Member;
import com.xinshang.modular.biz.service.IMemberService;

/**
 * @title:客户管理控制器
 * @author: sunhao
 * @since: 2019-10-19 11:37:47
 */
@Controller
@RequestMapping("/member")
public class MemberController extends BaseController {

    private String PREFIX = "/biz/member/";

    @Autowired
    private IMemberService memberService;
    @Autowired
    private IBankService bankService;
    @Autowired
    private IExamineService examineService;

    /**
     * 跳转到客户管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "member.html";
    }

    /**
     * 跳转到待审核vip首页
     */
    @RequestMapping("/member_auditList")
    public String auditList() {
        return PREFIX + "member_auditList.html";
    }

    /**
     * 跳转到待审核vip
     */
    @RequestMapping("/member_audit/{id}")
    public String auditList(@PathVariable Long id, Model model) {

        Member member = memberService.selectById(id);
        Wrapper wrapper=new EntityWrapper();
        List<Bank> bankList = bankService.selectList(wrapper.eq("master_id",member.getId()).eq("type",1));
        Account account=memberService.getAccount(member.getBizUserId());
        model.addAttribute("bankList", bankList);
        model.addAttribute("item", member);
        model.addAttribute("account", account);
        LogObjectHolder.me().set(member);
        return PREFIX + "member_audit.html";
    }


    /**
     * 跳转到添加客户管理
     */
    @RequestMapping("/member_add")
    public String memberAdd() {
        return PREFIX + "member_add.html";
    }

    /**
     * 跳转到修改客户管理
     */
    @RequestMapping("/member_update/{memberId}")
    public String memberUpdate(@PathVariable Integer memberId, Model model) {
        Member member = memberService.selectById(memberId);
        model.addAttribute("item", member);
        LogObjectHolder.me().set(member);
        return PREFIX + "member_edit.html";
    }


    /**
     * 获取客户管理分页列表
     */
    @RequestMapping(value = "/pageList")
    @ResponseBody
    public Object pageList(Member member) {
        Page<Member> page = new PageFactory<Member>().defaultPage();
        Map<String, Object> beanMap = BeanKit.beanToMap(member, true);
        EntityWrapper<Member> wrapper = new EntityWrapper<>();
        //手机号，身份证号码，注册时间，姓名
        if (StringUtils.isNotBlank(member.getUsername())) {
            wrapper.like("real_name", member.getUsername());
        }
        if (member.getCreated() != null) {
            wrapper.eq("DATE_FORMAT(created,'%Y-%m-%d')", DateTimeKit.formatDate(member.getCreated()));
        }
        if (StringUtils.isNotBlank(member.getCardNumber())) {
            wrapper.eq("card_number", member.getCardNumber());
        }
        if (StringUtils.isNotBlank(member.getPhone())) {
            wrapper.like("username", member.getPhone());
        }
        return memberService.selectPage(page, wrapper);
    }

    /**
     * 获取审核VIP客户管理分页列表
     */
    @RequestMapping(value = "/auditPageList")
    @ResponseBody
    public Object auditPageList(Member member) {
        Page<Member> page = new PageFactory<Member>().defaultPage();
        EntityWrapper<Member> wrapper = new EntityWrapper<>();
        wrapper.ne("audit_status", 1);
        return memberService.selectPage(page, wrapper);
    }

    /**
     * 停用会员
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "/stop/{username}")
    @ResponseBody
    public Object stopMember(@PathVariable String username) {
        memberService.alertMemberState(username, 0);
        return SUCCESS_TIP;
    }

    /**
     * 启用会员
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "/start/{username}")
    @ResponseBody
    public Object startMember(@PathVariable String username) {

        memberService.alertMemberState(username, 1);
        return SUCCESS_TIP;
    }

    /**
     * 会员审核拒绝
     *
     * @param MemberDTO
     * @return
     */
    @RequestMapping(value = "/refuse")
    @ResponseBody
    public Object refuse(MemberDTO MemberDTO) {
        ShiroUser user = ShiroKit.getUser();
        Examine examine = new Examine();
        examine.setProjectId(MemberDTO.getId());
        examine.setCreateTime(new DateTime());
        //3会员审核操作
        examine.setType(3);
        examine.setState(3);
        examine.setRemarks(MemberDTO.getAuditDetail());
        examine.setUserId(Long.valueOf(user.getId()));
        examine.setUserName(user.getName());
        examineService.insert(examine);
        memberService.alertMemberVipState(MemberDTO.getId(), 2, 0);
        return SUCCESS_TIP;
    }

    /**
     * 会员审核通过
     *
     * @param MemberDTO
     * @return
     */
    @RequestMapping(value = "/access")
    @ResponseBody
    public Object access(MemberDTO MemberDTO) {
        //获取当前审批者姓名
        ShiroUser user = ShiroKit.getUser();
        Examine examine = new Examine();
        examine.setProjectId(MemberDTO.getId());
        examine.setCreateTime(new DateTime());
        //3会员审核操作
        examine.setType(3);
        examine.setState(2);
        examine.setRemarks(MemberDTO.getAuditDetail());
        examine.setUserId(Long.valueOf(user.getId()));
        examine.setUserName(user.getName());
        examineService.insert(examine);
        memberService.alertMemberVipState(MemberDTO.getId(), 1, 1);
        return SUCCESS_TIP;
    }


    /**
     * 获取客户管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(Member member) {
        Map<String, Object> beanMap = BeanKit.beanToMap(member, true);
        EntityWrapper<Member> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
        return memberService.selectList(wrapper);
    }


    /**
     * 新增客户管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Member member) {
        memberService.insert(member);
        return SUCCESS_TIP;
    }

    /**
     * 删除客户管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam String memberIds) {
        if (StrUtil.isNotBlank(memberIds)) {
            memberService.deleteBatchIds(Arrays.asList(memberIds.split(",")));
        }
        return SUCCESS_TIP;
    }

    /**
     * 修改客户管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Member member) {
        memberService.updateById(member);
        return SUCCESS_TIP;
    }

    /**
     * 跳转到修改客户管理
     */
    @RequestMapping("/member_detail/{memberId}")
    public String detail(@PathVariable Integer memberId, Model model) {
        Member member = memberService.selectById(memberId);
        Account account=memberService.getAccount(member.getBizUserId());
        Wrapper wrapper=new EntityWrapper();
        List<Bank> bankList = bankService.selectList(wrapper.eq("master_id",member.getId()).eq("type",1));
        List<ExamineVO> list = examineService.selectlistByIdDesc(memberId.toString());
        for (ExamineVO vo : list
        ) {
            vo.setTypeName("vip审核");
        }
        model.addAttribute("bankList", bankList);
        model.addAttribute("list", list);
        model.addAttribute("item", member);
        model.addAttribute("account", account);
        return PREFIX + "member_detail.html";
    }

    /**
     * 客户管理详情
     */
    @RequestMapping(value = "/detail/{memberId}")
    @ResponseBody
    public Object detail(@PathVariable("memberId") Integer memberId) {
        return memberService.selectById(memberId);
    }


    /**
     * 激活供应商
     */
    @RequestMapping(value = "/active")
    @ResponseBody
    public Object active() {

        List<Member> members = memberService.selectList(null);
        for (Member member : members) {
            if(StrUtil.isEmpty(member.getBizUserId())){
                memberService.active(member);
            }
        }

        return SUCCESS_TIP;
    }
}
