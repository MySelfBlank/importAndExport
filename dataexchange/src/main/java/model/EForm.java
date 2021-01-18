package model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 形态
 */
public class EForm {

    private Long id;

    private Long fid;

    /**
     * 形态类型（点线面模型等）
     */
    private String type;

    /**
     * 维度
     */
    private int dim;

    /**
     * 最小可见级别
     */
    private double minGrain;

    /**
     * 最大可见级别
     */
    private double maxGrain;

    /**
     * GeoJson格式的位置描述
     */
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private EPosition geom;

    /**
     * 【模型】形态引用
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private EFormRef formRef;

    private String style = "";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFid() {
        return fid;
    }

    public void setFid(Long fid) {
        this.fid = fid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDim() {
        return dim;
    }

    public void setDim(int dim) {
        this.dim = dim;
    }

    public double getMinGrain() {
        return minGrain;
    }

    public void setMinGrain(double minGrain) {
        this.minGrain = minGrain;
    }

    public double getMaxGrain() {
        return maxGrain;
    }

    public void setMaxGrain(double maxGrain) {
        this.maxGrain = maxGrain;
    }

    public EPosition getGeom() {
        return geom;
    }

    public void setGeom(EPosition geom) {
        this.geom = geom;
    }

    public EFormRef getFormRef() {
        return formRef;
    }

    public void setFormRef(EFormRef formRef) {
        this.formRef = formRef;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
