package model;

import java.util.ArrayList;
import java.util.List;

/**
 * 类
 */
public class EClasses extends AbstractObject {
    /**
     * 描述
     */
    private String desc;

    /**
     * 空间参考
     */
    private String srs;

    /**
     * 时间参考
     */
    private String trs;

    /**
     * 字段
     */
    private List<EField> fields = new ArrayList<>();

    /**
     * 形态
     */
    private List<EForm> forms = new ArrayList<>();

    /**
     * 关系
     */
    private List<EConnector> connectors = new ArrayList<>();

    /**
     * 行为
     */
    private List<EModel> models = new ArrayList<>();

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSrs() {
        return srs;
    }

    public void setSrs(String srs) {
        this.srs = srs;
    }

    public String getTrs() {
        return trs;
    }

    public void setTrs(String trs) {
        this.trs = trs;
    }

    public List<EField> getFields() {
        return fields;
    }

    public void setFields(List<EField> fields) {
        this.fields = fields;
    }

    public List<EForm> getForms() {
        return forms;
    }

    public void setForms(List<EForm> forms) {
        this.forms = forms;
    }

    public List<EConnector> getConnectors() {
        return connectors;
    }

    public void setConnectors(List<EConnector> connectors) {
        this.connectors = connectors;
    }

    public List<EModel> getModels() {
        return models;
    }

    public void setModels(List<EModel> models) {
        this.models = models;
    }
}
