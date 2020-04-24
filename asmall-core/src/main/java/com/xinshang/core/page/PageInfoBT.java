package com.xinshang.core.page;

import com.baomidou.mybatisplus.plugins.Page;
import lombok.*;

import java.util.List;

/**
 * 分页结果的封装(for Bootstrap Table)
 *
 * @author fengshuonan
 * @date 2017年1月22日 下午11:06:41
 */

@Data
public class PageInfoBT<T> {

    /** 结果集**/
    private List<T> rows;

    /** 总数**/
    private long total;

    public PageInfoBT(Page<T> page) {
        this.rows = page.getRecords();
        this.total = page.getTotal();
    }

}
