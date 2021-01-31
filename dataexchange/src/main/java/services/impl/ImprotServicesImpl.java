package services.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import enums.EFormEnum;
import model.*;
import onegis.common.utils.IdMakerUtils;
import onegis.common.utils.JsonUtils;
import onegis.psde.attribute.Attribute;
import onegis.psde.psdm.OType;
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

    private Map<String, Object> otypeIdCache = new HashMap<>();
    private Map<String, Object> fieldIdCache = new HashMap<>();
    private Map<String, Object> formIdCache = new HashMap<>();
    private Map<String, Object> modelIdCache = new HashMap<>();
    private Map<String, Object> modelDefIdCache = new HashMap<>();
    private Map<String, Object> relationIdCache = new HashMap<>();

    @Override
    public void importData(String path, Long sdomainId) throws Exception {
        List<String> objectNameList = new ArrayList<>();
        List<File> files = FileUtils.getFiles(path);
        //获取对象的本地文件路径
        files.forEach(file -> {
            String name = file.getName();
            String suffix = name.substring(name.lastIndexOf(".") + 1);
            if (suffix.startsWith("object")) {
                System.out.println(file.getAbsolutePath());
                objectNameList.add(file.getAbsolutePath());
            }
        });
//        // 创建基础对象
//        java.util.List<CustomerSObject> sObject1List = new ReadSObject().readFromFile(objectNameList.get(0), true);
//        if (sObject1List == null || sObject1List.isEmpty()) {
//            throw new RuntimeException("未解析出时空对象");
//        }
        //读取本地Id缓存
        String idpath = path+"\\base";
        String otpyeIdStr = FileUtils.readFile(idpath + "\\otpyeId.text");
        String fieldIdStr = FileUtils.readFile(idpath + "\\fieldId.text");
        String formIdStr = FileUtils.readFile(idpath + "\\formId.text");
        String modelIdStr = FileUtils.readFile(idpath + "\\modelId.text");
        String modelDefIdStr = FileUtils.readFile(idpath + "\\modelDefId.text");
        String relationIdStr = FileUtils.readFile(idpath + "\\relationId.text");

        otypeIdCache.putAll(JsonUtils.parseMap(otpyeIdStr));
        fieldIdCache.putAll(JsonUtils.parseMap(fieldIdStr));
        formIdCache.putAll(JsonUtils.parseMap(formIdStr));
        modelIdCache.putAll(JsonUtils.parseMap(modelIdStr));
        modelDefIdCache.putAll(JsonUtils.parseMap(modelDefIdStr));
        relationIdCache.putAll(JsonUtils.parseMap(relationIdStr));

        ReadID.resetId(path);//重置ID
        ReadID.readId(path);//读取ID文件
//        Map<String, String> modelMap = new HashMap<>();
        Map<String, String> modelMap=uploadModles(path + "/data");
        System.out.println("上传模型成功");
        importSObjectList(objectNameList, sdomainId, modelMap);
        System.out.println("上传对象成功");
        importDynamic.saveDynamicDatas(path);
        System.out.println("数据上传完成");
    }

    private void importSObjectList(List<String> objectNameList, Long sdomainId, Map<String, String> modelMap) throws Exception {
        for (int i = 0; i < objectNameList.size(); i++) {
            List<CustomerSObject> customerSObjects = ReadSObject.readFromFile(objectNameList.get(i), true);
            //id替换
            for (CustomerSObject customerSObject : customerSObjects) {
                //修改类模板Id
                handleOTypeId(customerSObject);
                //修改字段Id
                handleFieldId(customerSObject.getAttributes().getAttributeList());
                //修改形态样式Id
                handleFormStyleId(customerSObject.getForms());
                //修改对象的行为Id
                handleModelId(customerSObject.getModelList());
                handleModelId(customerSObject.getModels().getModels());
                //修改对象使用关系的Id
                handleNetwork(customerSObject.getNetwork());
            }
            if (i == 0) {
                if (customerSObjects == null || customerSObjects.isEmpty()) {
                    throw new RuntimeException("未解析出时空对象");
                }
                if (customerSObjects.size() > 0) {
                    Long sdomain = customerSObjects.get(0).getSdomain();
                    if (sdomain.equals(sdomainId)) {
                        System.out.println("不能使用相同的时空域");
                        break;
                    }
                }
            }
            List<CustomerSObject> sObjectsVersion = ReadSObject.buildSObjectWithVersion(customerSObjects);
            List<CustomerSObject> deleteSobjectList = ReadSObject.deleteSObjectList;
            // 保存对象
            setInfo(customerSObjects, modelMap, sdomainId);
            requestServices.saveSObject(BaseUrl.token, customerSObjects, 1);
            System.out.println("=========>>>>> 时空对象创建成功！");
            System.out.println("=========>>>>> 创建对象版本。。。。。。");
            // 保存对象版本
            setInfo(sObjectsVersion, modelMap, sdomainId);
            for (CustomerSObject customerSObject : sObjectsVersion) {
                List<CustomerSObject> customerSObjectList = new ArrayList<>();
                customerSObjectList.add(customerSObject);
                requestServices.saveSObject(BaseUrl.token, customerSObjectList, 2);
            }
            System.out.println("=========>>>>> 对象版本创建成功！。。。。。。");
            System.out.println("=========>>>>> 创建对象消亡版本。。。。。。");
            if (deleteSobjectList != null && !deleteSobjectList.isEmpty()) {
                System.out.println(String.format("======>>>>> 共%s个对象已消亡", deleteSobjectList.size()));
                for (CustomerSObject deleteSObject : deleteSobjectList) {
                    List<CustomerSObject> deleteSObjectList = new ArrayList<>();
                    deleteSObjectList.add(deleteSObject);
                    requestServices.saveSObject(BaseUrl.token, deleteSObjectList, 3);
                }
            }
            System.out.println("=========>>>>> 对象消亡版本创建成功！。。。。。。");
        }

    }

    /**
     * 设置时空域ID和形态ID
     *
     * @param sObjects
     * @param modelMap
     * @param sdomainId
     * @throws Exception
     */
    private void setInfo(List<CustomerSObject> sObjects, Map<String, String> modelMap, Long sdomainId) {
        sObjects.forEach(sObject -> {
            if (sObject.getRealTime() == null) {
                sObject.setRealTime(System.currentTimeMillis());
            }
            sObject.setSdomain(sdomainId);
            Long id = new IdMakerUtils().nextId();
            ArrayList<Form1> form1s = sObject.getForms();
            if (form1s != null && form1s.size() > 0) {
                for (int i = 0; i < form1s.size(); i++) {
                    Form1 form1 = form1s.get(i);
                    id += i;
                    PositionSerialize positionSerialize = form1.getFormRef() == null ? new PositionSerialize(i + 1 + "") : form1.getFormRef();
                    positionSerialize.setGeometry(null);
                    // 设置模型ID
                    EFormEnum type = form1.getType();
                    if (type != null && type.equals(EFormEnum.MODEL)) {
                        form1.setGeotype(EFormEnum.POINT.getValue());
                        String fname = positionSerialize.getFname();
                        if (fname != null && StringUtils.isNotBlank(fname)) {
                            String fid = modelMap.get(fname);
                            positionSerialize.setRefid(fid);
                        }
                    }
                    form1.setFormref(positionSerialize);
                    form1.setGeomref(id + "");

                    CustomerGeom customerGeom = form1.getGeom();
                    if (customerGeom != null) {
                        customerGeom.setId(id + "");
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
     *
     * @param directoryPath
     * @return
     * @throws Exception
     */
    private Map<String, String> uploadModles(String directoryPath) throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        // 读取该路径下所有文件
        ArrayList<File> fileList = FileUtils.getFiles(directoryPath);
        // 筛选出模型文件，包括后缀名.gltf .glb  .ive  .osgb
        String suffixArray[] = {"gltf", "glb", "ive", "osgb", "GLTF", "GLB", "IVE", "OSGB", "JSON", "json"};
        List<String> suffixList = Arrays.asList(suffixArray);
        // 遍历文件，并上传
        for (int i = 0; i < fileList.size(); i++) {
            File file = fileList.get(i);
            String fileName = file.getName();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            // 只上传模型文件
            if (suffixList.contains(suffix)) {
                Model model = requestServices.uploadModel(file);
                if (model != null) {
                    resultMap.put(model.getFname(), model.getFid());
                }
            }

        }
        return resultMap;
    }

    /**
     * 处理对象的类模板Id
     *
     * @param customerSObject
     */
    public void handleOTypeId(CustomerSObject customerSObject) {
        Long id = customerSObject.getOtype().getId();
        String newId = String.valueOf(otypeIdCache.get(String.valueOf(id)));
        if (StrUtil.isEmpty(newId) || StrUtil.isBlank(newId)) {
            throw new RuntimeException("对象" + customerSObject.getId() + "类模板Id不存在");
        } else {
            customerSObject.getOtype().setId(Long.parseLong(newId));
        }
    }

    /**
     * 处理对象的属性字段
     *
     * @param attributes
     */
    public void handleFieldId(List<Attribute> attributes) {
        if (ObjectUtil.isNull(attributes) || ObjectUtil.isEmpty(attributes)) {
            return;
        }
        for (Attribute attribute : attributes) {
            Long fid = attribute.getFid();
            String newId = String.valueOf(fieldIdCache.get(String.valueOf(fid)));
            if (StrUtil.isEmpty(newId) || StrUtil.isBlank(newId)) {
                throw new RuntimeException("字段" + fid + "新Id不存在");
            } else {
                attribute.setFid(Long.parseLong(newId));
            }
        }
    }

    /**
     * 处理对象的样式Id
     *
     * @param forms
     */
    public void handleFormStyleId(List<Form1> forms) {
        if (ObjectUtil.isEmpty(forms) || ObjectUtil.isNull(forms)) {
            return;
        }
        for (Form1 form : forms) {
            int value = form.getType().getValue();
            if (value == 21 || value == 22 || value == 23) {
                String styleStr = form.getStyle();
                if (styleStr.equals("[]") || StrUtil.isBlank(styleStr) || StrUtil.isEmpty(styleStr)) {
                    continue;
                }
                System.out.println(styleStr);
                JSONArray jsonArray = JSONArray.parseArray(styleStr);
                List<String> newIdList = new ArrayList<>();
                List<Integer> newIds = new ArrayList<>();
                for (Object s : jsonArray) {
                    if (s instanceof Integer) {
                        newIds.add((int) formIdCache.get(s.toString()));
                    } else {
                        newIdList.add(formIdCache.get(s).toString());
                        System.out.println(s);
                    }

                }
                System.out.println(JSONUtil.parseArray(newIdList).toString());
                if (newIds.size() != 0) {
                    form.setStyle(JSONUtil.parseArray(newIds).toString());
                } else {
                    form.setStyle(JSONUtil.parseArray(newIdList).toString());
                }
            }
        }
    }

    /**
     * 处理对象的行为Id  对象无行为挂载直接返回
     * @param models
     */
    public void handleModelId(List<onegis.psde.model.Model> models) {
        if (models.size() == 0 || ObjectUtil.isNull(models) || ObjectUtil.isEmpty(models)) {
            return;
        }
    }

    public void handleNetwork(ENetWork netWork){
        if (ObjectUtil.isNull(netWork)||ObjectUtil.isEmpty(netWork)){
            return;
        }
        //获取关系节点
        List<ERNode> nodes = netWork.getNodes();
        for (ERNode node : nodes) {
            //处理关系
            EREdge edge = node.getEdge();
            Map relation = handleEdge(edge.getRelation());

            //处理类模板
            EObase refObject = node.getRefObject();
            Map oType = handleRefObjectOType(refObject.getotype());
        }
    }
    private Map handleEdge(Map relation){
        String id = relation.get("id").toString();
        Integer newId = (Integer) relationIdCache.get(id);
        relation.remove("id");
        relation.put("id",newId);
        return relation;
    }
    private Map handleRefObjectOType(Map oType){
        String id = oType.get("id").toString();
        Integer newId = (Integer) otypeIdCache.get(id);
        oType.remove("id");
        oType.put("id",newId);
        return oType;
    }
}
