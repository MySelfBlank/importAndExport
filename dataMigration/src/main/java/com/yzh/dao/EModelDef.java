package com.yzh.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import onegis.psde.psdm.AObject;
import onegis.psde.psdm.User;

import java.util.Date;

/**
 * @author Yzh
 * @create 2020-12-17 8:49
 */
public class EModelDef extends AObject {
    private String tags;
    private Integer type;
    private String des;
    private String icon;
    private String inTypes;
    private String outTypes;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date mtime;
    private User user;

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

    public Date getMtime() {
        return mtime;
    }

    public void setMtime(Date mtime) {
        this.mtime = mtime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
