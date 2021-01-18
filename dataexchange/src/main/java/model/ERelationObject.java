package model;

import onegis.psde.catalog.RelationObjectCatalog;
import onegis.psde.catalog.RelationTime;
import onegis.psde.psdm.OBase;

import java.util.ArrayList;
import java.util.List;

public class ERelationObject {
    private Long sObject;

    private Long eObject;

    private List<ERelationTimes> relationTimes = new ArrayList<>();

    public ERelationObject(RelationObjectCatalog relationObjectCatalog) {
        OBase oObject = relationObjectCatalog.getoObject();
        OBase dObject = relationObjectCatalog.getdObject();

        if (oObject != null) {
            this.sObject = oObject.getId();
        }

        if (dObject != null) {
            this.eObject = dObject.getId();
        }

        List<RelationTime> relationTimeList = relationObjectCatalog.getRelationTimes();
        if (relationTimeList != null && !relationTimeList.isEmpty()) {
            for (RelationTime relationTime : relationTimeList) {
                addRelationTime(relationTime);
            }
        }
    }

    public void addRelationTime(RelationTime relationTime) {
        if (relationTimes == null) {
            relationTimes = new ArrayList<>();
        }
        ERelationTimes eRelationTimes = new ERelationTimes(relationTime);
        relationTimes.add(eRelationTimes);
    }

    public ERelationObject() {
    }

    public Long getsObject() {
        return sObject;
    }

    public void setsObject(Long sObject) {
        this.sObject = sObject;
    }

    public Long geteObject() {
        return eObject;
    }

    public void seteObject(Long eObject) {
        this.eObject = eObject;
    }

    public List<ERelationTimes> getRelationTimes() {
        return relationTimes;
    }

    public void setRelationTimes(List<ERelationTimes> relationTimes) {
        this.relationTimes = relationTimes;
    }
}
