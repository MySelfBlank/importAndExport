package utils;

import model.*;
import onegis.common.utils.GeneralUtils;
import onegis.psde.relation.Network;
import onegis.psde.relation.REdge;
import onegis.psde.relation.RNode;
import onegis.psde.relation.Relation;
import onegis.psde.rule.ARule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 关系转换
 */
public class ENetWorkUtils {


    /**
     * 转换关系
     * @param network
     * @return
     */
    public static ENetWork buildNetWork(Network network) {
        if (network == null) {
            return null;
        }

        List<RNode> nodes = network.getNodes();
        if (!GeneralUtils.isNotEmpty(nodes)) {
            return null;
        }

        ENetWork eNetWork = new ENetWork();
        List<ERNode> erNodes = new ArrayList<>();
        for (RNode rNode : nodes) {
            ERNode erNode = buildERNode(rNode);
            if (erNode != null) {
                erNodes.add(erNode);
            }
        }
        eNetWork.setNodes(erNodes);
        return eNetWork;
    }


    /**
     * RNode转ERNode
     * @param rNode 关系节点
     * @return ERNode
     */
    public static ERNode buildERNode(RNode rNode) {

        ERNode erNode = new ERNode();
        erNode.setId(rNode.getId());
        erNode.setRelatedObjectId(rNode.getRelatedObjectId().toString());
        EObase eObase = new EObase();
        eObase.setId(rNode.getRelatedObjectId());
        eObase.setName(rNode.getLabel());
        EClasses eClasses = new EClasses();
        eClasses.setId(rNode.getoType() == null ? 0L : rNode.getoType().getId());

        Map<String, Object> otypeMap = new HashMap<>(2);
        otypeMap.put("id", rNode.getoType() == null ? 0L : rNode.getoType().getId());
        otypeMap.put("name", rNode.getoType() == null ? null : rNode.getoType().getName());
        eObase.setotype(otypeMap);
        erNode.setRefObject(eObase);

        EREdge erEdge = buildEREdge(rNode.getEdge());
        erNode.setEdge(erEdge);
        return erNode;

    }


    /**
     * REdge转EREdge
     */
    public static EREdge buildEREdge(REdge rEdge) {
        if (rEdge == null) {
            return null;
        }
        EREdge erEdge = new EREdge();
        erEdge.setLabel(rEdge.getLabel());
        erEdge.setIntensity(rEdge.getIntensity());
        List<ARule> rules = rEdge.getRules();
        List<EARule> eaRules = new ArrayList<>();
        if (GeneralUtils.isNotEmpty(rules)) {
            for (ARule aRule : rules) {
                EARule eaRule = new EARule();
                if (GeneralUtils.isNotEmpty(eaRule.getId())) {
                    eaRule.setId(Long.valueOf(eaRule.getId().toString()));
                }
                eaRule.setName(aRule.getName());
                eaRules.add(eaRule);
            }
            erEdge.setRules(eaRules);
        }
        Relation relation = rEdge.getRelation();
        if (relation != null) {
            try {
                Map<String, Object> eRelaiton = new HashMap<>(2);
                eRelaiton.put("id", relation.getId());
                eRelaiton.put("name", relation.getName());
                erEdge.setRelation(eRelaiton);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        }
        return erEdge;
    }
}
