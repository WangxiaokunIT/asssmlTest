package com.xinshang.core.common.constant.state;

/**
 * @author zhangjiajia
 * @date 2018年11月27日 11:08:48
 * @desc 系统中所有树状结构的表枚举，主要用于更新其seq和leve用
 */

public enum SystemTreeTableEnum {
    /**
     * 部门表，职位表，菜单表，角色表,文件类别表
     */
    DEPT("sys_dept"),POSITION("sys_position"),MENU("sys_menu"),ROLE("sys_role"),FILE_CATEGORY("sys_file_category"),ITEM_CATEGORY("tb_item_category");

    /**
     * 表名字
     *
     **/
    private String tableName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    SystemTreeTableEnum(String tableName) {
        this.tableName = tableName;
    }
}
