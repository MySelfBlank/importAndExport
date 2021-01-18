package model;

import java.util.ArrayList;
import java.util.List;

/**
 * 时空域
 */
public class ESDomain extends AbstractObject{
    /**
     * 描述
     */
    private String desc;

    /**
     * 起止时间
     */
    private Long sTime;

    /**
     * 终止时间
     */
    private Long eTime;

    /**
     * 父对象ID
     */
    private Long parentId;

    /**
     * 时间参考
     */
    private String trs;

    /**
     * 空间参考
     */
    private String srs;

    /**
     * 范围
     */
    private List<Double> geoBox = new ArrayList<>();

    public void addGeobox(double minx, double miny, double minz,
                          double maxx, double maxy, double maxz){
        this.addGeobox(this.geoBox,minx ,miny ,minz ,maxx ,maxy ,maxz );
    }

    public ESDomain() {
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getsTime() {
        return sTime;
    }

    public void setsTime(Long sTime) {
        this.sTime = sTime;
    }

    public Long geteTime() {
        return eTime;
    }

    public void seteTime(Long eTime) {
        this.eTime = eTime;
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
}
