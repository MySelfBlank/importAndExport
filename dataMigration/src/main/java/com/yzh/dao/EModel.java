package com.yzh.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yzh.dao.exportModel.AbstractObject;
import onegis.psde.psdm.User;

import java.util.Date;

/**
 * @author Yzh
 * @create 2020-12-16 18:10
 */
public class EModel extends AbstractObject {
    /**
     * 初始化信息
     */
    private String initData;

    /**
     * 行为类
     */
    private EModelDef mdef;

    private User user;
    /**
     * 行为引用
     */
    private Mobj mobj;

    /**
     * 行为执行语言
     */
    private String pLanguage;

    /**
     * 执行器
     */
    private String executor = "";
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date mtime;

    public String getInitData() {
        return initData;
    }

    public void setInitData(String initData) {
        this.initData = initData;
    }

    public EModelDef getMdef() {
        return mdef;
    }

    public void setMdef(EModelDef mdef) {
        this.mdef = mdef;
    }

    public Mobj getMobj() {
        return mobj;
    }

    public void setMobj(Mobj mobj) {
        this.mobj = mobj;
    }

    public String getpLanguage() {
        return pLanguage;
    }

    public void setpLanguage(String pLanguage) {
        this.pLanguage = pLanguage;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getMtime() {
        return mtime;
    }

    public void setMtime(Date mtime) {
        this.mtime = mtime;
    }
}
