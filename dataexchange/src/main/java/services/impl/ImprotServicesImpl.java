package services.impl;

import enums.EFormEnum;
import model.*;
import onegis.common.utils.IdMakerUtils;
import org.apache.commons.lang.StringUtils;
import services.ImportServices;
import services.RequestServices;
import services.importData.ImportDynamic;
import services.importData.ReadID;
import services.importData.ReadSObject;
import utils.BaseUrl;
import utils.FileUtils;

import java.io.File;
import java.util.*;

public class ImprotServicesImpl implements ImportServices {

    private RequestServices requestServices = new RequestServicesImpl();
    private ImportDynamic importDynamic = new ImportDynamic();
    @Override
    public void importData(String path,Long sdomainId) throws Exception{
        List<String> objectNameList = new ArrayList<>();
        List<File> files = FileUtils.getFiles(path);
        //获取对象的本地文件路径
        files.forEach(file ->{
            String name = file.getName();
            String suffix = name.substring(name.lastIndexOf(".") + 1);
            if(suffix.startsWith("object")){
                System.out.println(file.getAbsolutePath());
                objectNameList.add(file.getAbsolutePath());
            }
        });
//        // 创建基础对象
//        java.util.List<CustomerSObject> sObject1List = new ReadSObject().readFromFile(objectNameList.get(0), true);
//        if (sObject1List == null || sObject1List.isEmpty()) {
//            throw new RuntimeException("未解析出时空对象");
//        }
        ReadID.resetId(path);//重置ID
        ReadID.readId(path);//读取ID文件
        Map<String, String> modelMap = uploadModles(path + "/data");
        System.out.println("上传模型成功");
        importSObjectList(objectNameList,sdomainId,modelMap);
        System.out.println("上传对象成功");
        importDynamic.saveDynamicDatas(path);
        System.out.println("数据上传完成");
    }

    private void importSObjectList(List<String> objectNameList,Long sdomainId,Map<String, String> modelMap) throws Exception{
        for(int i=0;i<objectNameList.size();i++){
            List<CustomerSObject> customerSObjects = ReadSObject.readFromFile(objectNameList.get(i), true);
            //id替换

            if(i==0){
                if (customerSObjects == null || customerSObjects.isEmpty()) {
                    throw new RuntimeException("未解析出时空对象");
                }
                if(customerSObjects.size()>0){
                    Long sdomain = customerSObjects.get(0).getSdomain();
                    if(sdomain==sdomainId){
                        System.out.println("不能使用相同的时空域");
                        break;
                    }
                }
            }
            List<CustomerSObject> sObjectsVersion = ReadSObject.buildSObjectWithVersion(customerSObjects);
            List<CustomerSObject> deleteSobjectList = ReadSObject.deleteSObjectList;
            // 保存对象
            setInfo(customerSObjects, modelMap, sdomainId);
            requestServices.saveSObject(BaseUrl.token, customerSObjects,1);
            System.out.println("=========>>>>> 时空对象创建成功！");
            System.out.println("=========>>>>> 创建对象版本。。。。。。");
            // 保存对象版本
            setInfo(sObjectsVersion, modelMap, sdomainId);
            for (CustomerSObject customerSObject : sObjectsVersion) {
                List<CustomerSObject> customerSObjectList = new ArrayList<>();
                customerSObjectList.add(customerSObject);
                requestServices.saveSObject(BaseUrl.token, customerSObjectList,2);
            }
            System.out.println("=========>>>>> 对象版本创建成功！。。。。。。");
            System.out.println("=========>>>>> 创建对象消亡版本。。。。。。");
            if (deleteSobjectList != null && !deleteSobjectList.isEmpty()) {
                System.out.println(String.format("======>>>>> 共%s个对象已消亡", deleteSobjectList.size()));
                for (CustomerSObject deleteSObject : deleteSobjectList) {
                    List<CustomerSObject> deleteSObjectList = new ArrayList<>();
                    deleteSObjectList.add(deleteSObject);
                    requestServices.saveSObject(BaseUrl.token, deleteSObjectList,3);
                }
            }
            System.out.println("=========>>>>> 对象消亡版本创建成功！。。。。。。");
        }

    }

    /**
     * 设置时空域ID和形态ID
     * @param sObjects
     * @param modelMap
     * @param sdomainId
     * @throws Exception
     */
    private void setInfo(List<CustomerSObject> sObjects, Map<String, String> modelMap, Long sdomainId){
        sObjects.forEach(sObject->{
            if (sObject.getRealTime() == null) {
                sObject.setRealTime(System.currentTimeMillis());
            }
            sObject.setSdomain(sdomainId);
            Long id = new IdMakerUtils().nextId();
            ArrayList<Form1> form1s = sObject.getForms();
            if (form1s != null && form1s.size() > 0) {
                for (int i=0; i<form1s.size(); i++) {
                    Form1 form1 = form1s.get(i);
                    id += i;
                    PositionSerialize positionSerialize = form1.getFormRef() == null ? new PositionSerialize(i + 1 + "") : form1.getFormRef();
                    positionSerialize.setGeometry(null);
                    // 设置模型ID
                    EFormEnum type = form1.getType();
                    if (type != null && type.equals(EFormEnum.MODEL)) {
                        form1.setGeotype(EFormEnum.POINT.getValue());
                        String fname = positionSerialize.getFname();
                        if (fname != null && StringUtils.isNotBlank(fname)){
                            String fid = modelMap.get(fname);
                            positionSerialize.setRefid(fid);
                        }
                    }
                    form1.setFormref(positionSerialize);
                    form1.setGeomref(id+"");

                    CustomerGeom customerGeom = form1.getGeom();
                    if (customerGeom != null) {
                        customerGeom.setId(id+"");
                    }
                }
            }
//            if (form1s != null&&!form1s.isEmpty()) {
//                form1s.forEach(form1 -> form1.setFid(null));
//                sObject.setForms(form1s);
//            }
        });
    }

    /**
     * 上传模型
     * @param directoryPath
     * @return
     * @throws Exception
     */
    private Map<String, String> uploadModles(String directoryPath) throws Exception{
        Map<String, String> resultMap = new HashMap<>();
        // 读取该路径下所有文件
        ArrayList<File> fileList = FileUtils.getFiles(directoryPath);
        // 筛选出模型文件，包括后缀名.gltf .glb  .ive  .osgb
        String suffixArray[] = {"gltf", "glb", "ive", "osgb", "GLTF", "GLB", "IVE", "OSGB", "JSON", "json"};
        List<String> suffixList = Arrays.asList(suffixArray);
        // 遍历文件，并上传
        for(int i=0; i<fileList.size(); i++){
            File file = fileList.get(i);
            String fileName = file.getName();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            // 只上传模型文件
            if (suffixList.contains(suffix)) {
                Model model = requestServices.uploadModel(file);
                if (model!=null){
                    resultMap.put(model.getFname(), model.getFid());
                }
            }

        }
        return resultMap;
    }

}
