package com.yzh.dao.exportModel;

import java.util.Map;

/**
 * 基础对象
 */
public class EObase extends AbstractObject{
    /**
     * 对象类
     */
    private Map oType;

    public Map getotype() {
        return oType;
    }

    public void setotype(Map oType) {
        this.oType = oType;
    }
}
