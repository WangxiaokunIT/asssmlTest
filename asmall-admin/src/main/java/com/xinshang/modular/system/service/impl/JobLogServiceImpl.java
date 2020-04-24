package com.xinshang.modular.system.service.impl;

import com.xinshang.modular.system.dao.JobLogMapper;
import com.xinshang.modular.system.model.JobLog;
import com.xinshang.modular.system.service.IJobLogService;
import com.xinshang.modular.system.model.JobLog;
import com.xinshang.modular.system.dao.JobLogMapper;
import com.xinshang.modular.system.service.IJobLogService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 定时任务调度日志表 服务实现类
 * </p>
 *
 * @author 王晓坤
 * @since 2018-08-06
 */
@Service
public class JobLogServiceImpl extends ServiceImpl<JobLogMapper, JobLog> implements IJobLogService {

}
