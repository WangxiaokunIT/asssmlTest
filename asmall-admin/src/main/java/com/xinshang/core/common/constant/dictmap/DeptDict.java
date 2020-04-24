package com.xinshang.core.common.constant.dictmap;

import com.xinshang.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 部门的映射
 *
 * @author fengshuonan
 * @date 2017-05-06 15:01
 */
public class DeptDict extends AbstractDictMap {

    @Override
    public void init() {
        put("deptId", "部门名称");
        put("sortNum", "部门排序");
        put("parentId", "上级名称");
        put("simpleName", "部门简称");
        put("fullName", "部门全称");
        put("remark", "备注");
    }

    @Override
    protected void initBeWrapped() {
        putFieldWrapperMethodName("deptId", "getDeptName");
        putFieldWrapperMethodName("pid", "getDeptName");
    }
}
