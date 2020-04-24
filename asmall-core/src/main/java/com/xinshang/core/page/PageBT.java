package com.xinshang.core.page;

import lombok.*;

/**
 * 分页参数类（for BootStrap Table）
 *
 * @author fengshuonan
 * @date 2017年1月21日 下午2:21:35
 */

@Data
public class PageBT {

    /**每页显示个数**/
    private int limit;
    /**查询的偏移量（查询的页数 = offset/limit + 1）**/
    private int offset;
    /**  排序方式**/
    private String order;


    public PageBT() {
        super();
    }

    public PageBT(int limit, int offset) {
        super();
        this.limit = limit;
        this.offset = offset;
    }

    public int getPageSize() {
        return this.limit;
    }

    public int getPageNumber() {
        return this.offset / this.limit + 1;
    }

}
