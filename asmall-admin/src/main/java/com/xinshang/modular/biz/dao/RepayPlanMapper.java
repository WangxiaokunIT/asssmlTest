package com.xinshang.modular.biz.dao;

import com.xinshang.modular.biz.model.Joinin;
import com.xinshang.modular.biz.model.RepayPlan;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-10-25
 */
@Repository
public interface RepayPlanMapper extends BaseMapper<RepayPlan> {
    Boolean  updateRepay(@Param("joinin") Joinin joinin);
    RepayPlan repayPlanByProjectId(@Param("projectId") Long projectId);
    List<RepayPlan> repayPlanList(@Param("projectId") Long projectId);
    Boolean  updateStateById(@Param("repayPlan") RepayPlan repayPlan);
    RepayPlan repayPlanByJoinId(@Param("joininId") Long joininId);

}
