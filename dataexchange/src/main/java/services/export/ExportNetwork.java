package services.export;

import enums.ConstantDict;
import model.EGraph;
import onegis.common.utils.FileUtils;
import onegis.common.utils.JsonUtils;
import services.RequestServices;
import services.impl.RequestServicesImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * 保存关联关系网
 */
public class ExportNetwork {

    private RequestServices requestServices = new RequestServicesImpl();
    public  void wiriteNetwork(String baseDir,Long sdomainId){
        List<EGraph> eGraphs;
        try {
            eGraphs = requestServices.getRelationCatalog(sdomainId);
        } catch (Exception e) {
            eGraphs = new ArrayList<>();
        }
        FileUtils.writeContent(JsonUtils.objectToJson(eGraphs), baseDir, ConstantDict.GRAPH_DATA_FILE_NAME.getName(), false);
    }
}
