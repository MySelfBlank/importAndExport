package com.yzh.importTask.requestEntity;

import onegis.psde.psdm.AObject;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2021/1/12 15:47
 */
public class ModelDefEntity extends AObject {
    private String tags;
    private Integer type;
    private String des;
    private String icon;
    private String inTypes;
    private String outTypes;

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getInTypes() {
        return inTypes;
    }

    public void setInTypes(String inTypes) {
        this.inTypes = inTypes;
    }

    public String getOutTypes() {
        return outTypes;
    }

    public void setOutTypes(String outTypes) {
        this.outTypes = outTypes;
    }
}
