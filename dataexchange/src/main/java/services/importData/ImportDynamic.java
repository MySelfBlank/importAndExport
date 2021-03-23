package services.importData;

import enums.ConstantDict;
import model.EDObject;
import model.ResponseResult;
import onegis.common.utils.JsonUtils;
import onegis.psde.dynamicdata.DynamicDatas;
import onegis.psde.dynamicdata.ObjectDynamicData;
import onegis.psde.dynamicdata.ObjectDynamicDatas;
import onegis.psde.psdm.OType;
import services.RequestServices;
import services.impl.RequestServicesImpl;
import utils.FileUtils;
import utils.PathUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImportDynamic {
    private RequestServices requestServices = new RequestServicesImpl();

    public void saveDynamicDatas(String path) {
        try {
            DynamicDatas dynamicDatas = buildDynamicDatas(path);
            String jsonStr = JsonUtils.objectToJson(dynamicDatas);

            ResponseResult responseResult = requestServices.saveDynamicDatas(jsonStr);
            if (responseResult.getStatus() == 200) {
                System.out.println("轨迹数据上传成功！");
            } else {
                System.out.println("轨迹数据上传失败！");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private DynamicDatas buildDynamicDatas(String path) throws Exception {

        DynamicDatas result = new DynamicDatas();
        ArrayList<File> files = FileUtils.getFiles(path + "\\data");
        String msg = FileUtils.readFile(path + "\\" + ConstantDict.DOBJECT_DATA_FILE_NAME.getName());
        List<EDObject> edObjects = JsonUtils.jsonToList(msg, EDObject.class);
        Map<Long, String> objectOt = new HashMap<>();
        edObjects.forEach(edObject -> {
            objectOt.put(edObject.getDataSource(), edObject.getdType());
        });
        List<ObjectDynamicDatas> objectDynamicDatasList = new ArrayList<>();
        Map<String, List<ObjectDynamicData>> nameMapDy = new HashMap<>();

        for (File file : files) {
            String fileName = file.getName();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (!"track".equals(suffix)) {
                continue;
            }
            String content = FileUtils.readFile(path + "\\data\\" + fileName);
            ObjectDynamicData oldObjectDynamicData = JsonUtils.jsonToPojo(content, ObjectDynamicData.class);
            Long oid = oldObjectDynamicData.getOid();
            String otName = objectOt.get(oid);
            if (otName.isEmpty() || otName.equals("")) {
                continue;
            }
            /**
             * 置换为新的ID
             */
            ObjectDynamicData objectDynamicData = changID(content, oid + "");
            if (nameMapDy.containsKey(otName)) {
                List<ObjectDynamicData> objectDynamicDataL = nameMapDy.get(otName);
                objectDynamicDataL.add(objectDynamicData);
                nameMapDy.put(otName, objectDynamicDataL);
            } else {
                List<ObjectDynamicData> objectDynamicDataL = new ArrayList<>();
                objectDynamicDataL.add(objectDynamicData);
                nameMapDy.put(otName, objectDynamicDataL);
            }
        }

        for (String key : nameMapDy.keySet()) {
            OType oType = requestServices.queryOtype(key);
            if (oType == null) {
                System.out.println("缺少tags为：" + key + "的Otype,相应的轨迹无法上传");
                continue;
            }
            List<ObjectDynamicData> objectDynamicDataList = nameMapDy.get(key);
            ObjectDynamicDatas objectDynamicDatas = new ObjectDynamicDatas();
            objectDynamicDatas.setOtype(oType);
            objectDynamicDatas.setObjectDynamicDataList(objectDynamicDataList);
            objectDynamicDatasList.add(objectDynamicDatas);
        }
        result.setObjectDynamicDatasList(objectDynamicDatasList);
        return result;
    }

    /**
     * 重置轨迹的ID
     *
     * @param content
     * @param id
     * @return
     * @throws Exception
     */
    private ObjectDynamicData changID(String content, String id) throws Exception {
        String newID = ReadID.idMaps.get(id).get(id);
        String newContent = content.replaceAll(id, newID);
        ObjectDynamicData objectDynamicData = JsonUtils.jsonToPojo(newContent, ObjectDynamicData.class);
        return objectDynamicData;
    }

}
