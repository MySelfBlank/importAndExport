package com.yzh.dao.exportModel;

import java.io.Serializable;

/**
 * @author Yzh
 * @create 2021-01-14 10:38
 * @details
 */
public class EFormStyles implements Serializable {
    private Long id;
    private String name;
    private String des;
    private Long style;
    private String data;
    private Long type;
    private Integer dim;
    private Double minGrain;
    private Double maxGrain;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Long getStyle() {
        return style;
    }

    public void setStyle(Long style) {
        this.style = style;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Integer getDim() {
        return dim;
    }

    public void setDim(Integer dim) {
        this.dim = dim;
    }

    public Double getMinGrain() {
        return minGrain;
    }

    public void setMinGrain(Double minGrain) {
        this.minGrain = minGrain;
    }

    public Double getMaxGrain() {
        return maxGrain;
    }

    public void setMaxGrain(Double maxGrain) {
        this.maxGrain = maxGrain;
    }
}
