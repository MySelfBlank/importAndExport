package com.yzh.dao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/22 16:01
 */
public enum  ERelationEnum {
    /**
     * 一对一
     */
    ONETOONE("onetoone", 1),
    /**
     * 一对多
     */
    ONETOMANY("onetomany", 2),
    /**
     * 多对一
     */
    MANYTOONE("manytoone", 3),
    /**
     * 多对多
     */
    MANYTOMANY("manytomany", 4);

    private final String name;

    private final int value;

    ERelationEnum(String name, int value) {
        this.value = value;
        this.name = name;
    }

    public static List<Map<String, Object>> enumList() {
        List<Map<String, Object>> list = new ArrayList<>();
        for(ERelationEnum relationEnum : values()){
            Map<String, Object> map = new HashMap<>();
            map.put("name", relationEnum.getName());
            map.put("value", relationEnum.getValue());
            list.add(map);
        }
        return list;
    }

    @JsonCreator
    public static ERelationEnum getEnum(int value){
        for(ERelationEnum relationEnum : values()){
            if(relationEnum.getValue() == value){
                return relationEnum;
            }
        }
        return null;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
