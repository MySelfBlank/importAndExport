package com.yzh.dao.exportModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yzh.dao.EForm;
import com.yzh.dao.EModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 版本
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EVersion {


    private Long vtime;

    private EBase base;

    /**
     * 行为集合
     */
    private List<EAction> actions = new ArrayList<>();

    /**
     * 增量 -- 属性集合
     */
    private List<EAttribute> attributes = new ArrayList<>();

    /**
     * 增量 -- 组成结构集合
     */
    private List<ECompose> composes = new ArrayList<>();

    /**
     * 增量 -- 形态集合
     */
    private List<EForm> forms = new ArrayList<>();

    /**
     * 增量 -- 行为集合
     */
    private List<EModel> models = new ArrayList<>();

    /**
     * 增量 -- 关系集合
     */
    private ENetWork network;

    public void addAction(EAction eAction){
        this.actions.add(eAction);
    }

    public void addForm(EForm eForm){
        this.forms.add(eForm);
    }

    public void addAttr(EAttribute eAttribute){
        this.attributes.add(eAttribute);
    }

    public void addModel(EModel eModel){
        this.models.add(eModel);
    }

    public void addCompose(ECompose eCompose){
        this.composes.add(eCompose);
    }

    public Long getVtime() {
        return vtime;
    }

    public void setVtime(Long vtime) {
        this.vtime = vtime;
    }

    public EBase getBase() {
        return base;
    }

    public void setBase(EBase base) {
        this.base = base;
    }

    public List<EAction> getActions() {
        return actions;
    }

    public void setActions(List<EAction> actions) {
        this.actions = actions;
    }

    public List<EAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<EAttribute> attributes) {
        this.attributes = attributes;
    }

    public List<ECompose> getComposes() {
        return composes;
    }

    public void setComposes(List<ECompose> composes) {
        this.composes = composes;
    }

    public List<EForm> getForms() {
        return forms;
    }

    public void setForms(List<EForm> forms) {
        this.forms = forms;
    }

    public List<EModel> getModels() {
        return models;
    }

    public void setModels(List<EModel> models) {
        this.models = models;
    }

    public ENetWork getNetwork() {
        return network;
    }

    public void setNetwork(ENetWork network) {
        this.network = network;
    }
}
