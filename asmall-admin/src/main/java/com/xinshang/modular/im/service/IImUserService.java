package com.xinshang.modular.im.service;

import com.xinshang.modular.im.model.ImUser;
import com.baomidou.mybatisplus.service.IService;


/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author zhangjiajia
 * @since 2019-07-05
 */
public interface IImUserService extends IService<ImUser> {
    /**
     * 添加用户
     * @param imUser
     * @return
     */
    Boolean addUser(ImUser imUser);

}
