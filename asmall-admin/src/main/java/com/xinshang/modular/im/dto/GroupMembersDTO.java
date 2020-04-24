package com.xinshang.modular.im.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author zhangjiajia
 * @since 19-7-5
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMembersDTO {

    /**
     * 群成员
     */
   private List<InitUser> list;
}
