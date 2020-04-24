package com.xinshang.core.common.constant.state;

/**
 * 用户部门树类型
 *
 * @author zhangjiajia
 * @date 2018年11月7日 15:21:45
 */
public enum UserTreeType {

    DEPT(1),POSITION(2),USER(3);

    Integer val;

    UserTreeType(Integer val) {
        this.val = val;
    }

    public Integer getVal() {
        return val;
    }

    public void setVal(Integer val) {
        this.val = val;
    }
}
