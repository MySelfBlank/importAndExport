package model;

import onegis.psde.attribute.Attribute;

/**
 * 属性字段
 */
public class EAttribute {
    /**
     * 字段ID
     */
    private Long fid;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 字段值
     */
    private Object value;

    public EAttribute() {

    }

    public EAttribute(Attribute attribute) {
        this.fid = attribute.getFid();
        this.name = attribute.getName();
        this.value = attribute.getValue();
    }

    public Long getFid() {
        return fid;
    }

    public void setFid(Long fid) {
        this.fid = fid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
