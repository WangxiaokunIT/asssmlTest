package com.xinshang.modular.biz.service.impl;

import com.xinshang.modular.biz.dao.JoininMapper;
import com.xinshang.modular.biz.dao.ProjectMapper;
import com.xinshang.modular.biz.model.ContractInfo;
import com.xinshang.modular.biz.model.Joinin;
import com.xinshang.modular.biz.model.Project;
import com.xinshang.modular.biz.model.RepayPlan;
import com.xinshang.modular.biz.dao.RepayPlanMapper;
import com.xinshang.modular.biz.service.IRepayPlanService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-10-25
 */
@Service
public class RepayPlanServiceImpl extends ServiceImpl<RepayPlanMapper, RepayPlan> implements IRepayPlanService {
    @Autowired
    private RepayPlanMapper repayPlanMapper;
    @Autowired
    private  JoininMapper joininMapper;
    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public boolean updateRepay(Joinin joinin) {
        return repayPlanMapper.updateRepay(joinin);

     }

    @Override
    public boolean createplan(Project project)
    {
        //project=projectMapper.selectById(project.getId());
        //Date date = new Date();

        BigDecimal a=new BigDecimal(0);
        Integer b = new Integer(1);
        ContractInfo contractInfo=joininMapper.joinListByProjectId(project.getId());
        if(contractInfo == null){
             return true;
        }

        if(project.getRepaymentMethod()==1)//一次性还本付息
        {
            for (int j=0;j<contractInfo.getJoininInfos().size();j++) {

                Calendar cal = Calendar.getInstance();
                cal.setTime(project.getStartRecordTime());//设置起时间
                    RepayPlan repayPlan=new RepayPlan();
                    repayPlan.setProjectId(project.getId());
                    repayPlan.setJoininId(contractInfo.getJoininInfos().get(j).getJoinId().intValue());
                    repayPlan.setMemberId(contractInfo.getJoininInfos().get(j).getCustomId().intValue());
                    repayPlan.setLendAmount(contractInfo.getJoininInfos().get(j).getInvestmentAmount());
                    repayPlan.setPaidTim(contractInfo.getJoininInfos().get(j).getInvestmentAmount());
                    repayPlan.setHaveTim(a);
                    if(project.getUnit()=="天") {
                        BigDecimal bd = contractInfo.getJoininInfos().get(j).getInvestmentAmount().multiply(new BigDecimal(project.getRecruitmentCycle())).multiply(project.getEquityRate().divide(new BigDecimal(100))).divide(new BigDecimal(365),2, BigDecimal.ROUND_HALF_UP);
                        repayPlan.setPaidInter(bd);
                    }
                    else
                    {
                        BigDecimal bd = contractInfo.getJoininInfos().get(j).getInvestmentAmount().multiply(new BigDecimal(project.getRecruitmentCycle())).multiply(project.getEquityRate().divide(new BigDecimal(100))).divide(new BigDecimal(12),2, BigDecimal.ROUND_HALF_UP);
                        repayPlan.setPaidInter(bd);
                    }
                    repayPlan.setHaveInter(a);
                    repayPlan.setPaidMort(repayPlan.getPaidTim().add(repayPlan.getPaidInter()));
                    repayPlan.setHaveMort(a);
                    repayPlan.setStatecode(1);
                    repayPlan.setMonths(1);
                    cal.add(Calendar.DATE, project.getRecruitmentCycle());
                    repayPlan.setPaidTimDate(project.getEndRecordTime());
                    repayPlan.setPaidInterDate(cal.getTime());
                    repayPlanMapper.insert((repayPlan));

            }
        }
        else//按月付息到期还本
        {

            for (int j=0;j<contractInfo.getJoininInfos().size();j++) {
                for (int i = 1; i <= project.getRecruitmentCycle(); i++) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(project.getStartRecordTime());//设置起时间
                    RepayPlan repayPlan=new RepayPlan();
                    repayPlan.setProjectId(project.getId());
                    repayPlan.setJoininId(contractInfo.getJoininInfos().get(j).getJoinId().intValue());
                    repayPlan.setMemberId(contractInfo.getJoininInfos().get(j).getCustomId().intValue());
                    repayPlan.setLendAmount(contractInfo.getJoininInfos().get(j).getInvestmentAmount());



                    if(project.getRecruitmentCycle()==i)
                    {
                        repayPlan.setPaidTim(contractInfo.getJoininInfos().get(j).getInvestmentAmount());

                    }
                    else
                    {
                        repayPlan.setPaidTim(a);
                    }
                    repayPlan.setHaveTim(a);
                    if(project.getUnit()=="天") {
                        BigDecimal bd = contractInfo.getJoininInfos().get(j).getInvestmentAmount().multiply(project.getEquityRate().divide(new BigDecimal(100))).divide(new BigDecimal(365),2, BigDecimal.ROUND_DOWN);
                        repayPlan.setPaidInter(bd);
                        if(project.getRecruitmentCycle()==i)
                        {
                            BigDecimal bb=contractInfo.getJoininInfos().get(j).getInvestmentAmount().multiply(new BigDecimal(project.getRecruitmentCycle())).multiply(project.getEquityRate().divide(new BigDecimal(100))).divide(new BigDecimal(365));
                            bb.setScale(2, BigDecimal.ROUND_HALF_UP); //12.35
                            bd=bb.subtract(bd.multiply(new BigDecimal(i-1)));
                            repayPlan.setPaidInter(bd);
                        }
                    }
                    else
                    {
                        BigDecimal bd = contractInfo.getJoininInfos().get(j).getInvestmentAmount().multiply(project.getEquityRate()).divide(new BigDecimal(12),2, BigDecimal.ROUND_DOWN);
                        repayPlan.setPaidInter(bd);
                        if(project.getRecruitmentCycle()==i)
                        {
                            BigDecimal bb=contractInfo.getJoininInfos().get(j).getInvestmentAmount().multiply(new BigDecimal(project.getRecruitmentCycle())).multiply(project.getEquityRate()).divide(new BigDecimal(12),2,BigDecimal.ROUND_HALF_UP);
                            bd=bb.subtract(bd.multiply(new BigDecimal(i-1)));
                            repayPlan.setPaidInter(bd);
                        }
                    }

                    repayPlan.setHaveInter(a);
                    repayPlan.setPaidMort(repayPlan.getPaidTim().add(repayPlan.getPaidInter()));
                    repayPlan.setHaveMort(a);
                    repayPlan.setStatecode(1);
                    repayPlan.setMonths(i);
                    cal.add(Calendar.MONTH, i);
                    repayPlan.setPaidTimDate(project.getEndRecordTime());
                    repayPlan.setPaidInterDate(cal.getTime());
                    repayPlanMapper.insert((repayPlan));
                }
            }
        }

        //repayPlanMapper.updateRepay(joinin);
        return true;
    }

    @Override
    public boolean updateStateById(RepayPlan repayPlan) {
        return repayPlanMapper.updateStateById(repayPlan);
    }

    @Override
    public RepayPlan repayPlanByProjectId(Long projectId) {
        return repayPlanMapper.repayPlanByProjectId(projectId);
    }

    @Override
    public List<RepayPlan> repayPlanList(Long projectId) {
        return repayPlanMapper.repayPlanList(projectId);
    }

    @Override
    public RepayPlan repayPlanByJoinId(Long joininId) {
        return repayPlanMapper.repayPlanByJoinId(joininId);
    }
}
