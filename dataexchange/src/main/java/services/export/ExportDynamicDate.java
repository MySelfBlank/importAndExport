package services.export;

import cn.hutool.core.io.FileUtil;
import model.EDObject;
import onegis.common.utils.FileUtils;
import onegis.common.utils.IdMakerUtils;
import onegis.common.utils.JsonUtils;
import onegis.psde.dynamicdata.DynamicData;
import onegis.psde.dynamicdata.DynamicDatas;
import onegis.psde.dynamicdata.ObjectDynamicData;
import onegis.psde.dynamicdata.ObjectDynamicDatas;
import onegis.psde.psdm.SObject;
import services.RequestServices;
import services.impl.RequestServicesImpl;
import utils.PathUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 导出动态数据
 */
public class ExportDynamicDate {

    private RequestServices requestServices = new RequestServicesImpl();

    public void downloadDynamicData(List<SObject> sObjects){
        for (SObject sObject:sObjects){
            try{
                Long id = sObject.getId();
                DynamicDatas dynamicDates = requestServices.getDynamicDate(id + "");
                handleDynamicData(sObject,dynamicDates);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void handleDynamicData(SObject sObject,DynamicDatas dynamicDates) throws Exception{
        Long oid = sObject.getId();
        if(ExecuteContainer.oidAllList.contains(oid)){
            return;
        }
        ExecuteContainer.oidAllList.add(oid);
        if(dynamicDates!=null){
            List<ObjectDynamicDatas> objectDynamicDatasList = dynamicDates.getObjectDynamicDatasList();
            for (ObjectDynamicDatas objectDynamicDatas : objectDynamicDatasList) {
                String otName = objectDynamicDatas.getOtype().getName();
                List<ObjectDynamicData> objectDynamicDataList = objectDynamicDatas.getObjectDynamicDataList();
                for (ObjectDynamicData objectDynamicData : objectDynamicDataList) {
                    if (objectDynamicData.getDynamicData() != null && objectDynamicData.getDynamicData().size() > 0) {
                        /** 构建EClasses 、DObject和 dynamicDataCorn*/
                        dsDynamicWriter(sObject, objectDynamicData.getDynamicData(),otName);
                        /** 写入动态轨迹数据到文件 */
                        FileUtils.writeString(JsonUtils.objectToJson(objectDynamicData), PathUtil.baseDirData, sObject.getName() + ".track");
                        /** 拥有类模板的OtypeId*/
                        ExecuteContainer.DOTypeIdList.add(sObject.getOtype().getId());
                    }
                }
            }
        }
    }

    private void dsDynamicWriter(SObject sObject, List<DynamicData> dynamicDatas,String otName) throws Exception {
        List<EDObject> edObjects = new ArrayList<>();
        //构建dobject
        EDObject edObject = new EDObject();
        edObject.setId(new IdMakerUtils().nextId());
        edObject.setDataSource(sObject.getId());
        edObject.setName(sObject.getName() + "动态数据");
        //edObject.setdType(idMakerUtils.nextId());
        edObject.setdType(otName);
        edObject.setData(sObject.getName() + ".track");
        edObjects.add(edObject);
        ExecuteContainer.addDObject(edObjects);
    }

}
