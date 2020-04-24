package com.xinshang.rest.factory;

import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xinshang.core.support.HttpKit;
import com.xinshang.core.util.ToolUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;

/**
 * BootStrap Table默认的分页参数创建
 *
 * @author fengshuonan
 * @date 2017-04-05 22:25
 */
@Data
public class PageFactory<T> {

    @ApiModelProperty(value = "每页多少条")
    private Integer limit;
    @ApiModelProperty(value = "从第几条开始")
    private Integer offset;
    @JsonIgnore
    private String sort;
    @JsonIgnore
    private String order;

    public Page<T> defaultPage() {
        HttpServletRequest request = HttpKit.getRequest();
        //每页多少条数据
        int limit = Integer.valueOf(this.limit);
        //每页的偏移量(本页当前有多少条)
        int offset = Integer.valueOf(this.offset);
        //排序字段名称
        String sort = this.sort;
        //asc或desc(升序或降序)
        String order = this.order;
        if (ToolUtil.isEmpty(sort)) {
            Page<T> page = new Page<>((offset / limit + 1), limit);
            page.setOpenSort(false);
            return page;
        } else {
            Page<T> page = new Page<>((offset / limit + 1), limit, sort);
            if (Order.ASC.getDes().equals(order)) {
                page.setAsc(true);
            } else {
                page.setAsc(false);
            }
            return page;
        }
    }
}
