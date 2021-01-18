package com.yzh.utilts;

import com.yzh.dao.EConnector;
import onegis.psde.psdm.OType;
import onegis.psde.relation.Connector;
import onegis.psde.relation.Connectors;
import onegis.psde.relation.Relation;

import java.util.*;

/**
 * @author Yzh
 * @create 2020-12-17 10:01
 * @details 连接器处理
 */
public class ConnectorUtils {
    public static List<EConnector> dsConnectors2EConnectors(Connectors connectors, Set<Long> classIDs) throws Exception {

        //判断该Otype中是否存在连接器
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

    public static EConnector dsConnector2EConnector(Connector Connector, Set<Long> classIDs) {
        EConnector eConnector = new EConnector();
        eConnector.setType(Connector.getType().getName().toLowerCase());
        if (Connector.getRelation() != null) {
            Map<String, Object> eRelation = new HashMap<>();
            Relation relation = Connector.getRelation();
//            container.addERelation(RelationUtils.dsRelations2ERelation(relation));
//            Export.addRelation(RelationUtils.dsRelations2ERelation(relation));
            eRelation.put("id", relation.getId());
            eRelation.put("name", relation.getName());
            eConnector.setRelation(eRelation);
        }
        if (Connector.getdType() != null) {
            Map<String, Object> targetEClasses = new HashMap<>();
            OType target = Connector.getdType();
            if (classIDs.contains(target.getId())) {
                targetEClasses.put("id", target.getId());
                targetEClasses.put("name", target.getName());
                eConnector.setTarget(targetEClasses);
            }
        }
        if (eConnector.getTarget() == null) {
            return null;
        }
//        eConnector.setTarget(ClassesUtils.dsClasses2EClass(connector.getdType()));
        return eConnector;
    }
}
