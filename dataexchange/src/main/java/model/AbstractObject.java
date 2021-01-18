package model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class AbstractObject {
    /**
     * 对象ID -- 数据库唯一标识
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Long id;

    /**
     * 对象名称
     */
    private String name;

    /**
     * 构建空间盒子【范围】
     * @param geobox 盒子对象
     * @param minx 最小X
     * @param miny 最小Y
     * @param minz 最小Z
     * @param maxx 最大X
     * @param maxy 最大Y
     * @param maxz 最大Z
     */
    public void addGeobox(List<Double> geobox, double minx, double miny, double minz,
                          double maxx, double maxy, double maxz){
        geobox.add(minx);
        geobox.add(miny);
        geobox.add(minz);
        geobox.add(maxx);
        geobox.add(maxy);
        geobox.add(maxz);
    }

    public AbstractObject() {
    }

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
}
