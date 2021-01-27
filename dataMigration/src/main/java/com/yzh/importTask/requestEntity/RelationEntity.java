package com.yzh.importTask.requestEntity;

import onegis.psde.attribute.Fields;
import onegis.psde.psdm.AObject;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/25 14:50
 */
public class RelationEntity extends AObject {
    private int mappingType;
    private Fields fields;
    public ModelEntity model;
    private String code;

    public Integer getMappingType() {
        return mappingType;
    }

    public void setMappingType(Integer mappingType) {
        this.mappingType = mappingType;
    }

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

    public ModelEntity getModel() {
        return model;
    }

    public void setModel(ModelEntity model) {
        this.model = model;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
