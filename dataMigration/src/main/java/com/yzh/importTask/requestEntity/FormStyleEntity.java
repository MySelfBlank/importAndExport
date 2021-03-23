package com.yzh.importTask.requestEntity;

import onegis.psde.psdm.AObject;

/**
 * @author Yzh
 * @create 2020-12-25 14:06
 * @details
 */
public class FormStyleEntity extends AObject {
    private String className = "oformstyle";
    private String des;
    private int style;
    private String data;
    private int type;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
