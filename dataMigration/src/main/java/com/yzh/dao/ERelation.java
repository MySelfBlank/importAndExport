package com.yzh.dao;

import com.yzh.dao.exportModel.AbstractObject;
import com.yzh.dao.exportModel.EFields;
import onegis.psde.attribute.Fields;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/22 15:58
 */
public class ERelation extends AbstractObject {
    /**
     * 描述关系的字段
     */
    private EFields fields;
    /**
     * 关系映射类型
     */
    private int mappingType;

    /**
     * 规则
     */
    public EModel model;

    public EFields getFields() {
        return fields;
    }

    public void setFields(EFields fields) {
        this.fields = fields;
    }

    public Integer getMappingType() {
        return mappingType;
    }

    public void setMappingType(Integer mappingType) {
        this.mappingType = mappingType;
    }

    public EModel getModel() {
        return model;
    }

    public void setModel(EModel model) {
        this.model = model;
    }
}
