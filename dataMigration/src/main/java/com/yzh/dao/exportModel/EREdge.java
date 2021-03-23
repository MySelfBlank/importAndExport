package com.yzh.dao.exportModel;

import com.yzh.dao.EField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 边
 */
public class EREdge extends ERObject {

    /**
     * 关联【自定义】关系
     */
    private Map relation;

    /**
     * 关系强度
     */
    private Integer intensity;

    private List<EARule> rules = new ArrayList<>();

    /**
     * 对象属性值
     */
    private List<EField> attributes = new ArrayList<>();

    public Map getRelation() {
        return relation;
    }

    public void setRelation(Map relation) {
        this.relation = relation;
    }

    public Integer getIntensity() {
        return intensity;
    }

    public void setIntensity(Integer intensity) {
        this.intensity = intensity;
    }

    public List<EARule> getRules() {
        return rules;
    }

    public void setRules(List<EARule> rules) {
        this.rules = rules;
    }

    public List<EField> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<EField> attributes) {
        this.attributes = attributes;
    }
}
