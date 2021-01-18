package model;

import onegis.psde.catalog.RelationTime;

import java.util.ArrayList;
import java.util.List;

public class ERelationTimes {

    private Long startTime;

    private Long endTime;

    private List<ERelationProperties> properties = new ArrayList<>();

    public ERelationTimes(RelationTime relationTime) {
        this.startTime = relationTime.getStartTime();
        this.endTime = relationTime.getEndTime();
    }

    public ERelationTimes(Long startTime, Long endTime, List<ERelationProperties> properties) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.properties = properties;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public List<ERelationProperties> getProperties() {
        return properties;
    }

    public void setProperties(List<ERelationProperties> properties) {
        this.properties = properties;
    }
}
