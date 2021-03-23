package com.yzh.dao.exportModel;

import java.util.ArrayList;
import java.util.List;

public class ENetWork {
    /**
     * 记录关系的目标结点
     */
    private List<ERNode> nodes = new ArrayList<>();

    public void addRNode(ERNode rNode) {
        if (nodes == null) {
            nodes = new ArrayList<>();
        }
        this.nodes.add(rNode);
    }

    public ENetWork() {
    }

    public List<ERNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<ERNode> nodes) {
        this.nodes = nodes;
    }
}
