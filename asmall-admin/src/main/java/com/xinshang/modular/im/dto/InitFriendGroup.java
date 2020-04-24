package com.xinshang.modular.im.dto;

import lombok.Data;

import java.util.List;

/**
 * @author zhangjiajia
 * @since 19-7-5
 */
@Data
public class InitFriendGroup {

    /**
     * 分组名称
     */
    private String groupname;

    /**
     * 分组id
     */
    private Integer id;

    /**
     * 分组好友
     */
    private List<InitUser> list;
}
