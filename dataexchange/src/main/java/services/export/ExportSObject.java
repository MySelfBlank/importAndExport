package services.export;

import enums.ConstantDict;
import model.*;
import onegis.common.utils.FileUtils;
import onegis.common.utils.GeneralUtils;
import onegis.common.utils.JsonUtils;
import onegis.psde.attribute.Attribute;
import onegis.psde.attribute.Attributes;
import onegis.psde.compose.Compose;
import onegis.psde.compose.ComposeElement;
import onegis.psde.form.Form;
import onegis.psde.form.Forms;
import onegis.psde.form.GeoBox;
import onegis.psde.model.Model;
import onegis.psde.model.ModelDef;
import onegis.psde.model.Models;
import onegis.psde.psdm.OBase;
import onegis.psde.psdm.OType;
import onegis.psde.psdm.SObject;
import onegis.psde.psdm.Version;
import onegis.psde.reference.SpatialReferenceSystem;
import onegis.psde.reference.TimeReferenceSystem;
import onegis.psde.relation.Network;
import org.apache.commons.lang.StringUtils;
import services.export.action.BuildAttributeAction;
import services.export.action.BuildBaseAction;
import services.export.action.BuildFormAction;
import services.export.action.BuildRelationAction;
import utils.ENetWorkUtils;
import utils.PathUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExportSObject {

    public static void writeSObject(List<SObject> sObjects, Integer region) {
        Map<Long, List<Long>> dobjectMap = getEDObjectMap();
        List<ESObject> esObjects = dsSObjects2ex(sObjects, dobjectMap);
        /**对数据ID进行重置*/
        resetId(esObjects);
        FileUtils.writeContent(JsonUtils.objectToJson(esObjects),
                PathUtil.baseDir, ConstantDict.SOBJECT_DATA_FILE_NAME.getName() + String.format("(%s)", region), false);
    }

    public static Map<Long, List<Long>> getEDObjectMap() {
        Map<Long, List<Long>> map = new HashMap<>(8);
        List<EDObject> dObjectList = ExecuteContainer.dObjectList;
        if (dObjectList.size() > 0) {
            dObjectList.forEach(d -> {
                if (map.containsKey(d.getDataSource())) {
                    map.get(d.getDataSource()).add(d.getId());
                } else {
                    map.put(d.getDataSource(), new ArrayList<Long>() {{
                        add(d.getId());
                    }});
                }
            });
        }
        return map;
    }

    /**
     * 空间对象列表转交换格式空间列表
     *
     * @param sObjects   空间对象列表
     * @param dobjectMap 空间对象对应产生的数据文件ID
     * @return List<ESObject>
     */
    public static List<ESObject> dsSObjects2ex(List<SObject> sObjects, Map<Long, List<Long>> dobjectMap) {
        List<ESObject> esObjects = new ArrayList<>();
        if (!GeneralUtils.isNotEmpty(sObjects)) {
            System.out.println("空间对象列表null");
            return esObjects;
        }
        HashMap<Long, List<SObject>> sobjectGroups = groupSObjects(sObjects);
        Map<Long, List<SObject>> collect = sObjects.stream().collect(Collectors.groupingBy(sObject -> sObject.getId()));
        // 获取所有对象的版本变化
        Map<Long, List<EVersion>> allEVersionMap = buildEVersions(sobjectGroups);  //判断基本信息是否发生变化
        for (Map.Entry<Long, List<SObject>> resMap : sobjectGroups.entrySet()) {
            Long oid = resMap.getKey();
            List<SObject> resList = resMap.getValue();
            ESObject esObject = buildESObject(resList, dobjectMap);
            if (esObject != null) {
                List<EVersion> eVersions = allEVersionMap.get(oid);
                if (GeneralUtils.isNotEmpty(eVersions)) {
                    esObject.setVersions(eVersions);
                }
                esObjects.add(esObject);
            }
        }
        return esObjects;
    }

    /**
     * 根据空间对象分组列表，构建一个增量版本的ESObject
     *
     * @param resList 空间对象列表
     * @return ESObject
     */
    public static ESObject buildESObject(List<SObject> resList, Map<Long, List<Long>> dobjectMap) {
        if (!GeneralUtils.isNotEmpty(resList)) {
            return null;
        }
        // 找出对象版本最小的那个，也就是当前列表中对象的起始版本
        SObject startSObject = getOriginal(resList);
        if (startSObject == null) {
            return null;
        }
        SpatialReferenceSystem spatialReferenceSystem = startSObject.getSrs();
        TimeReferenceSystem timeReferenceSystem = startSObject.getTrs();
        if (spatialReferenceSystem != null) {
            ExecuteContainer.addSrsSystem(startSObject.getSrs());
            ExecuteContainer.addSrsSystemId(spatialReferenceSystem.getId());
        }

        if (timeReferenceSystem != null) {
            ExecuteContainer.addTrsSystem(startSObject.getTrs());
            ExecuteContainer.addTrsSystemId(timeReferenceSystem.getId());
        }

        OType oType = startSObject.getOtype();
        if (oType != null && oType.getId() != null) {
            ExecuteContainer.addOType(oType);
            ExecuteContainer.addOTypeId(oType.getId());
        }

        return buildESObject(startSObject, dobjectMap);
    }

    /**
     * 构建空间对象交换格式对象ESObject
     */
    public static ESObject buildESObject(SObject startSObject, Map<Long, List<Long>> dobjectMap) {

        if (startSObject == null) {
            return null;
        }
        //获取初始版本中已有的属性
//        List<Attribute> attributeList = startSObject.getAttributes().getAttributeList();
//        attributeList.forEach(a -> actionIds.add(a.getFid()));
//        operationID = 0;

        ESObject esObject = new ESObject();
        setInfo(esObject, startSObject, dobjectMap);
        setOType(esObject, startSObject);
        setAttribute(esObject, startSObject);
        setModels(esObject, startSObject);
        setNetWork(esObject, startSObject);
        setForms(esObject, startSObject);
        setCompose(esObject, startSObject);
        return esObject;
    }

    /**
     * 拿到对象列表中，初始状态的对象
     *
     * @param sObjects 空间对象列表
     * @return SObject
     */
    public static SObject getOriginal(List<SObject> sObjects) {

        if (!GeneralUtils.isNotEmpty(sObjects)) {
            return null;
        }
        SObject sObject = new SObject();
        Long minVid = Long.MAX_VALUE;
        for (SObject object : sObjects) {
            Version version = object.getVersion();
            if (version == null || version.getVid() == null) {
                continue;
            }
            Long vid = version.getVid();
            if (vid < minVid) {
                minVid = vid;
                sObject = object;
            }
        }
        return sObject;
    }

    /**
     * 空间对象列表分组
     *
     * @param sObjects
     * @return
     */
    public static HashMap<Long, List<SObject>> groupSObjects(List<SObject> sObjects) {
        // 1、对象分组
        HashMap<Long, List<SObject>> sobjectGroups = new HashMap<>();
        if (!GeneralUtils.isNotEmpty(sObjects)) {
            return sobjectGroups;
        }
        for (SObject sObject : sObjects) {
            Long oId = sObject.getId();
            List<SObject> lists;
            if (sobjectGroups.containsKey(oId)) {
                lists = sobjectGroups.get(oId);
                lists.add(sObject);
            } else {
                lists = new ArrayList<>();
                lists.add(sObject);
            }
            sobjectGroups.put(oId, lists);
        }
        return sobjectGroups;
    }

    /**
     * 判断基本信息是否发生变化
     *
     * @param allSObjects
     * @return
     */
    public static Map<Long, List<EVersion>> buildEVersions(Map<Long, List<SObject>> allSObjects) {
        Map<Long, List<EVersion>> result = new HashMap<>();
        for (Map.Entry<Long, List<SObject>> entry : allSObjects.entrySet()) {
            Long oid = entry.getKey();
            // 该对象所有版本,如果只有一个版本，则
            List<SObject> sObjects = entry.getValue();
            if (sObjects.size() <= 1) {
                continue;
            }
            List<EVersion> thisEVersions = buildBaseActions(sObjects);

            result.put(oid, thisEVersions);
        }
        return result;
    }

    /**
     * 构建单个对象的基本信息变化,目前只判断code是否发生变化
     *
     * @param sObjects
     * @return
     */
    private static List<EVersion> buildBaseActions(List<SObject> sObjects) {
        List<EVersion> list = new ArrayList<>();
        SObject lastSObject = sObjects.get(0);
        for (int i = 1; i < sObjects.size(); i++) {
            SObject thisSObject = sObjects.get(i);
            EVersion eVersion = new EVersion();
            eVersion.setVtime(thisSObject.getRealTime());
            // 计算基本信息的变化
            BuildBaseAction.setBaseAction(eVersion, lastSObject, thisSObject);
            // 计算属性信息的变化
            BuildAttributeAction.setAttributeAction(eVersion, lastSObject, thisSObject);
            // 计算组成结构的变化
            // 计算形态的变化
            BuildFormAction.setFormAction(eVersion, lastSObject, thisSObject);
            // 计算行为的变化
            // 计算关系的变化
            BuildRelationAction.setRelationAction(eVersion, lastSObject, thisSObject);

            list.add(eVersion);
            lastSObject = thisSObject;
        }
        return list;
    }

    /**
     * 设置基本信息【id、name、时空参考、时空域等】
     *
     * @param esObject ESObject
     * @param sObject  SObject
     */
    public static void setInfo(ESObject esObject, SObject sObject, Map<Long, List<Long>> dobjectMap) {

        esObject.setId(sObject.getId());
        esObject.setName(sObject.getName());
        esObject.setCode(!GeneralUtils.isNotEmpty(sObject.getCode()) ? "" : sObject.getCode());

        esObject.setTrs("onegis:1001");
        TimeReferenceSystem trs = sObject.getTrs();
        String trsId = trs.getId();
        String trsAuthName = trs.getAuthName();
        if (trs != null && trsId != null && trsAuthName != null && !trsId.isEmpty() && !trsAuthName.isEmpty()) {
            esObject.setTrs(String.format("%s%s", trsAuthName == null || trsAuthName.isEmpty() ? "" : trsAuthName + ":", trsId));
        }

        esObject.setSrs("epsg:4326");
        SpatialReferenceSystem srs = sObject.getSrs();
        String srsId = srs.getId();
        String srsAuthName = srs.getAuthName();
        if (srs != null && srsId != null && srsAuthName != null && !srsId.isEmpty() && !srsAuthName.isEmpty()) {
            esObject.setSrs(String.format("%s%s", srsAuthName == null || srsAuthName.isEmpty() ? "" : srsAuthName + ":", srs.getId()));
        }

        esObject.setSdomain(sObject.getSdomain() == null ? 0L : sObject.getSdomain());
        Long realTime = sObject.getRealTime();

//        if (GeneralUtils.isNotEmpty(realTime)) {
//            if (10 == realTime.toString().length()) {
//                esObject.setRealTime(sObject.getRealTime() * 1000);
//            } else {
//                esObject.setRealTime(sObject.getRealTime());
//            }
//        }

        if (realTime != null) {
            esObject.setRealTime(sObject.getRealTime());
        }

        GeoBox geoBox = sObject.getGeoBox();
        esObject.addGeobox(geoBox.getMinx(),
                geoBox.getMiny(), geoBox.getMinz(),
                geoBox.getMaxx(), geoBox.getMaxy(), geoBox.getMaxz());

        List<OBase> parents = sObject.getParents();
        if (GeneralUtils.isNotEmpty(parents)) {
            esObject.setParent(parents.get(0).getId());
        }

        esObject.setDataSource(sObject.getFrom());
        if (dobjectMap.containsKey(sObject.getId())) {
            List<Long> datas = dobjectMap.get(sObject.getId());
            esObject.setDataGenerate(!GeneralUtils.isNotEmpty(datas) ? new ArrayList<>() : datas);
        }

    }

    /**
     * 设置对象类
     */
    public static void setOType(ESObject esObject, SObject sObject) {

//        EClasses eClasses = new EClasses();
        OType otype = sObject.getOtype();
//        eClasses.setId(otype.getId());
//        eClasses.setName(otype.getName());
        Map<String, Object> otypeMap = new HashMap<>(2);
        otypeMap.put("id", otype.getId());
        otypeMap.put("name", otype.getName());
        esObject.setOtype(otypeMap);
    }

    /**
     * 设置属性
     */
    public static void setAttribute(ESObject esObject, SObject sObject) {
        Attributes attributes = sObject.getAttributes();
        if (attributes == null) {
            return;
        }
        List<Attribute> attributeList = attributes.getAttributeList();
        List<EAttribute> eAttributes = getEAttributes(attributeList);
        esObject.setAttributes(eAttributes);
    }


    /**
     * 获取 List<EAttribute>
     */
    public static List<EAttribute> getEAttributes(List<Attribute> attributeList) {
        List<EAttribute> eAttributes = new ArrayList<>();
        for (Attribute attribute : attributeList) {
            EAttribute eAttr = new EAttribute();
            eAttr.setFid(attribute.getFid());
            eAttr.setName(attribute.getName());
            eAttr.setValue(attribute.getValue());
            eAttributes.add(eAttr);
        }
        return eAttributes;
    }

    /**
     * 设置行为
     */
    public static void setModels(ESObject esObject, SObject sObject) {

        Models models = sObject.getModels();
        if (models == null) {
            return;
        }
        List<Model> modelList = models.getModels();
        if (!GeneralUtils.isNotEmpty(modelList)) {
            return;
        }
        List<EModel> eModels = new ArrayList<>();
        for (Model model : modelList) {
            EModel eModel = new EModel();
            eModel.setId(model.getId());
            eModel.setName(model.getName());
            eModel.setInitData(model.getInitData());

            EModelDef eModelDef = new EModelDef();
            ModelDef mdef = model.getMdef();
            eModelDef.setId(mdef.getId());
            eModelDef.setName(mdef.getName());

            eModel.setMdef(eModelDef);
            eModel.setpLanguage(model.getpLanguage().getName());
            eModel.setExecutor(model.getExecutor());
            eModels.add(eModel);
        }
        esObject.setModels(eModels);
    }

    /**
     * 设置关系
     */
    public static void setNetWork(ESObject esObject, SObject sObject) {

        Network network = sObject.getNetwork();
        ENetWork eNetWork = ENetWorkUtils.buildNetWork(network);
        if (eNetWork == null) {
            return;
        }
        esObject.setNetwork(eNetWork);
    }

    /**
     * 设置形态
     */
    public static void setForms(ESObject esObject, SObject sObject) {

        Forms forms = sObject.getForms();
        if (forms == null) {
            return;
        }

        List<EForm> eForms = getEForms(forms);
        if (eForms == null) {
            return;
        }
        esObject.setForms(eForms);
    }

    /**
     * 取EForm
     *
     * @param forms
     * @return
     */
    public static List<EForm> getEForms(Forms forms) {

        List<Form> formList = forms.getForms();
        if (!GeneralUtils.isNotEmpty(formList)) {
            return null;
        }

        List<EForm> eForms = new ArrayList<>();
        for (Form form : formList) {
            EForm eForm = BuildFormAction.getEForm(form);
            if (eForm != null) {
                eForms.add(eForm);
            }
        }
        return eForms;
    }

    /**
     * 设置组成结构
     */
    public static void setCompose(ESObject esObject, SObject sObject) {

        Compose compose = sObject.getCompose();
        if (compose == null) {
            return;
        }

        List<ECompose> eComposes = new ArrayList<>();

        ECompose eCompose = new ECompose();
        eCompose.setId(compose.getId());
        eCompose.setName(eCompose.getName());
        eCompose.setDes(compose.getDes());
        eCompose.setStrong(compose.getStrong());


        List<ComposeElement> elements = compose.getElements();

        if (!GeneralUtils.isNotEmpty(elements)) {
            return;
        }

        List<EComposeElement> eComposeElements = new ArrayList<>();
        for (ComposeElement element : elements) {

            EComposeElement eComposeElement = new EComposeElement();
            eComposeElement.setOid(element.getOid());
            eComposeElement.setName(element.getName());
            eComposeElement.setMatrix(element.getMatrix());
            eComposeElements.add(eComposeElement);
        }

        eCompose.setElements(eComposeElements);
        eComposes.add(eCompose);
        esObject.setCompose(eComposes);
    }

    /**
     * 重置数据ID
     *
     * @param esObjecs
     */
    private static void resetId(List<ESObject> esObjecs) {
        for (ESObject esObject : esObjecs) {
            Long id = esObject.getId();
            List<String> idList = new ArrayList<>();
            /**
             * 对象ID
             */
            idList.add(id + "");
            List<EForm> forms = esObject.getForms();
            if (forms != null && forms.size() > 0) {
                for (EForm form1 : forms) {
                    /**
                     * 形态ID
                     */
                    String oldFormId = form1.getId().toString();
                    idList.add(oldFormId + "");

                    String oldFormFId = form1.getFid().toString();
                    idList.add(oldFormFId + "");
                    /**
                     * 位置ID
                     */
                    EPosition customerGeom = form1.getGeom();
                    if (customerGeom != null && customerGeom.getId() != null && StringUtils.isNotBlank(customerGeom.getId().toString())) {
                        Long geomId = customerGeom.getId();
                        idList.add(geomId + "");
                    }
                }
            }
            ExecuteContainer.addNewId(id + "", idList);
        }
    }


}
