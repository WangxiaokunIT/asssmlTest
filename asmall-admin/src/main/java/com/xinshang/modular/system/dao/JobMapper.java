package com.xinshang.modular.system.dao;

import com.xinshang.modular.system.model.Job;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @author: 王晓坤
 * @date: 2018/8/3 13:21
 * @desc: 定时任务调度表 Mapper 接口
 */
public interface JobMapper extends BaseMapper<Job> {


    /**
     * 根据className和groupName获取任务
     * @param jobClassName
     * @param jobGroup
     * @return
     */
    Job getJobByJobClassNameAndJobGroup(@Param("jobClassName") String jobClassName,@Param("jobGroup") String jobGroup);

    /**
     * 根据条件查询任务
     * @param condition
     * @return
     */
    List<Job> listByCondition(@Param("condition") String condition);

}
