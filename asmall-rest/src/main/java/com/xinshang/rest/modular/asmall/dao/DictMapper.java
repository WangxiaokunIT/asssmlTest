package com.xinshang.rest.modular.asmall.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xinshang.rest.modular.asmall.model.Dict;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *
 * @author zhangjiajia
 * @since 2017-07-11
 */
public interface DictMapper extends BaseMapper<Dict> {

    /**
     * 查询字典列表
     */
    List<Map<String, Object>> list(@Param("condition") String conditiion);

    /**
     * 根据父类编码获取词典列表
     */
    List<Dict> selectByParentCode(@Param("code") String code);

    /**
     * 查询所有字典的code和name
     * @return
     */
    List<Map<String, Object>> listAllDictCodeAndName();

    /**
     * 根据编号集合获取字典项集合
     * @param codes 编号集合
     * @return
     */
//    List<DictVo> selectByNames(@Param("codes") String codes);
}