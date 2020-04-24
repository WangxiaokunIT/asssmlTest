package com.xinshang.modular.biz.vo;

import com.xinshang.modular.biz.model.Joinin;
import lombok.Data;

/**
 * 招募详情查看的
 * @author lyk
 */
@Data
public class JoininVO extends Joinin {
    /**
     * 客户名称
     */
    private String username;

    /**
     * 客户手机号
     */
    private String phone;
}
