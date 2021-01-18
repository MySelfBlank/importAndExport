package model;

import java.util.List;

public class ERelation extends AbstractObject {
    /**
     * 描述关系的字段
     */
    private List<EField> fields;
    /**
     * 关系映射类型
     */
    private String mappingType;

    /**
     * 规则
     */
    private List rules;

    public List<EField> getFields() {
        return fields;
    }

    public void setFields(List<EField> fields) {
        this.fields = fields;
    }

    public String getMappingType() {
        return mappingType;
    }

    public void setMappingType(String mappingType) {
        this.mappingType = mappingType;
    }

    public List getRules() {
        return rules;
    }

    public void setRules(List rules) {
        this.rules = rules;
    }
}
