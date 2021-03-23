package com.yzh.dao.exportModel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EDObject extends AbstractObject {
    public EDObject() {
    }

    /**
     * 产生数据对应ot
     */
    @JsonProperty("dType")
    private String dType;

    /**
     * 产生数据源
     */
    private Long dataSource;

    /**
     * 写出文件名
     */
    private String data;

    public String getdType() {
        return dType;
    }

    public void setdType(String dType) {
        this.dType = dType;
    }

    public Long getDataSource() {
        return dataSource;
    }

    public void setDataSource(Long dataSource) {
        this.dataSource = dataSource;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
