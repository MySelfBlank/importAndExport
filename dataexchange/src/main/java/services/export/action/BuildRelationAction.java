package services.export.action;

import enums.EActionEnum;
import model.*;
import onegis.psde.psdm.SObject;
import onegis.psde.relation.Network;
import onegis.psde.relation.RNode;
import utils.ENetWorkUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildRelationAction {
    /**
     * 设置关系变化
     * @param eVersion
     * @param lastSObject
     * @param thisSObject
     */
    public static void setRelationAction(EVersion eVersion, SObject lastSObject, SObject thisSObject) {
        Network lastNetwork = lastSObject.getNetwork();
        List<RNode> lastRNodeList = new ArrayList<>();
        Map<Long, RNode> lastRNodeMap = new HashMap<>();
        if (lastNetwork != null) {
            lastRNodeList = lastNetwork.getNodes();
        }
        lastRNodeList.forEach(node -> lastRNodeMap.put(node.getId(), node));

        Network thisNetwork = thisSObject.getNetwork();
        List<RNode> thisRNodeList = new ArrayList<>();
        Map<Long, RNode> thisRNodeMap = new HashMap<>();
        if (thisNetwork != null) {
            thisRNodeList = thisNetwork.getNodes();
        }
        thisRNodeList.forEach(node -> thisRNodeMap.put(node.getId(), node));

        if (lastRNodeList.isEmpty() && thisRNodeList.isEmpty()) {
            return;
        }

        ENetWork eNetWork = new ENetWork();
        // 设置增加的关系节点
        for (RNode rNode : thisRNodeList) {
            Long id = rNode.getId();
            if (lastRNodeMap.get(id) == null) {
                operationNode(eVersion, eNetWork, rNode, EActionEnum.ADDING);
            }
        }
        // 设置删除的关系节点
        for (RNode rNode : lastRNodeList) {
            Long id = rNode.getId();
            if (thisRNodeMap.get(id) == null) {
                operationNode(eVersion, eNetWork, rNode, EActionEnum.DELETE);
            }
        }
        eVersion.setNetwork(eNetWork);
    }


    /**
     * 记录对象关系的变化
     * @param eVersion
     * @param eNetWork
     * @param rNode
     * @param eActionEnum
     */
    private static void operationNode(EVersion eVersion, ENetWork eNetWork, RNode rNode, EActionEnum eActionEnum) {
        if (rNode == null) {
            return;
        }
        Long id = rNode.getId();
        ERNode erNode = ENetWorkUtils.buildERNode(rNode);

        EAction eAction = new EAction();
        eAction.setId(id);
        eAction.setOperation(new EActionEvent(eActionEnum, EActionEnum.RELATION));

        eVersion.addAction(eAction);
        eNetWork.addRNode(erNode);
    }

}
