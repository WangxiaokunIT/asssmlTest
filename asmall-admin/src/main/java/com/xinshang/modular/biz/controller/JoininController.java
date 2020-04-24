package com.xinshang.modular.biz.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xinshang.constant.Constants;
import com.xinshang.core.base.controller.BaseController;
import com.xinshang.core.base.tips.Tip;
import com.xinshang.modular.biz.dto.AllinPayResponseDTO;
import com.xinshang.modular.biz.dto.JoininDTO;
import com.xinshang.modular.biz.model.*;
import com.xinshang.modular.biz.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import com.xinshang.core.support.BeanKit;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.core.common.constant.factory.PageFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import com.xinshang.core.log.LogObjectHolder;


/**
 * @title:加盟信息控制器
 *
 * @author: zhoushuai
 * @since: 2019-10-23 10:48:45
 */
@Controller
@Slf4j
@RequestMapping("/joinin")
public class JoininController extends BaseController {

    private String PREFIX = "/biz/joinin/";

    @Autowired
    private IJoininService joininService;
    @Autowired
    private IProjectService projectService;

    @Autowired
    private IRepayPlanService repayPlanService;
    @Autowired
    private IEqbContractService eqbContractService;

    /**
     * 跳转到加盟信息首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "joinin.html";
    }

    /**
     * 跳转到添加加盟信息
     */
    @RequestMapping("/joinin_add")
    public String joininAdd() {
        return PREFIX + "joinin_add.html";
    }

    /**
     * 跳转到修改加盟信息
     */
    @RequestMapping("/joinin_update/{joininId}")
    public String joininUpdate(@PathVariable Integer joininId, Model model) {
        Joinin joinin = joininService.selectById(joininId);
        model.addAttribute("item",joinin);
        LogObjectHolder.me().set(joinin);
        return PREFIX + "joinin_edit.html";
    }

    /**
     * 跳转到修改加盟信息
     */
    @RequestMapping("/contract/{projectId}/{memberId}")
    public String contract(@PathVariable Integer projectId,@PathVariable Integer memberId, Model model) {
        EntityWrapper<EqbContract> wrapper1 = new EntityWrapper<>();
        wrapper1.eq("project_id",projectId);
        wrapper1.eq("memberId",memberId);
       EqbContract eqbContract= eqbContractService.selectOne(wrapper1);

         return  "redirect:"+eqbContract.getContractUrl();
    }

    /**
     * 获取加盟信息分页列表
     */
    @RequestMapping(value = "/pageList")
    @ResponseBody
    public Object pageList(JoininDTO joininDTO) {
        Page<JoininInfo> page = new PageFactory<JoininInfo>().defaultPage();
        page.setRecords(joininService.joinList(joininDTO, page));
        return page;
    }

    /**
     * 获取加盟信息列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(Joinin joinin) {
        Map<String, Object> beanMap = BeanKit.beanToMap(joinin,true);
        EntityWrapper<Joinin> wrapper = new EntityWrapper<>();
        wrapper.allEq(beanMap);
         return joininService.selectList(wrapper);
    }

    /**
     * 使用余额支付密码确认同步结果
     * @return
     */
    @GetMapping("/goPayPwdResult")
    public String goSetPayPwdResult(AllinPayResponseDTO<String> allinPayResponseDTO, Model model) {
        log.info("使用余额支付密码确认返回结果:{}", allinPayResponseDTO);
        JSONObject signedValue = JSON.parseObject(allinPayResponseDTO.getSignedValue());
        String payStatus = signedValue.get("payStatus").toString();

        if (Constants.STATUS_FAIL_CODE.equals(payStatus)) {
            model.addAttribute("result", signedValue.get("result"));
            model.addAttribute("msg", signedValue.get("payFailMessage"));
        } else {
            model.addAttribute("result", signedValue.get("result"));
            model.addAttribute("msg", "支付成功");
        }

        return PREFIX + "allin_pay_sync_result.html";
    }

    /**
     * 还款
     */
    @RequestMapping(value = "/repayment")
    @ResponseBody
    public Map<String,String> repayment(@RequestParam String joininIds,@RequestParam String moneys,@RequestParam String projectId) {
        Map<String,String> map = new HashMap(3);
        String[] moneysItems = moneys.split(",");

        BigDecimal money=new BigDecimal(0);
        for(int i=0;i<moneysItems.length;i++){
            money=money.add(new BigDecimal(moneysItems[i]));
        }

        Project project=projectService.selectById(projectId);
        if(project.getState()!=7){
             map.put("message","招募未完成");
             map.put("state","0");
            return map;
        }


        return  joininService.payGoods(joininIds,Long.parseLong(projectId),money);
    }



    /**
     * 还款确认
     * @param joininDTO
     * @return
     */
    @PostMapping("/depositConfirmPayment")
    @ResponseBody
    public Tip depositConfirmPayment(JoininDTO joininDTO) {
        log.info("还款确认{}",joininDTO);
        return joininService.payConfirm(joininDTO);
    }

    /**
     * 跳转到修改招募信息申请
     */
    @RequestMapping("/repayPlanDetial/{projectId}/{id}")
    public String repayPlanDetial(@PathVariable Long projectId, @PathVariable Long id, Model model) {
        model.addAttribute("projectId",projectId);
        model.addAttribute("joinId",id);
        return PREFIX + "repayplan.html";
    }

    /**
     * 招募信息申请详情
     */
    @RequestMapping(value = "/detail/{projectId}/{id}")
    @ResponseBody
    public Object detail(@PathVariable("projectId") Integer projectId, @PathVariable Integer id) {
        EntityWrapper<RepayPlan> wrapper = new EntityWrapper<>();
        wrapper.eq("project_id",projectId);
        wrapper.eq("joinin_id",id);
        wrapper.eq("is_delete",0);
        List<RepayPlan> repayPlanList= repayPlanService.selectList(wrapper);
        BigDecimal paidMort= new BigDecimal("0.00");//应还本息
        BigDecimal haveMort= new BigDecimal("0.00");//实还本息
        BigDecimal paidTim= new BigDecimal("0.00");//应还本金
        BigDecimal haveTim= new BigDecimal("0.00");//实还本金
        BigDecimal paidInter= new BigDecimal("0.00");//应还利息
        BigDecimal haveInter= new BigDecimal("0.00");//实还利息
            for(int i=0;i<repayPlanList.size();i++){
            BigDecimal paidMort1=repayPlanList.get(i).getPaidMort();
            BigDecimal haveMort1=repayPlanList.get(i).getHaveMort();
            BigDecimal paidTim1=repayPlanList.get(i).getPaidTim();
            BigDecimal haveTim1=repayPlanList.get(i).getHaveTim();
            BigDecimal paidInter1=repayPlanList.get(i).getPaidInter();
            BigDecimal haveInter1=repayPlanList.get(i).getHaveInter();

            paidMort=paidMort.add(paidMort1);
            haveMort=haveMort.add(haveMort1);
            paidTim=paidTim.add(paidTim1);
            haveTim= haveTim.add(haveTim1);
            paidInter= paidInter.add(paidInter1);
            haveInter=haveInter.add(haveInter1);
        }
        RepayPlan repayPlan=new RepayPlan();
        repayPlan.setPaidMort(paidMort);
        repayPlan.setHaveMort(haveMort);
        repayPlan.setPaidTim(paidTim);
        repayPlan.setHaveTim(haveTim);
        repayPlan.setPaidInter(paidInter);
        repayPlan.setHaveInter(haveInter);
        return repayPlan;
    }

    /**
     * 招募详情中显示列表
     * @param projectId
     * @return
     */
    @RequestMapping(value = "/detail_project/{projectId}")
    @ResponseBody
    public Object detailProject(@PathVariable("projectId") Long projectId) {
        return joininService.showJoinin(projectId);
    }


    /**
     * 跳转提前结清页面
     */
    @RequestMapping("/tqSettle/{projectId}")
    public String tqSettle(@PathVariable Long projectId, Model model) {
         try {
            RepayPlan repayPlan;
            Project project = projectService.selectById(projectId);
             repayPlan = repayPlanService.repayPlanByProjectId(projectId);
            //日利率
             BigDecimal interestlv = project.getEquityRate().divide(new BigDecimal(365),20,BigDecimal.ROUND_HALF_UP);
             BigDecimal interest=interestlv.multiply(repayPlan.getLendAmount());
            //设置日期格式
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            //获取系统当前时间
            String date = df.format(new Date());
            //代理开始时间
            String paidTimDate = df.format(project.getStartRecordTime());
            Calendar cal = Calendar.getInstance();
            //字符串的日期格式的计算
            cal.setTime(df.parse(date));
            long time1 = cal.getTimeInMillis();
            cal.setTime(df.parse(paidTimDate));
            long time2 = cal.getTimeInMillis();
            long between_days = (time1 - time2) / (1000 * 3600 * 24);
            Integer days = Integer.parseInt(String.valueOf(between_days));
            //一次性还本付息
            if (project.getRepaymentMethod().equals("1")) {
                repayPlan.setPaidInter(interest.multiply(new BigDecimal(days)));
                log.info("计算应还利息公式(应还利息=权益值*投资金额*投资天数(当前时间-代理开始时间)):{},{},{}",project.getEquityRate().divide(new BigDecimal(365),20,BigDecimal.ROUND_HALF_UP),repayPlan.getLendAmount(),new BigDecimal(days));

            } else {
                List<RepayPlan> repayPlanList = repayPlanService.repayPlanList(projectId);
                BigDecimal haveInter = new BigDecimal("0.00");
                for (int i = 0; i < repayPlanList.size(); i++) {
                    haveInter = haveInter.add(repayPlanList.get(i).getHaveInter());
                }
                repayPlan.setPaidInter(interest.multiply(new BigDecimal(days)).subtract(haveInter));
                log.info("计算应还利息公式(应还利息=日权益值*投资金额*投资天数(当前时间-代理开始时间)-实还利息):{},{},{},{}",project.getEquityRate().divide(new BigDecimal(365),20,BigDecimal.ROUND_HALF_UP),repayPlan.getLendAmount(),new BigDecimal(days),haveInter);
                log.info("计算结果:{}*{}*{}-{}={}",project.getEquityRate().divide(new BigDecimal(365),20,BigDecimal.ROUND_HALF_UP),repayPlan.getLendAmount(),new BigDecimal(days),haveInter,repayPlan.getPaidInter());
            }
            model.addAttribute("item", repayPlan);
            LogObjectHolder.me().set(repayPlan);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return PREFIX + "tqSettle.html";
    }

    /**
     * 提前还款
     */
    @RequestMapping(value = "/tqUpdate")
    @ResponseBody
    public Object tqUpdate(RepayPlan repayPlan) {
        BigDecimal haveInter=repayPlan.getHaveInter();
         repayPlan = repayPlanService.repayPlanByProjectId(repayPlan.getProjectId());
        repayPlan.setStatecode(3);

        //修改还款计划状态
        repayPlanService.updateStateById(repayPlan);
        repayPlan.setPaidInter(haveInter);
        repayPlan.setHaveInterDate(new  Date());
        repayPlan.setHaveInter(haveInter);
        repayPlan.setPaidMort(haveInter.add(repayPlan.getPaidTim()));
        repayPlan.setHaveMort(haveInter.add(repayPlan.getPaidTim()));
        repayPlanService.updateById(repayPlan);
        return SUCCESS_TIP;
    }
}
