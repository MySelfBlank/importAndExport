package com.yzh.importTest.requestEntity;

import onegis.psde.psdm.AObject;
import onegis.psde.psdm.OType;
import onegis.psde.psdm.User;

import java.util.Map;

/**
 * @author Yzh
 * @create 2021-01-13 14:46
 * @details
 */
public class ConnectorEntity extends AObject {
    private Integer type;
    private Map<String,Object> relation;
    private User user;
    private OType dType;
    private Long fId;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Map<String, Object> getRelation() {
        return relation;
    }

    public void setRelation(Map<String, Object> relation) {
        this.relation = relation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OType getdType() {
        return dType;
    }

    public void setdType(OType dType) {
        this.dType = dType;
    }

    public Long getfId() {
        return fId;
    }

    public void setfId(Long fId) {
        this.fId = fId;
    }
}
