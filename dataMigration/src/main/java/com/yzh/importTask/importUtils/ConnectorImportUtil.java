package com.yzh.importTask.importUtils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yzh.api.MyApi;
import com.yzh.dao.EConnector;
import com.yzh.importTask.requestEntity.ConnectorEntity;
import com.yzh.userInfo.UserInfo;
import com.yzh.utilts.FileTools;
import onegis.psde.psdm.OType;
import onegis.psde.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yzh.importTask.importUtils.IdCache.otypeNewIdAndOldId;
import static com.yzh.importTask.importUtils.IdCache.relationNewIdAndOldId;
import static com.yzh.utilts.FileTools.login;

/**
 * @author Yzh
 * @create 2021-01-13 9:15
 * @details
 */
public class ConnectorImportUtil {
    //日志工厂
    private static final Logger logger = LoggerFactory.getLogger(ConnectorImportUtil.class);

    public static void importConnectorUtil()throws Exception{
        otypeNewIdAndOldId.put(2234L,2237L);
        otypeNewIdAndOldId.put(2237L,2234L);
        logger.debug("连接器开始导入===========》读取连接文件");
        String connectorJSONStr = FileTools.readFile("E:\\test\\中原工_yzh\\test.connectors");
        List<EConnector> eConnectors = JsonUtils.jsonToList(connectorJSONStr, EConnector.class);
        for (EConnector eConnector : eConnectors) {
            ConnectorEntity connector = new ConnectorEntity();
            Map<String,Object> erelation = eConnector.getRelation();
            connector.setfId(otypeNewIdAndOldId.get(eConnector.getId()));
            connector.setType(getConnectorEnum(eConnector.getType()));
            if(erelation!=null){
                Map<String,Object> relation = new HashMap<>();
                relation.put("id",relationNewIdAndOldId.get(erelation.get("id")));
                relation.put("id",54L);
                relation.put("name",erelation.get("name").toString());
                connector.setRelation(relation);
            }
            OType oType = new OType();
            oType.setId(otypeNewIdAndOldId.get(Long.parseLong(eConnector.getTarget().get("id").toString())));
            connector.setdType(oType);
            //请求
            HttpUtil.post(MyApi.insertConnector.getValue()+"?token="+ UserInfo.token, JSONUtil.parseObj(connector).toString());
        }
    }
    public static Integer getConnectorEnum(String type){
        switch (type){
            case "relation":
                return 0;
            case "realization":
                return 2;
            case "aggregation":
                return 4;
            case "composition":
                return 8;
            case "dependency":
                return 16;
            case "association":
                return 32;
        }
        return null;
    }
    public static void main(String[] args) throws Exception{
        login("asiayu01@163.com", "yu1306730458");
        importConnectorUtil();
    }
}
