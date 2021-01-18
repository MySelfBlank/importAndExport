package model;

public class EPosition {
    /**
     * 位置ID
     */
    private Long id;

    /**
     * 位置信息
     */
    private EGeom data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EGeom getData() {
        return data;
    }

    public void setData(EGeom data) {
        this.data = data;
    }
}
