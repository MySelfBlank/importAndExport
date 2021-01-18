package com.yzh.dao.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 操作方式
 */
public enum EActionEnum {

    /**
     * 操作方式
     */
    ADDING("ADDING", 1),
    DELETE("DELETE", 2),
    MODIFY("MODIFY", 4),
    COPY("COPY", 8),

    /**
     * 对象属性的编辑标记
     */
    BASE("BASE", 32),
    ATTRIBUTE("ATTRIBUTE", 64),
    FORM("FORM", 128),
    RELATION("RELATION", 256),
    COMPOSE("COMPOSE", 512),
    MODEL("MODEL", 1024);

    private final String name;
    private Integer value;

    EActionEnum(final String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 根据value获取枚举对象
     *
     * @param value
     * @return
     */
    @JsonCreator
    public static EActionEnum getEnum(int value) {
        for (EActionEnum actionEnum : values()) {
            if (actionEnum.getValue() == value) {
                return actionEnum;
            }
        }
        return null;
    }

    /**
     * 根据name获取枚举对象
     *
     * @param name
     * @return
     */
    public static EActionEnum getEnum(String name) {
        for (EActionEnum actionEnum : values()) {
            if (actionEnum.getName().equals(name)) {
                return actionEnum;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
