package com.yzh.utilts;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yzh.api.MyApi;
import com.yzh.dao.EConnector;
import com.yzh.userInfo.PathUtil;
import com.yzh.userInfo.UserInfo;
import com.yzh.utilts.tools.FileTools;
import onegis.psde.psdm.OType;
import onegis.psde.relation.Connector;
import onegis.psde.relation.Relation;
import onegis.psde.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static cn.hutool.core.util.ObjectUtil.isEmpty;
import static cn.hutool.core.util.ObjectUtil.isNull;

/**
 * @author Yzh
 * @create 2021-01-12 17:05
 * @details
 */
public class EConnectorUtils {
    private static final Logger logger = LoggerFactory.getLogger(EConnectorUtils.class);
    public static void EConnectorHandel( Set<Long> classIDs) throws Exception{
        Map<String,Object> params = new HashMap<>();
        params.put("token", UserInfo.token);
        params.put("otIds",classIDs.toArray());
        String responseStr = HttpUtil.get(MyApi.getConnector.getValue(),params);
        if(!JSONUtil.parseObj(responseStr).getStr("status").equals("200")){
            logger.error("获取连接器失败："+JSONUtil.parseObj(responseStr).getStr("message"));
            return;
        }
        JSONObject sourceData = JSONUtil.parseObj(responseStr);
        JSONObject clearData = (JSONObject) JSONUtil.parse(sourceData.get("data"));
        JSONArray clearDatalist = (JSONArray) clearData.get("list");
        List<Connector> connectorList = JsonUtils.jsonToList(clearDatalist.toString(),Connector.class);
        List<Connector> connectors = new ArrayList<>();
        for (Connector connector : connectorList) {
            if(classIDs.contains(connector.getfId())){
                if(classIDs.contains(connector.getdType().getId())){
                    connectors.add(connector);
                }
            }
        }
        List<EConnector> eConnectors = connector2EConnector(connectors, classIDs);
        FileTools.exportFile(JSONUtil.parse(eConnectors), PathUtil.baseInfoDir + "/test.connectors","connectors");
    }

    public static List<EConnector> connector2EConnector(List<Connector> connectors,Set<Long> classIDs) {
        List<EConnector> eConnectors = new ArrayList<>();
        if (isNull(connectors)||isEmpty(connectors)) {
            return eConnectors;
        }
        for (Connector connector : connectors) {
            EConnector eConnector = new EConnector();
            eConnector.setType(connector.getType().getName().toLowerCase());
            //从那来
            eConnector.setId(connector.getfId());
            if (connector.getRelation() != null) {
                Map<String, Object> eRelation = new HashMap<>();
                Relation relation = connector.getRelation();
//            container.addERelation(RelationUtils.dsRelations2ERelation(relation));
//            Export.addRelation(RelationUtils.dsRelations2ERelation(relation));
                eRelation.put("id", relation.getId());
                eRelation.put("name", relation.getName());
                eConnector.setRelation(eRelation);
            }
            if (connector.getdType() != null) {
                Map<String, Object> targetEClasses = new HashMap<>();
                OType target = connector.getdType();
                if (classIDs.contains(target.getId())) {
                    targetEClasses.put("id", target.getId());
                    targetEClasses.put("name", target.getName());
                    eConnector.setTarget(targetEClasses);
                }
            }
            if (eConnector.getTarget() == null) {
                return null;
            }
            eConnectors.add(eConnector);
        }
        return eConnectors;
    }

}
