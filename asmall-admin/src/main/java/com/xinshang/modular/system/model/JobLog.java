package com.xinshang.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 定时任务调度日志表
 * </p>
 *
 * @author 王晓坤
 * @since 2018-08-06
 */

@Data
@TableName("sys_job_log")
public class JobLog extends Model<JobLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 任务日志ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
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
     * 任务描述
     */
    private String description;

    /**
     * 任务方法
     */
    @TableField("job_class_name")
    private String jobClassName;
    /**
     * 方法参数
     */
    private String params;
    /**
     * 日志信息
     */
    @TableField("job_message")
    private String jobMessage;
    /**
     * 执行状态（0成功 1失败）
     */
    private String state;
    /**
     * 异常信息
     */
    @TableField("exception_info")
    private String exceptionInfo;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
