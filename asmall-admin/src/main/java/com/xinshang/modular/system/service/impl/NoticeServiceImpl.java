package com.xinshang.modular.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.modular.system.dao.NoticeMapper;
import com.xinshang.modular.system.model.Notice;
import com.xinshang.modular.system.service.INoticeService;
import com.xinshang.modular.system.dao.NoticeMapper;
import com.xinshang.modular.system.model.Notice;
import com.xinshang.modular.system.service.INoticeService;
import com.xinshang.modular.system.dao.NoticeMapper;
import com.xinshang.modular.system.model.Notice;
import com.xinshang.modular.system.service.INoticeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 通知表 服务实现类
 * </p>
 *
 * @author zhangjiajia
 * @since 2018-02-22
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {

    @Override
    public List<Map<String, Object>> list(String condition) {
        return this.baseMapper.list(condition);
    }
}
