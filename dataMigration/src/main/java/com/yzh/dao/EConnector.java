package com.yzh.dao;

import com.yzh.dao.exportModel.AbstractObject;

import java.util.Map;

/**
 * @author Yzh
 * @create 2020-12-16 18:10
 */
public class EConnector extends AbstractObject {
    private Long id;
    /**
     * 连接关系类型
     */
    private String type;

    /**
     * 连接关系
     */
    private Map relation;

    /**
     * 关系指向类模板
     */
    private Map target;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map getRelation() {
        return relation;
    }

    public void setRelation(Map relation) {
        this.relation = relation;
    }

    public Map getTarget() {
        return target;
    }

    public void setTarget(Map target) {
        this.target = target;
    }
}
