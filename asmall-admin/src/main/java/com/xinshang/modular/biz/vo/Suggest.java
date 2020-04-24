package com.xinshang.modular.biz.vo;

import com.xinshang.modular.biz.model.Supplier;
import lombok.Data;

import java.util.List;

/**
 * @author lyk 2019年6月19日 17:29:05
 */
@Data
public class Suggest {
    private String message;
    private List<Supplier> value;
    private Integer code;
    private String redirect;
}
