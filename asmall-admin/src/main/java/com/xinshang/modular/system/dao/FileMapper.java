package com.xinshang.modular.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xinshang.modular.system.model.File;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 上传文件 Mapper 接口
 * </p>
 *
 * @author zhangjiajia123
 * @since 2018-07-03
 */
public interface FileMapper extends BaseMapper<File> {

    /**
     * 根据条件查询用户列表
     */
    List<Map<String, Object>> selectFile(@Param("name") String name, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("categoryId") Integer categoryId);

}
