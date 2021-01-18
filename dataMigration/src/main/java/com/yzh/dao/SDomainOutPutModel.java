package com.yzh.dao;

import com.yzh.dao.exportModel.AbstractObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/16 16:35
 */
public class SDomainOutPutModel extends AbstractObject {

    private String desc;
    private Long parentId;
    private String trs;
    private String srs;
    private List<Double> geoBox = new ArrayList<>();
    private Long stime;
    private Long etime;

    public void addGeobox(double minx, double miny, double minz,
                          double maxx, double maxy, double maxz) {
        this.addGeobox(this.geoBox, minx, miny, minz, maxx, maxy, maxz);
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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

    public List<Double> getGeoBox() {
        return geoBox;
    }

    public void setGeoBox(List<Double> geoBox) {
        this.geoBox = geoBox;
    }

    public Long getStime() {
        return stime;
    }

    public void setStime(Long stime) {
        this.stime = stime;
    }

    public Long getEtime() {
        return etime;
    }

    public void setEtime(Long etime) {
        this.etime = etime;
    }
}
