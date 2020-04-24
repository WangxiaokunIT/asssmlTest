package com.xinshang.modular.im.dto;

import lombok.Data;

import java.util.List;


/**
 * @author zhangjiajia
 * @since 19-7-5
 */
@Data
public class InitDTO {

    /**
     * 我的信息
     */
    private InitUser mine;

    /**
     * 好友
     */
    private List<InitFriendGroup> friend;

    /**
     * 群
     */
    private List<InitGroup> group;
}
