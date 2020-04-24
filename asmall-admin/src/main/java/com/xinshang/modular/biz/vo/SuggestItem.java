package com.xinshang.modular.biz.vo;

import lombok.Data;

import java.util.List;

/**
 * @author sunhao
 */
@Data
public class SuggestItem {
    private String message;
    private List<ItemDetailVO> value;
    private Integer code;
    private String redirect;
}
