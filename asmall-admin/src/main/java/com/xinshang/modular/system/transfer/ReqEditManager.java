package com.xinshang.modular.system.transfer;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

/**
 * 编辑管理员的请求
 *
 * @author fengshuonan
 * @date 2017年1月15日 下午10:29:16
 */

@Data
public class ReqEditManager {

    @NotNull
    private String userId;

    /**
     * 用户姓名
     */
    @NotNull
    private String userName;
    /**
     * 密码
     */
    private String userPassword;
    /**
     * 手机号
     */
    @NotNull
    @Length(min = 11, max = 11)
    private String userPhone;

}
