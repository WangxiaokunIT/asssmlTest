package com.xinshang.rest.modular.asmall.service;

import com.baomidou.mybatisplus.service.IService;
import com.xinshang.rest.modular.asmall.model.Dict;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 字典服务
 *
 * @author fengshuonan
 * @date 2017-04-27 17:00
 */
public interface IDictService extends IService<Dict> {

    /**
     * 添加字典
     */
    void addDict(String dictCode, String dictName, String dictRemark, String dictValues);

    /**
     * 编辑字典
     */
    void editDict(Integer dictId, String dictCode, String dictName, String dictRemark, String dicts);

    /**
     * 删除字典
     */
    void delteDict(Integer dictId);


    /**
     * 根据父类编码获取词典列表
     */
    List<Dict> selectByParentCode(@Param("code") String code);

    /**
     * 查询字典列表
     */
    List<Map<String, Object>> list(@Param("condition") String conditiion);

/*    *//**
     *  根据字典codes集合获取对应的字典项
     * @param codes 字典code集合 如： 'A','B','C'
     * @return
     *//*
    List<DictVo> selectByNames(String codes);*/
}
