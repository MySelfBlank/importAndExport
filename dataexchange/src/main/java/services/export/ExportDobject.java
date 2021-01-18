package services.export;

import com.google.common.collect.Lists;
import enums.ConstantDict;
import model.EDObject;
import onegis.common.utils.FileUtils;
import onegis.common.utils.JsonUtils;
import onegis.psde.psdm.DObject;
import onegis.psde.psdm.SObject;
import services.RequestServices;
import services.impl.RequestServicesImpl;
import utils.PathUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExportDobject {

    private RequestServices requestServices = new RequestServicesImpl();

    /**
     * 保存Dobject文件到本地
     */
    public void writeDoject(){
        if (ExecuteContainer.dObjectList != null) {
            FileUtils.writeContent(JsonUtils.objectToJson(ExecuteContainer.dObjectList), PathUtil.baseDir, ConstantDict.DOBJECT_DATA_FILE_NAME.getName(), false);
        }
    }

    /**
     * 提取EDobject
     * @param sObjects
     */
    public void dsDObject2DataFile(List<SObject> sObjects) {
        if(sObjects==null||sObjects.size()==0){
            return;
        }
        Set<Long> dobjectfromIds = new HashSet<>(16);
        sObjects.forEach(s -> {
            Long from = s.getId();
            if (from != null && from != 0) {
                dobjectfromIds.add(from);
            }
        });

        if(dobjectfromIds.size()>0){
            List<Long> dobjectIds = new ArrayList<>(dobjectfromIds);
            List<EDObject> edObjects = new ArrayList<>();
            List<List<Long>> partition = Lists.partition(dobjectIds, 10);
            try{
                List<DObject> dObjects = new ArrayList<>();
                for (List<Long> list : partition) {
                    dObjects.addAll(requestServices.getDobjectByIds(list));
                }
                edObjects = dsDobjectsToEDObjects(dObjects);
            }catch (Exception e){
                e.printStackTrace();
            }
            ExecuteContainer.addDObject(edObjects);
        }
    }

    /**
     *
     * @param dObjects
     * @return
     * @throws Exception
     */
    public  List<EDObject> dsDobjectsToEDObjects(List<DObject> dObjects) throws Exception {
        if (dObjects == null||dObjects.size() == 0) {
            return null;
        }
        List<EDObject> edObjects = new ArrayList<>(dObjects.size());
        for(DObject dObject : dObjects){
            if (dObject.getDataRef() != null && !"".equals(dObject.getDataRef())){
                /**下载模型文件*/
                requestServices.downLoadDll(dObject.getDataRef(), PathUtil.baseDirData);
            }
            EDObject edObject = dsDobjectToEDObject(dObject);
            edObjects.add(edObject);
        }

        return edObjects;
    }

    /**
     * 创建EDObject
     * @param dObject
     * @return
     */
    private  EDObject dsDobjectToEDObject(DObject dObject) {
        EDObject edObject = new EDObject();
        edObject.setId(dObject.getId());
        edObject.setName(dObject.getName());
        if (dObject.getOtype() != null) {
            //edObject.setdType(dObject.getOtype().getId());
            edObject.setdType(dObject.getOtype().getName());
        }
        edObject.setDataSource(dObject.getFrom());
        String dataRef = dObject.getDataRef();
        String fileName = dataRef.substring(dataRef.lastIndexOf("_") + 1);
        edObject.setData(fileName);
        return edObject;
    }

}
