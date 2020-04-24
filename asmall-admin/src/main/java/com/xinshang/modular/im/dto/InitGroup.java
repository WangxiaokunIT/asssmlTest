package com.xinshang.modular.im.dto;

import lombok.Data;

/**
 * @author zhangjiajia
 * @since 19-7-5
 */
@Data
public class InitGroup {
    /**
     * 群聊名
     */
    private String groupname;

    /**
     * 群聊id
     */
    private Integer id;

    /**
     * 群头像
     */
    private String avatar;

}
