package utils;

import enums.ERelationEnum;
import model.EConnector;
import model.ERelation;
import onegis.psde.psdm.OType;
import onegis.psde.relation.Connector;
import onegis.psde.relation.Connectors;
import onegis.psde.relation.Relation;
import services.export.ExecuteContainer;

import java.util.*;

public class ConnectorUtils {

    public static List<EConnector> dsConnectors2EConnectors(Connectors connectors, Set<Long> classIDs) throws Exception {
        if (connectors == null || connectors.getConnectors() == null) {
            return new ArrayList<>();
        }
        List<Connector> connectorList = connectors.getConnectors();
        List<EConnector> eConnectors = new ArrayList<>();
        if (connectorList.size() == 0) {
            return eConnectors;
        }
        for (Connector connector : connectorList) {
            EConnector eConnector = dsConnector2EConnector(connector, classIDs);
            if (eConnector != null) {
                eConnectors.add(eConnector);
            }
        }
        return eConnectors;
    }

    /**
     * 2019年3月11日09:55:13修订——connector中不给id个name，target如果为null，不返回connector
     *
     * @param connector
     * @param classIds
     * @return
     * @throws Exception
     */
    private static EConnector dsConnector2EConnector(Connector connector, Set<Long> classIds) throws Exception {
        EConnector eConnector = new EConnector();
        eConnector.setType(connector.getType().getName().toLowerCase());
        if (connector.getRelation() != null) {
            Map<String, Object> eRelation = new HashMap<>();
            Relation relation = connector.getRelation();
            ExecuteContainer.addRelation(dsRelations2ERelation(relation));
            eRelation.put("id", relation.getId());
            eRelation.put("name", relation.getName());
            eConnector.setRelation(eRelation);
        }
        if (connector.getdType() != null) {
            Map<String, Object> targetEClasses = new HashMap<>();
            OType target = connector.getdType();
            if (classIds.contains(target.getId())) {
                targetEClasses.put("id", target.getId());
                targetEClasses.put("name", target.getName());
                eConnector.setTarget(targetEClasses);
            }
        }
        if (eConnector.getTarget() == null) {
            return null;
        }
        return eConnector;
    }

    public static ERelation dsRelations2ERelation(Relation relation) throws Exception {
        if (relation == null) {
            return null;
        }
        ERelation eRelation = new ERelation();
        eRelation.setId(relation.getId());
        eRelation.setName(relation.getName());
        eRelation.setFields(FieldUtils.dsFields2DataFile(relation.getFields()));
        eRelation.setMappingType(ERelationEnum.getEnum(relation.getMappingType().getValue()).getName());
        eRelation.setRules(new ArrayList());
        return eRelation;
    }
}
