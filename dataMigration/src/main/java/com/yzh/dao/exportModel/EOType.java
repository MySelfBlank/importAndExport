package com.yzh.dao.exportModel;

import onegis.psde.dictionary.EditTypeEnum;
import onegis.psde.psdm.DTypes;
import onegis.psde.reference.SpatialReferenceSystem;
import onegis.psde.reference.TimeReferenceSystem;
import onegis.psde.relation.Connectors;

/**
 * @author Yzh
 * @create 2021-01-14 10:16
 * @details
 */
public class EOType extends AbstractObject {
    private String tags;
    private String des;
    private String icon;
    private String code;
    private SpatialReferenceSystem srs;
    private TimeReferenceSystem trs;
    private EFields fields;
    private EFormStyless formStyles;
    private Connectors connectors;
    private EModels models;
//    private DTypes dTypes;
    private Integer placedes;
    private EditTypeEnum editType;
    private Long mtime;

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SpatialReferenceSystem getSrs() {
        return srs;
    }

    public void setSrs(SpatialReferenceSystem srs) {
        this.srs = srs;
    }

    public TimeReferenceSystem getTrs() {
        return trs;
    }

    public void setTrs(TimeReferenceSystem trs) {
        this.trs = trs;
    }

    public EFields getFields() {
        return fields;
    }

    public void setFields(EFields fields) {
        this.fields = fields;
    }

    public EFormStyless getFormStyles() {
        return formStyles;
    }

    public void setFormStyles(EFormStyless formStyles) {
        this.formStyles = formStyles;
    }

    public Connectors getConnectors() {
        return connectors;
    }

    public void setConnectors(Connectors connectors) {
        this.connectors = connectors;
    }

    public EModels getModels() {
        return models;
    }

    public void setModels(EModels models) {
        this.models = models;
    }

//    public DTypes getdTypes() {
//        return dTypes;
//    }
//
//    public void setdTypes(DTypes dTypes) {
//        this.dTypes = dTypes;
//    }

    public Integer getPlacedes() {
        return placedes;
    }

    public void setPlacedes(Integer placedes) {
        this.placedes = placedes;
    }

    public EditTypeEnum getEditType() {
        return editType;
    }

    public void setEditType(EditTypeEnum editType) {
        this.editType = editType;
    }

    public Long getMtime() {
        return mtime;
    }

    public void setMtime(Long mtime) {
        this.mtime = mtime;
    }
}
