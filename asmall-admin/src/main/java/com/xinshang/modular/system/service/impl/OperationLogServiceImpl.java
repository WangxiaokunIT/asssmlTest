package com.xinshang.modular.system.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.modular.system.dao.OperationLogMapper;
import com.xinshang.modular.system.model.OperationLog;
import com.xinshang.modular.system.service.IOperationLogService;
import com.xinshang.modular.system.dao.OperationLogMapper;
import com.xinshang.modular.system.model.OperationLog;
import com.xinshang.modular.system.service.IOperationLogService;
import com.xinshang.modular.system.dao.OperationLogMapper;
import com.xinshang.modular.system.model.OperationLog;
import com.xinshang.modular.system.service.IOperationLogService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 操作日志 服务实现类
 * </p>
 *
 * @author zhangjiajia
 * @since 2018-02-22
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements IOperationLogService {

    @Override
    public List<Map<String, Object>> getOperationLogs(Page<OperationLog> page, String beginTime, String endTime, String logName, String s, String orderByField, boolean asc) {
        return this.baseMapper.getOperationLogs(page, beginTime, endTime, logName, s, orderByField, asc);
    }
}
