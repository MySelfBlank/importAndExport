package com.yzh.dao.exportModel;

import com.yzh.dao.EForm;
import com.yzh.dao.EModel;
import sun.font.EAttribute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Yzh
 * @create 2021-01-08 14:28
 * @details
 */
public class ESObject extends AbstractObject implements Serializable {

    public ESObject() {
    }

    /**
     * 编码
     */
    private String code;

    /**
     * 对象类
     */
    private Map otype;

    /**
     * 时间参考名称
     */
    private String trs;

    /**
     * 空间参考名称
     */
    private String srs;

    /**
     * 当前时间参考下的时间
     */
    private Long realTime = 0L;

    /**
     * 对象空间范围
     */
    private List<Double> geoBox = new ArrayList<>();

    /**
     * 对象所属时空域ID
     */
    private Long sdomain;

    /**
     * 对象所属的父对象ID
     */
    private Long parent;

    /**
     * 对象属性列表
     */
    private List<EAttribute> attributes = new ArrayList<>();

    /**
     * 对象形态列表
     */
    private List<EForm> forms = new ArrayList<>();

    /**
     * 对象具备的行为列表
     */
    private List<EModel> models = new ArrayList<>();

    /**
     * 对象网络【拓扑结构】关系网
     */
    private ENetWork network;

    /**
     * 组成结构
     */
    private List<ECompose> compose = new ArrayList<>();

    /**
     * 对象的数据来源
     */
    private Long dataSource;

    /**
     * 对象产生的DObject的ID集合
     */
    private List<Long> dataGenerate = new ArrayList<>();

    /**
     * 对象版本集合
     */
    private List<EVersion> versions = new ArrayList<>();

    public void addGeobox(double minx, double miny, double minz,
                          double maxx, double maxy, double maxz) {
        this.addGeobox(this.geoBox, minx, miny, minz, maxx, maxy, maxz);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public Map getOtype() {
        return otype;
    }

    public void setOtype(Map otype) {
        this.otype = otype;
    }

    public String getTrs() {
        return trs;
    }

    public void setTrs(String trs) {
        this.trs = trs;
    }

    public String getSrs() {
        return srs;
    }

    public void setSrs(String srs) {
        this.srs = srs;
    }

    public Long getRealTime() {
        return realTime;
    }

    public void setRealTime(Long realTime) {
        this.realTime = realTime;
    }

    public List<Double> getGeoBox() {
        return geoBox;
    }

    public void setGeoBox(List<Double> geoBox) {
        this.geoBox = geoBox;
    }

    public Long getSdomain() {
        return sdomain;
    }

    public void setSdomain(Long sdomain) {
        this.sdomain = sdomain;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public List<EAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<EAttribute> attributes) {
        this.attributes = attributes;
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

    public List<ECompose> getCompose() {
        return compose;
    }

    public void setCompose(List<ECompose> compose) {
        this.compose = compose;
    }

    public Long getDataSource() {
        return dataSource;
    }

    public void setDataSource(Long dataSource) {
        this.dataSource = dataSource;
    }

    public List<Long> getDataGenerate() {
        return dataGenerate;
    }

    public void setDataGenerate(List<Long> dataGenerate) {
        this.dataGenerate = dataGenerate;
    }

    public List<EVersion> getVersions() {
        return versions;
    }

    public void setVersions(List<EVersion> versions) {
        this.versions = versions;
    }
}
