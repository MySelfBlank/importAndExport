package com.yzh.dao;

import java.util.List;

/**
 * @author Yzh
 * @create 2020-12-17 8:41
 */
public class EGeom {
    private String geotype;

    private List<?> coordinates;

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

//    public static void main(String[] args) throws TextFormat.ParseException {
//        String point = "{\"type\":\"Point\",\"coordinates\":[114.2863425681,9.7167597312]}";
//        String lineString = "{\"type\":\"LineString\",\"coordinates\":[[114.2860043564,9.7152040254],[114.2873432948,9.7149835766],[114.2886809448,9.7147700313]]}";
//        String polygon = "{\"type\":\"Polygon\",\"coordinates\":[[[114.2865034685,9.7146506339],[114.2867459196,9.7146086635],[114.2867224566,9.7144750433],[114.2864800055,9.7145170137],[114.2865034685,9.7146506339]]]}";
//        EGeom geomPoint = JsonUtils.jsonToPojo(point.replace("type", "geotype"), EGeom.class);
//        geomPoint.setGeotype(geomPoint.getGeotype().toLowerCase());
//        EGeom geomLine = JsonUtils.jsonToPojo(lineString, EGeom.class);
//        EGeom geomPolygon = JsonUtils.jsonToPojo(polygon, EGeom.class);
//        System.out.println(geomPoint);
//        System.out.println(geomLine);
//        System.out.println(geomPolygon);
//    }
}
