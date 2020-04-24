package com.xinshang.modular.system.dao;

import com.xinshang.modular.system.model.GeneralTable;
import com.xinshang.modular.system.transfer.UserPosition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统公共 Mapper 接口
 * </p>
 *
 * @author zhangjiajia
 * @since 2018年11月26日 17:45:06
 */
@Mapper
public interface SystemMapper{

    /**
     * 跟据id添加seq和level
     * @param tableName
     * @param id
     * @param parentId
     * @return
     */
    int addSeqAndLevelByIdAndParentId(@Param("tableName") String tableName ,@Param("id") Integer id, @Param("parentId") Integer parentId);

    /**
     * 根据id更新其包含其下子节点的seq和level
     * @param tableName
     * @param id
     * @return
     */
    int updateSeqAndLevelById(@Param("tableName") String tableName ,@Param("id") Integer id);

    /**
     * 更新全表的seq和level
     * @param tableName
     * @return
     */
    int updateAllSeqAndLevel(@Param("tableName") String tableName);

    /**
     * 根据职位id查询该职位的人员及子职位下面的人员
     * @param positionId
     * @return
     */
    List<UserPosition> getPositionUserByPositionId(@Param("positionId") Integer positionId);

    /**
     * 根据用户id查询该用户所在职位的人员及子职位下面的人员
     * @param userId
     * @return
     */
    List<UserPosition> getPositionUserByUserId(@Param("userId") Integer userId);



    /**
     * 判断用户职位是否该职位
     * @param userId
     * @param positionCode
     * @return
     */
    int judgeUserHasPositionByUserIdAndPositionCode(@Param("userId") Integer userId, @Param("positionCode") String positionCode);

    /**
     * 该职位相同的人
     * @param positionCode
     * @return
     */
    List<UserPosition> getSamePositionUserByPositionCode(@Param("positionCode") String positionCode);

    /**
     * 该职位相同的人
     * @param positionCodes
     * @return
     */
    List<UserPosition> getSamePositionUserByPositionCodes(@Param("positionCodes") String positionCodes);

    /**
     * 根据表名和关联字段名查询出要获取的字段名
     * @param generalTable
     * @return
     */
    List<GeneralTable> getByTableAndField(GeneralTable generalTable);

}