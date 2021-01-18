package model;

import java.util.List;

/**
 * 几何
 */
public class EGeom {
    private String geotype;

    private List<?> coordinates;

    @Override
    public String toString() {
        return "EGeom{" +
                "geotype='" + geotype + '\'' +
                ", coordinates=" + coordinates +
                '}';
    }

    public String getGeotype() {
        return geotype;
    }

    public void setGeotype(String geotype) {
        this.geotype = geotype;
    }

    public List<?> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<?> coordinates) {
        this.coordinates = coordinates;
    }
}
