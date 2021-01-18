package com.yzh.dao;

import onegis.psde.attribute.Field;

import java.util.List;

/**
 * @author Yzh
 * @create 2020-12-07 15:25
 */
public class OtypeInputModel {
    private String name;
    private List<String> forms;
    private List<Field> fields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getForms() {
        return forms;
    }

    public void setForms(List<String> forms) {
        this.forms = forms;
    }

    public List<Field> getFides() {
        return fields;
    }

    public void setFides(List<Field> fides) {
        this.fields = fides;
    }

    @Override
    public String toString() {
        return "OtypeInputModel{" +
                "name='" + name + '\'' +
                ", forms=" + forms.toString() +
                ", fides=" + fields.toString() +
                '}';
    }
}
