package com.yzh.dao.exportModel;

import com.yzh.dao.EField;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yzh
 * @create 2021-01-14 16:26
 * @details
 */
public class EFields {
    private List<EField> fields;
    public EFields() {
        this.fields = new ArrayList();
    }

    public EFields(List<EField> fields) {
        this.fields = fields;
    }

    public List<EField> getFields() {
        return this.fields;
    }

    public void setFields(List<EField> fields) {
        this.fields = fields;
    }

    public List<Long> toList() {
        return (List)this.fields.stream().map((f) -> {
            return f.getId();
        }).collect(Collectors.toList());
    }

    public void addField(EField field) {
        this.fields.add(field);
    }

    public void addFields(List<EField> fields) {
        this.fields.addAll(fields);
    }

    public String toString() {
        String result = "[]";
        if (this.fields != null) {
            List<Long> fieldIdList = (List)this.fields.stream().map((f) -> {
                return f.getId();
            }).collect(Collectors.toList());
            result = fieldIdList.toString();
        }

        return result;
    }
}
