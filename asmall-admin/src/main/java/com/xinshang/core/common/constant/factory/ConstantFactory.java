package com.xinshang.core.common.constant.factory;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xinshang.constant.Constants;
import com.xinshang.core.beetl.EncryptionExt;
import com.xinshang.core.common.constant.cache.Cache;
import com.xinshang.core.common.constant.cache.CacheKey;
import com.xinshang.core.log.LogObjectHolder;
import com.xinshang.core.support.StrKit;
import com.xinshang.core.util.Convert;
import com.xinshang.core.util.RedisUtil;
import com.xinshang.core.util.SpringContextHolder;
import com.xinshang.core.util.ToolUtil;
import com.xinshang.modular.system.dao.*;
import com.xinshang.modular.system.model.*;
import com.xinshang.modular.system.transfer.UserPosition;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 常量的生产工厂
 *
 * @author fengshuonan
 * @date 2017年2月13日 下午10:55:21
 */
@Component
@DependsOn("springContextHolder")
public class ConstantFactory implements IConstantFactory {
    private SystemMapper systemMapper = SpringContextHolder.getBean(SystemMapper.class);
    private RoleMapper roleMapper = SpringContextHolder.getBean(RoleMapper.class);
    private DeptMapper deptMapper = SpringContextHolder.getBean(DeptMapper.class);
    private DictMapper dictMapper = SpringContextHolder.getBean(DictMapper.class);
    private UserMapper userMapper = SpringContextHolder.getBean(UserMapper.class);
    private MenuMapper menuMapper = SpringContextHolder.getBean(MenuMapper.class);
    private PositionMapper positionMapper = SpringContextHolder.getBean(PositionMapper.class);
    private NoticeMapper noticeMapper = SpringContextHolder.getBean(NoticeMapper.class);
    private RoleUserMapper roleUserMapper = SpringContextHolder.getBean(RoleUserMapper.class);

    private RedisUtil redisUtil = SpringContextHolder.getBean(RedisUtil.class);
    public static IConstantFactory me() {
        return SpringContextHolder.getBean("constantFactory");
    }


    /**
     * 根据用户id获取用户名称
     *
     * @author zhangjiajia
     * @date 2017/5/9 23:41
     */
    @Override
    public String getUserNameById(Integer userId) {
        User user = userMapper.selectById(userId);
        if (user != null) {
            return user.getName();
        } else {
            return "--";
        }
    }

    /**
     * 根据用户id获取用户角色集合
     *
     * @param userId
     * @return
     */
    @Override
    public List<Role> listRoleByUserId(Integer userId) {
        return  roleMapper.listByUserId(userId);
    }

    /**
     * 根据用户id获取用户职位集合
     *
     * @param userId
     * @return
     */
    @Override
    public List<Position> listPositionByUserId(Integer userId) {
        return positionMapper.listByUserId(userId);
    }

    /**
     * 根据表名和关联字段名查询出要获取的字段名
     * @param tableName  表名
     * @param relationColumn  关联字段名
     * @param targetColumn   要获取字段名
     * @return
     */
    @Override
    public List<GeneralTable> findInTable(String tableName, String relationColumn, String targetColumn) {
        GeneralTable generalTable = new GeneralTable();
        generalTable.setRelationColumn(relationColumn);
        generalTable.setTargetColumn(targetColumn);
        generalTable.setTableName(tableName);
        return systemMapper.getByTableAndField(generalTable);
    }

    /**
     * 根据表名和关联字段名查询出要获取的字段名
     * @param generalTable
     * @return
     */
    @Override
    public List<GeneralTable> findInTable(GeneralTable generalTable) {
        generalTable.setTableName(EncryptionExt.decrypt(generalTable.getTableName()));
        generalTable.setRelationColumn(EncryptionExt.decrypt(generalTable.getRelationColumn()));
        generalTable.setTargetColumn(EncryptionExt.decrypt(generalTable.getTargetColumn()));
        generalTable.setWhereColumn(EncryptionExt.decrypt(generalTable.getWhereColumn()));
        return systemMapper.getByTableAndField(generalTable);
    }

    /**
     * 根据用户id查询其角色名称
     * @param userId
     * @return
     */
    @Override
    public String getUserRoleName(Integer userId) {
        return roleUserMapper.getUserRoleName(userId);
    }

    /**
     * 根据用户id获取用户职位名字,号分割
     *
     * @param userId
     * @return
     */
    @Override
    public String getPositionNameByUserId(Integer userId) {
        List<Position> positions = positionMapper.listByUserId(userId);
        StringBuilder sb = new StringBuilder();
        positions.forEach(i->{
            sb.append(i.getName()).append(",");
        });
        return StrKit.removeSuffix(sb.toString(), ",");
    }

    /**
     * 根据用户id获取用户账号
     *
     * @author zhangjiajia
     * @date 2017年5月16日21:55:371
     */
    @Override
    public String getUserAccountById(Integer userId) {
        User user = userMapper.selectById(userId);
        if (user != null) {
            return user.getAccount();
        } else {
            return "--";
        }
    }


    /**
     * 通过角色id获取角色名称
     */
    @Override
    @Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.SINGLE_ROLE_NAME + "'+#roleId")
    public String getSingleRoleName(Integer roleId) {
        if (0 == roleId) {
            return "--";
        }
        Role roleObj = roleMapper.selectById(roleId);
        if (ToolUtil.isNotEmpty(roleObj) && ToolUtil.isNotEmpty(roleObj.getName())) {
            return roleObj.getName();
        }
        return "";
    }

    /**
     * 通过角色id获取角色英文名称
     */
    @Override
    @Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.SINGLE_ROLE_TIP + "'+#roleId")
    public String getSingleRoleRemark(Integer roleId) {
        if (0 == roleId) {
            return "--";
        }
        Role roleObj = roleMapper.selectById(roleId);
        if (ToolUtil.isNotEmpty(roleObj) && ToolUtil.isNotEmpty(roleObj.getName())) {
            return roleObj.getRemark();
        }
        return "";
    }

    /**
     * 获取部门名称
     */
    @Override
    @Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.DEPT_NAME + "'+#deptId")
    public String getDeptName(Integer deptId) {
        Dept dept = deptMapper.selectById(deptId);
        if (ToolUtil.isNotEmpty(dept) && ToolUtil.isNotEmpty(dept.getFullName())) {
            return dept.getFullName();
        }
        return "";
    }

    /**
     * 获取获取职位名称
     */
    @Override
    @Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.POSITION_NAME + "'+#positionId")
    public String getPositionName(Integer positionId) {
        Position position = positionMapper.selectById(positionId);
        if (ToolUtil.isNotEmpty(position) && ToolUtil.isNotEmpty(position.getName())) {
            return position.getName();
        }
        return "";
    }


    /**
     * 获取菜单的名称们(多个)
     */
    @Override
    public String getMenuNames(String menuIds) {
        Integer[] menus = Convert.toIntArray(menuIds);
        StringBuilder sb = new StringBuilder();
        for (int menu : menus) {
            Menu menuObj = menuMapper.selectById(menu);
            if (ToolUtil.isNotEmpty(menuObj) && ToolUtil.isNotEmpty(menuObj.getName())) {
                sb.append(menuObj.getName()).append(",");
            }
        }
        return StrKit.removeSuffix(sb.toString(), ",");
    }

    /**
     * 获取菜单名称
     */
    @Override
    public String getMenuName(Integer menuId) {
        if (ToolUtil.isEmpty(menuId)) {
            return "";
        } else {
            Menu menu = menuMapper.selectById(menuId);
            if (menu == null) {
                return "";
            } else {
                return menu.getName();
            }
        }
    }

    /**
     * 获取菜单名称通过编号
     */
    @Override
    public String getMenuNameByCode(String code) {
        if (ToolUtil.isEmpty(code)) {
            return "";
        } else {
            Menu param = new Menu();
            param.setCode(code);
            Menu menu = menuMapper.selectOne(param);
            if (menu == null) {
                return "";
            } else {
                return menu.getName();
            }
        }
    }

    /**
     * 获取通知标题
     */
    @Override
    public String getNoticeTitle(Integer dictId) {
        if (ToolUtil.isEmpty(dictId)) {
            return "";
        } else {
            Notice notice = noticeMapper.selectById(dictId);
            if (notice == null) {
                return "";
            } else {
                return notice.getTitle();
            }
        }
    }


    /**
     * 查询所有的用户
     */
    @Override
    public List<User> listAllUserIdAndName() {
        List<User> users = userMapper.selectList(new EntityWrapper<User>().setSqlSelect("id","name").ne("state",3));
        return users;
    }

    /**
     * 如果redis中没有就从数据库中查询出来放到redis中
     */
    @Override
    public void cacheDict() {
        //如果redis中没有就从数据库中查询出来放到redis中
        if(!redisUtil.hasKey(Constants.asmall_SYSTEM_DICT_KEY)){
            List<Map<String, Object>> list = dictMapper.listAllDictCodeAndName();
            Map<String,Object> dictTempMap = new LinkedHashMap<>();
            for(Map dict : list){
                dictTempMap.put(dict.get("pcode").toString()+Constants.asmall_REDIS_KEY_SEPARATOR_SYMBOL+dict.get("code").toString(),dict.get("name"));
            }
            redisUtil.hmset(Constants.asmall_SYSTEM_DICT_KEY,dictTempMap);
        }
    }

    /**
     * 根据code获取字典项
     *
     * @param code
     * @return
     */
    @Override
    public List<Dict> listDictByCode(String code) {
        return dictMapper.selectByParentCode(code);
    }



    /**
     * 获取被缓存的对象(用户删除业务)
     */
    @Override
    public String getCacheObject(String para) {
        return LogObjectHolder.me().get().toString();
    }

    /**
     * 获取子部门id
     */
    @Override
    public List<Integer> getSubDeptId(Integer deptId) {
        Wrapper<Dept> wrapper = new EntityWrapper<>();
        wrapper = wrapper.like("seq",  "."+deptId + ".");
        List<Dept> depts = this.deptMapper.selectList(wrapper);
        ArrayList<Integer> deptIds = new ArrayList<>();
        if(depts != null && depts.size() > 0){
            for (Dept dept : depts) {
                deptIds.add(dept.getId());
            }
        }
        return deptIds;
    }

    /**
     * 获取所有父部门id
     */
    @Override
    public List<Integer> getParentDeptIds(Integer deptId) {
        Dept dept = deptMapper.selectById(deptId);
        String seq = dept.getSeq();
        String[] split = seq.split(".");
        ArrayList<Integer> parentDeptIds = new ArrayList<>();
        for (String s : split) {
            parentDeptIds.add(Integer.valueOf(s));
        }
        return parentDeptIds;
    }

    /**
     * 根据id和parentId更新自己的seq、level
     * @param tableName 表名字
     * @param id  编码
     * @param parentId  上级编码
     */
    @Override
    public void addSeqAndLevelByIdAndParentId(String tableName, Integer id, Integer parentId){
        systemMapper.addSeqAndLevelByIdAndParentId(tableName,id,parentId);
    }

    /**
     * 根据id更新自己和自己下面的seq、level
     * @param tableName 表名字
     * @param id  编码
     */
    @Override
    public void updateSeqAndLevelById(String tableName,Integer id){
        systemMapper.updateSeqAndLevelById(tableName,id);
    }

    /**
     * 更新全表seq、level
     * @param tableName 表名字
     */
    @Override
    public void updateAllSeqAndLevel(String tableName){
        systemMapper.updateAllSeqAndLevel(tableName);
    }

    /**
     * 根据职位id查询该职位的人员及子职位下面的人员
     * @param positionId
     * @return
     */
    @Override
    public List<UserPosition> getPositionUserByPositionId(Integer positionId){
        return systemMapper.getPositionUserByPositionId(positionId);
    }


    /**
     * 根据用户id查询该用户所在职位的人员及子职位下面的人员
     * @param userId
     * @return
     */
    @Override
    public List<UserPosition> getPositionUserByUserId(Integer userId){
        return systemMapper.getPositionUserByUserId(userId);
    }



    /**
     * 判断用户职位是否该职位
     * @param userId
     * @param positionCode
     * @return
     */
    @Override
    public int judgeUserHasPositionByUserIdAndPositionCode(Integer userId,String positionCode){
        return systemMapper.judgeUserHasPositionByUserIdAndPositionCode(userId,positionCode);
    }

    /**
     * 该职位相同的人
     * @param positionCode
     * @return
     */
    @Override
    public List<UserPosition> getSamePositionUserByPositionCode(String positionCode){
        return systemMapper.getSamePositionUserByPositionCode(positionCode);
    }

    @Override
    public List<UserPosition> getSamePositionUserByPositionCodes(String positionCodes){
        return systemMapper.getSamePositionUserByPositionCodes(positionCodes);
    }


}
