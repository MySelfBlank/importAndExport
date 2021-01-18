package model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 关系基类
 */
public class ERObject {
    private String relatedObjectId;

    /**
     * 对象标签
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String label;

    public ERObject() {
    }

    public String getRelatedObjectId() {
        return relatedObjectId;
    }

    public void setRelatedObjectId(String relatedObjectId) {
        this.relatedObjectId = relatedObjectId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
