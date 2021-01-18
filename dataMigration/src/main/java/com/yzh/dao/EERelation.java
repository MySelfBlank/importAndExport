package com.yzh.dao;

import com.alibaba.fastjson.annotation.JSONField;
import onegis.psde.dictionary.RelationEnum;
import onegis.psde.relation.Relation;

import java.io.Serializable;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/23 17:38
 */
public class EERelation extends Relation implements Serializable {
    @Override
    public RelationEnum getMappingType() {
        return super.getMappingType();
    }
    @JSONField(name = "mappingType")
    @Override
    public void setOor(RelationEnum oor) {
        this.setOor(RelationEnum.getEnum(oor.getValue()));
    }
}
