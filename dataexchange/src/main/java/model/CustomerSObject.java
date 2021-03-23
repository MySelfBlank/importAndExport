package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import onegis.psde.dynamicdata.DynamicData;
import onegis.psde.form.GeoBox;
import onegis.psde.model.Model;
import onegis.psde.model.Models;
import onegis.psde.psdm.DObject;
import onegis.psde.reference.SpatialReferenceSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomerSObject extends DObject {
    @JsonIgnore
    private Boolean isDeleteVersion;
    private String trsStr;
    private String srsStr;
    private SpatialReferenceSystem srs;
    private List<Double> geoBoxList;
    private Long parentId;
    private GeoBox geoBox;
    /*@JsonDeserialize(
            using = FormDeserializer.class
    )
    @JsonSerialize(
            using = FormSerialize.class
    )*/
    private ArrayList<Form1> forms;
    private List<Model> modelList = new ArrayList<>();
    private Models models = new Models();
    private Long networkId;
    private ENetWork network = new ENetWork();
    private HashMap<Long, DObject> datas = new HashMap();
    private List<DynamicData> dynamicData;

    private List<EVersion> versions = new ArrayList<>();
//    private Compose compose;

    public CustomerSObject() {
    }

    public String getTrsStr() {
        return trsStr;
    }

    public void setTrsStr(String trsStr) {
        this.trsStr = trsStr;
    }

    public String getSrsStr() {
        return srsStr;
    }

    public void setSrsStr(String srsStr) {
        this.srsStr = srsStr;
    }

    public SpatialReferenceSystem getSrs() {
        return this.srs;
    }

    public void setSrs(SpatialReferenceSystem srs) {
        this.srs = srs;
    }

    public ArrayList<Form1> getForms() {
        return forms;
    }

    public void setForms(ArrayList<Form1> forms) {
        this.forms = forms;
    }

    public Models getModels() {
        return this.models;
    }

    public void setModels(Models models) {
        this.models = models;
    }

    public ENetWork getNetwork() {
        return network;
    }

    public void setNetwork(ENetWork network) {
        this.network = network;
    }

    public HashMap<Long, DObject> getDatas() {
        return this.datas;
    }

    public void setDatas(HashMap<Long, DObject> datas) {
        this.datas = datas;
    }

    public GeoBox getGeoBox() {
        return this.geoBox;
    }

    public void setGeoBox(GeoBox geoBox) {
        this.geoBox = geoBox;
    }

    public List<DynamicData> getDynamicData() {
        return this.dynamicData;
    }

    public void setDynamicData(List<DynamicData> dynamicData) {
        this.dynamicData = dynamicData;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            return o != null && this.getClass() == o.getClass() ? super.equals(o) : false;
        }
    }

    public int hashCode() {
        return super.hashCode();
    }

    public List<Double> getGeoBoxList() {
        return geoBoxList;
    }

    public void setGeoBoxList(List<Double> geoBoxList) {
        this.geoBoxList = geoBoxList;
    }

    public List<Model> getModelList() {
        return modelList;
    }

    public void setModelList(List<Model> modelList) {
        this.modelList = modelList;
    }

    public Long getNetworkId() {
        return networkId;
    }

    public void setNetworkId(Long networkId) {
        this.networkId = networkId;
    }

    public List<EVersion> getVersions() {
        return versions;
    }

    public void setVersions(List<EVersion> versions) {
        this.versions = versions;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Boolean getDeleteVersion() {
        return isDeleteVersion;
    }

    public void setDeleteVersion(Boolean deleteVersion) {
        isDeleteVersion = deleteVersion;
    }
}
