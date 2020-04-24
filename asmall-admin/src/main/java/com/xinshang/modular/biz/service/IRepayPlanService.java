package com.xinshang.modular.biz.service;

import com.xinshang.modular.biz.model.Joinin;
import com.xinshang.modular.biz.model.Project;
import com.xinshang.modular.biz.model.RepayPlan;
import com.baomidou.mybatisplus.service.IService;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-10-25
 */
public interface IRepayPlanService extends IService<RepayPlan> {

    boolean updateRepay(Joinin Joinin);

    boolean createplan(Project project);

    boolean updateStateById(RepayPlan repayPlan);

    /**
     * 查询还款计划最后一条记录
     * @param projectId
     * @return
     */
    RepayPlan repayPlanByProjectId(@Param("projectId") Long projectId);

    /**
     * 查询已还利息
     * @param projectId
     * @return
     */
    List<RepayPlan> repayPlanList(@Param("projectId") Long projectId);

    RepayPlan repayPlanByJoinId(@Param("joininId") Long joininId);

}
