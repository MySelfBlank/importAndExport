package com.yzh.dao.exportModel;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2021/1/8 15:24
 * 参考系统（时间参考、空间参考）
 */
public class ERefSystem {
    /**
     * 参照ID
     */
    private String id;

    /**
     * 参照内容
     */
    private String wkt;

    public ERefSystem(String id, String wkt) {
        this.id = id;
        this.wkt = wkt;
    }

    public ERefSystem(String id, String name, String wkt) {
        this.id = id;
        if (name != null && !name.equals("")) {
            this.id = name + ":" + id;
        }

        this.wkt = wkt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWkt() {
        return wkt;
    }

    public void setWkt(String wkt) {
        this.wkt = wkt;
    }
}
