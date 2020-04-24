package com.xinshang.modular.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.xinshang.modular.system.model.Notice;
import com.xinshang.modular.system.model.Notice;
import com.xinshang.modular.system.model.Notice;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 通知表 服务类
 * </p>
 *
 * @author zhangjiajia
 * @since 2018-02-22
 */
public interface INoticeService extends IService<Notice> {

    /**
     * 获取通知列表
     */
    List<Map<String, Object>> list(String condition);
}
