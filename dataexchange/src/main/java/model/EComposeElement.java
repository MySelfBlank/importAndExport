package model;

/**
 * 组成元素
 */
public class EComposeElement {
    /**
     * 对象ID
     */
    private Long oid;

    /**
     * 对象名称
     */
    private String name;

    /**
     * 变换矩阵
     */
    private double[][] matrix;

    public EComposeElement() {
    }

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }
}
