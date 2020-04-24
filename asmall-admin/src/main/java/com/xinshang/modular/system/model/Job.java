package com.xinshang.modular.system.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import lombok.Data;
import java.io.Serializable;

/**
 * @author zhangjiajia
 * @date 2019年2月15日 17:01:48
 */
@Data
public class Job extends Model<Job> {

    private static final long serialVersionUID = 1L;

    /**
     * 触发器名称
     */
    @TableField("trigger_name")
    private String triggerName;

    /**
     * 触发器组名
     */
    @TableField("trigger_group")
    private String triggerGroup;

    /**
     * 任务名称
     */
    @TableField("job_name")
    private String jobName;

    /**
     * 任务组名
     */
    @TableField("job_group")
    private String jobGroup;

    /**
     * 任务处理类名
     */
    @TableField("job_class_name")
    private String jobClassName;

    /**
     * 上次执行时间
     */
    @TableField("prev_fire_time")
    private String prevFireTime;

    /**
     * 下次执行时间
     */
    @TableField("next_fire_time")
    private String nextFireTime;


    /**
     * cron执行表达式
     */
    @TableField("cron_expression")
    private String cronExpression;

    /**
     * 状态（0作废 1正常 2暂停 3继续）
     */
    @TableField("trigger_state")
    private String triggerState;

    /**
     * 描述
     */
    private String description;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
