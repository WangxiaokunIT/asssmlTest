package com.xinshang.modular.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xinshang.core.datascope.DataScope;
import com.xinshang.core.node.ZTreeNode;
import com.xinshang.modular.system.dao.UserMapper;
import com.xinshang.modular.system.model.User;
import com.xinshang.modular.system.service.IUserService;
import com.xinshang.core.datascope.DataScope;
import com.xinshang.modular.system.dao.UserMapper;
import com.xinshang.modular.system.model.User;
import com.xinshang.modular.system.service.IUserService;
import com.xinshang.core.datascope.DataScope;
import com.xinshang.modular.system.dao.UserMapper;
import com.xinshang.modular.system.model.User;
import com.xinshang.modular.system.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author zhangjiajia
 * @since 2018-02-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public int setState(Integer userId, int state) {
        return this.baseMapper.setState(userId, state);
    }

    @Override
    public int changePwd(Integer userId, String pwd) {
        return this.baseMapper.changePwd(userId, pwd);
    }

    @Override
    public List<User> selectUsers(DataScope dataScope, String name, String beginTime, String endTime, Integer deptId,Integer positionId) {
        return this.baseMapper.selectUsers(dataScope, name, beginTime, endTime, deptId,positionId);
    }

    @Override
    public User getByAccount(String account) {
        return this.baseMapper.getByAccount(account);
    }

    @Override
    public List<User> selectDeptUser(Integer deptId) {
       return this.baseMapper.selectDeptUser(deptId);
    }

}
