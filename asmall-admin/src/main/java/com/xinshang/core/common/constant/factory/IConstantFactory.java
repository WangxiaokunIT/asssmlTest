package com.xinshang.core.common.constant.factory;

import com.xinshang.modular.system.model.*;
import com.xinshang.modular.system.transfer.UserPosition;

import java.util.List;
import java.util.Map;

/**
 * 常量生产工厂的接口
 *
 * @author fengshuonan
 * @date 2017-06-14 21:12
 */
public interface IConstantFactory {

    /**
     * 根据用户id获取用户名称
     *
     * @author zhangjiajia
     * @date 2017/5/9 23:41
     */
    String getUserNameById(Integer userId);

    /**
     * 根据用户id获取用户账号
     *
     * @author zhangjiajia
     * @date 2017年5月16日21:55:371
     */
    String getUserAccountById(Integer userId);


    /**
     * 通过角色id获取角色名称
     */
    String getSingleRoleName(Integer roleId);

    /**
     * 根据用户id获取用户职位名字,号分割
     * @param userId
     * @return
     */
    String getPositionNameByUserId(Integer userId);

    /**
     * 通过角色id获取角色英文名称
     */
    String getSingleRoleRemark(Integer roleId);

    /**
     * 获取部门名称
     */
    String getDeptName(Integer deptId);
    /**
     * 获取职位名称
     */
    String getPositionName(Integer positionId);

    /**
     * 获取菜单的名称们(多个)
     */
    String getMenuNames(String menuIds);

    /**
     * 获取菜单名称
     */
    String getMenuName(Integer menuId);

    /**
     * 获取菜单名称通过编号
     */
    String getMenuNameByCode(String code);

    /**
     * 获取通知标题
     */
    String getNoticeTitle(Integer dictId);

    /**
     * 获取被缓存的对象(用户删除业务)
     */
    String getCacheObject(String para);

    /**
     * 获取子部门id
     */
    List<Integer> getSubDeptId(Integer deptId);

    /**
     * 获取所有父部门id
     */
    List<Integer> getParentDeptIds(Integer deptId);

    /**
     * 根据id和parentId更新自己的seq、level
     * @param tableName 表名字
     * @param id  编码
     * @param parentId  上级编码
     */
    void addSeqAndLevelByIdAndParentId(String tableName, Integer id, Integer parentId);

    /**
     * 根据id更新自己和自己下面的seq、level
     * @param tableName 表名字
     * @param id  编码
     */
    void updateSeqAndLevelById(String tableName,Integer id);

    /**
     * 更新全表seq、level
     * @param tableName 表名字
     */
    void updateAllSeqAndLevel(String tableName);

    /**
     * 根据职位id查询该职位的人员及子职位下面的人员
     * @param positionId
     * @return
     */
    List<UserPosition> getPositionUserByPositionId(Integer positionId);

    /**
     * 根据用户id查询该用户所在职位的人员及子职位下面的人员
     * @param userId
     * @return
     */
    List<UserPosition> getPositionUserByUserId(Integer userId);

    /**
     * 判断用户职位是否该职位
     * @param userId
     * @param positionCode
     * @return
     */
    int judgeUserHasPositionByUserIdAndPositionCode(Integer userId,String positionCode);

    /**
     * 该职位相同的人
     * @param positionCode
     * @return
     */
    List<UserPosition> getSamePositionUserByPositionCode(String positionCode);

    /**
     * 该职位相同的人
     * @param positionCodes 多个code
     * @return
     */
    List<UserPosition> getSamePositionUserByPositionCodes(String positionCodes);


    /**
     * 根据用户id获取用户职位集合
     *
     * @param userId
     * @return
     */
    List<Position> listPositionByUserId(Integer userId);


    /**
     * 根据表名和关联字段名查询出要获取的字段名
     * @param tableName  表名
     * @param relationColumn  关联字段名
     * @param targetColumn   要获取字段名
     * @return
     */
    List<GeneralTable> findInTable(String tableName, String relationColumn, String targetColumn);

    /**
     * 根据表名和关联字段名查询出要获取的字段名
     * @param generalTable
     * @return
     */
    List<GeneralTable> findInTable(GeneralTable generalTable);


    /**
     * 根据用户id获取其角色名称
     * @param userId
     * @return
     */
    String getUserRoleName(Integer userId);

    /**
     * 根据用户id获取角色
     * @param userId
     * @return
     */
    List<Role> listRoleByUserId(Integer userId);

    /**
     * 查询所有的用户
     */
    List<User> listAllUserIdAndName();

    /**
     * 如果redis中没有就从数据库中查询出来放到redis中
     */
    void cacheDict();

    /**
     * 根据code获取字典项
     * @param code
     * @return
     */
    List<Dict> listDictByCode(String code);

}
