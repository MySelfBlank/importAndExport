package model;

import onegis.psde.catalog.RelationCatalog;
import onegis.psde.catalog.RelationObjectCatalog;

import java.util.ArrayList;
import java.util.List;

/**
 * 关联关系网
 */
public class EGraph {

    private Long relationClassId;

    private String relationClassName;

    private List<ERelationObject> relationObject = new ArrayList<>();

    public EGraph(RelationCatalog relationCatalog) {
        this.relationClassId = relationCatalog.getId();
        this.relationClassName = relationCatalog.getName();

        List<RelationObjectCatalog> objects = relationCatalog.getObjects();
        if (objects != null && !objects.isEmpty()) {
            for (RelationObjectCatalog relationObjectCatalog : objects) {
                addRelationObject(relationObjectCatalog);
            }
        }

    }

    public void addRelationObject(RelationObjectCatalog relationObjectCatalog) {
        if (relationObject == null) {
            relationObject = new ArrayList<>();
        }

        ERelationObject eRelationObject = new ERelationObject(relationObjectCatalog);

        relationObject.add(eRelationObject);
    }

    public EGraph() {
    }

    public Long getRelationClassId() {
        return relationClassId;
    }

    public void setRelationClassId(Long relationClassId) {
        this.relationClassId = relationClassId;
    }

    public String getRelationClassName() {
        return relationClassName;
    }

    public void setRelationClassName(String relationClassName) {
        this.relationClassName = relationClassName;
    }

    public List<ERelationObject> getRelationObject() {
        return relationObject;
    }

    public void setRelationObject(List<ERelationObject> relationObject) {
        this.relationObject = relationObject;
    }
}
