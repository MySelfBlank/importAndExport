package com.yzh.dao;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yzh.dao.exportModel.AbstractObject;
import com.yzh.dao.exportModel.EPosition;
import org.apache.commons.lang.StringUtils;

/**
 * @author Yzh
 * @create 2020-12-16 18:10
 */
public class EForm extends AbstractObject {
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


    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (this.getId() == null ? 0 : this.getId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;//地址相等
        }

        if (obj == null) {
            return false;//非空性：对于任意非空引用x，x.equals(null)应该返回false。
        }

        if (obj instanceof EForm) {
            EForm other = (EForm) obj;
            //需要比较的字段相等，则这两个对象相等
            if (equalsStr(this.getId(), other.getId())) {
                if(equalsStr(this.getStyle(), other.getStyle())){
                    return true;
                }
                return false;
            }
        }

        return false;
    }

    private boolean equalsStr(Long str1, Long str2) {
        if (ObjectUtil.isEmpty(str1) && ObjectUtil.isEmpty(str2)) {
            return true;
        }
        if (!ObjectUtil.isEmpty(str1) && str1.equals(str2)) {
            return true;
        }
        return false;
    }
    private boolean equalsStr(String str1, String str2) {
        if (StringUtils.isEmpty(str1) && StringUtils.isEmpty(str2)) {
            return true;
        }
        if (!StringUtils.isEmpty(str1) && str1.equals(str2)) {
            return true;
        }
        return false;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
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
