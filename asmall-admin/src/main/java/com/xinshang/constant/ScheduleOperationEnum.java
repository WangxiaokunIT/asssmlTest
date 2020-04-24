package com.xinshang.constant;

/**
 * @author zhangjiajia
 */

public enum ScheduleOperationEnum {

    /**
     * 暂停
     */
    PAUSE("PAUSE"),
    /**
     * 继续
     */
    RESUME("RESUME");

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    ScheduleOperationEnum(String value) {
        this.value = value;
    }
    /**
     * 根据类型的名称，返回类型的枚举实例。
     * @param value 类型名称
     */
    public static ScheduleOperationEnum fromValue(String value) {
        for (ScheduleOperationEnum sse : ScheduleOperationEnum.values()) {
            if (sse.getValue().equals(value)) {
                return sse;
            }
        }
        return null;
    }
}
