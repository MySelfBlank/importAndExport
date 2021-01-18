package services.export;

import services.RequestServices;
import services.impl.RequestServicesImpl;
import utils.PathUtil;

import java.util.Set;

/**
 * 下载模型文件(mongodb)
 */
public class ExportModel {

    private RequestServices requestServices = new RequestServicesImpl();
    public void downloadModel(){
        Set<Long> modelIds = ExecuteContainer.modelIds;
        for(Long modelId:modelIds){
            requestServices.downLoadModle(modelId+"", PathUtil.baseDirData);
        }
    }
}
