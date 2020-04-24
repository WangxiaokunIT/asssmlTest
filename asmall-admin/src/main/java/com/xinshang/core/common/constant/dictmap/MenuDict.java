package com.xinshang.core.common.constant.dictmap;

import com.xinshang.core.common.constant.dictmap.base.AbstractDictMap;

/**
 * 菜单的字典
 *
 * @author fengshuonan
 * @date 2017-05-06 15:01
 */
public class MenuDict extends AbstractDictMap {

    @Override
    public void init() {
        put("menuId","菜单id");
        put("id","菜单id");
        put("code","菜单编号");
        put("parentId","菜单父编号");
        put("name","菜单名称");
        put("icon","菜单图标");
        put("url","url地址");
        put("num","菜单排序号");
        put("level","菜单层级");
        put("remark","备注");
        put("state","菜单状态");
        put("isOpen","是否打开");
        put("","");
    }

    @Override
    protected void initBeWrapped() {

    }
}
