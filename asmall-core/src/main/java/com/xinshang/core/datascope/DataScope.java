package com.xinshang.core.datascope;

import lombok.*;

import java.util.List;

/**
 * 数据范围
 *
 * @author fengshuonan
 * @date 2017-07-23 22:19
 */

@Data
public class DataScope {

    /**
     * 限制范围的字段名称
     */
    private String scopeName = "deptid";

    /**
     * 具体的数据范围
     */
    private List<Integer> deptIds;

    public DataScope() {
    }

    public DataScope(List<Integer> deptIds) {
        this.deptIds = deptIds;
    }

    public DataScope(String scopeName, List<Integer> deptIds) {
        this.scopeName = scopeName;
        this.deptIds = deptIds;
    }

}
