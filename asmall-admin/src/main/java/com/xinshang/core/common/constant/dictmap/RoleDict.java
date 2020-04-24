package com.xinshang.core.common.constant.dictmap;

import com.xinshang.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 角色的字典
 *
 * @author fengshuonan
 * @date 2017-05-06 15:01
 */
public class RoleDict extends AbstractDictMap {

    @Override
    public void init() {
        put("roleId","角色名称");
        put("sortNum","角色排序");
        put("parentId","角色的父级");
        put("name","角色名称");
        put("remark","备注");
        put("seq","资源名称");
    }

    @Override
    protected void initBeWrapped() {
        putFieldWrapperMethodName("parentId","getSingleRoleName");
        putFieldWrapperMethodName("deptId","getDeptName");
        putFieldWrapperMethodName("roleId","getSingleRoleName");
        putFieldWrapperMethodName("seq","getMenuNames");
    }
}
