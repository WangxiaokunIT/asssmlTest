package com.xinshang.core.shiro.factory;

import com.xinshang.core.common.constant.state.ManagerState;
import com.xinshang.core.shiro.ShiroUser;
import com.xinshang.core.util.Convert;
import com.xinshang.core.util.SpringContextHolder;
import com.xinshang.core.common.constant.factory.ConstantFactory;
import com.xinshang.modular.system.dao.MenuMapper;
import com.xinshang.modular.system.dao.UserMapper;
import com.xinshang.modular.system.model.User;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangjiajia
 * @date 2018年11月27日 16:54:33
 */
@Service
@DependsOn("springContextHolder")
@Transactional(readOnly = true,rollbackFor = Exception.class)
public class ShiroFactroy implements IShiro {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    public static IShiro me() {
        return SpringContextHolder.getBean(IShiro.class);
    }

    @Override
    public User user(String account) {

        User user = userMapper.getByAccount(account);

        // 账号不存在
        if (null == user) {
            throw new CredentialsException();
        }
        // 账号被冻结
        if (user.getState() != ManagerState.OK.getCode()) {
            throw new LockedAccountException();
        }
        return user;
    }

    @Override
    public ShiroUser shiroUser(User user) {
        ShiroUser shiroUser = new ShiroUser();
        shiroUser.setId(user.getId());
        shiroUser.setAccount(user.getAccount());
        shiroUser.setDeptId(user.getDeptId());
        shiroUser.setDeptName(ConstantFactory.me().getDeptName(user.getDeptId()));
        shiroUser.setName(user.getName());
        shiroUser.setFullName(user.getFullName());
        shiroUser.setRoles(ConstantFactory.me().listRoleByUserId(user.getId()));
        shiroUser.setPositions(ConstantFactory.me().listPositionByUserId(user.getId()));
        return shiroUser;
    }

    @Override
    public List<String> findPermissionsByRoleId(Integer roleId) {
        return menuMapper.getResUrlsByRoleId(roleId);
    }

    @Override
    public String findRoleNameByRoleId(Integer roleId) {
        return ConstantFactory.me().getSingleRoleRemark(roleId);
    }

    @Override
    public SimpleAuthenticationInfo info(ShiroUser shiroUser, User user, String realmName) {
        String credentials = user.getPassword();

        // 密码加盐处理
        String source = user.getSalt();
        ByteSource credentialsSalt = new Md5Hash(source);
        return new SimpleAuthenticationInfo(shiroUser, credentials, credentialsSalt, realmName);
    }

}
