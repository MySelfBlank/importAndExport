package services.importData;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import enums.EFormEnum;
import model.*;
import onegis.common.utils.IdMakerUtils;
import onegis.psde.attribute.Attribute;
import onegis.psde.attribute.Attributes;
import onegis.psde.dictionary.FormEnum;
import onegis.psde.form.GeoBox;
import onegis.psde.model.Model;
import onegis.psde.model.Models;
import onegis.psde.psdm.Action;
import onegis.psde.psdm.OBase;
import onegis.psde.psdm.SObject;
import onegis.psde.reference.SpatialReferenceSystem;
import onegis.psde.reference.TimeReferenceSystem;
import onegis.psde.relation.Network;
import onegis.psde.util.JsonUtils;
import org.apache.commons.lang.StringUtils;
import utils.FileUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReadSObject {

    /**记录消亡对象*/
    public static List<CustomerSObject> deleteSObjectList = new ArrayList<>();

    /**
     * 读取本地的SObject对象文件
     * @param filePath
     * @param setFid
     * @return
     * @throws Exception
     */
    public static List<CustomerSObject> readFromFile(String filePath, boolean setFid) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        String objectJson = FileUtils.readFile(filePath);
        /**替换对象属性名，方便转为自定义的类型*/
        objectJson = objectJson.replaceAll("\"srs\"", "\"srsStr\"").replaceAll("\"trs\"", "\"trsStr\"").replaceAll("\"geoBox\"", "\"geoBoxList\"").replaceAll("\"models\"", "\"modelList\"").replaceAll("\"parent\"", "\"parentId\"");
        List<CustomerSObject>  customerSObjects = objectMapper.readValue(objectJson, new TypeReference<List<CustomerSObject>>(){});

        for (CustomerSObject customerSObject : customerSObjects) {
            // 设置action
            ArrayList<Action> actions = new ArrayList<>();
            Action addAction = new Action();
            addAction.setId(customerSObject.getId());
            addAction.setOperation(Action.ADDING | Action.BASE);
            addAction.setsTime(null);
            addAction.seteTime(null);
            actions.add(addAction);
            customerSObject.setActions(actions);

            // 设置父对象
            Long parentId = customerSObject.getParentId();
            if (parentId != null) {
                List<OBase> parents = new ArrayList<>();
                OBase oBase = new OBase();
                oBase.setId(parentId);
                parents.add(oBase);
                customerSObject.setParents(parents);
            }
            // 时间参考
            String trsStr = customerSObject.getTrsStr();
            if (trsStr != null && !trsStr.isEmpty()) {
                trsStr = trsStr.substring(trsStr.indexOf(":")+1, trsStr.length());
                trsStr = trsStr.replaceAll(" ", "");
                if (trsStr != null && !trsStr.isEmpty()) {
                    TimeReferenceSystem trs = new TimeReferenceSystem();
                    trs.setId(trsStr);
                    customerSObject.setTrs(trs);
                }
            }

            // 空间参考
            String srsStr = customerSObject.getSrsStr();
            if (srsStr != null && !srsStr.isEmpty()) {
                srsStr = srsStr.substring(srsStr.indexOf(":")+1, srsStr.length());
                srsStr = srsStr.replaceAll(" ", "");
                if (srsStr != null && !srsStr.isEmpty()) {
                    SpatialReferenceSystem srs = new SpatialReferenceSystem();
                    srs.setId(srsStr);
                    customerSObject.setSrs(srs);
                }
            }
            // geoBox
            List<Double> geoBoxList = customerSObject.getGeoBoxList();
            if (geoBoxList != null && geoBoxList.size() > 0) {
                GeoBox geoBox = new GeoBox(geoBoxList.get(0), geoBoxList.get(1), geoBoxList.get(2), geoBoxList.get(3), geoBoxList.get(4), geoBoxList.get(5));
                customerSObject.setGeoBox(geoBox);
            }

            // Models
            List<Model> modelList = customerSObject.getModelList();
            if (modelList != null && !modelList.isEmpty()) {
                Models models = new Models();
                models.addModels(modelList);
                customerSObject.setModels(models);
            }
            // 设置forms的id和fid和geotype
            ArrayList<Form1> form1s = customerSObject.getForms();
            if (form1s != null && !form1s.isEmpty()) {
                form1s.forEach(f -> {if (setFid) {f.setFid(f.getId());} f.setGeotype(f.getType().getValue());});
            }
            customerSObject.setForms(form1s);//

            ENetWork network = customerSObject.getNetwork();
            if(network!=null){
                if(network.getNodes()!=null){
                    List<ERNode> nodes = network.getNodes();
                    for(ERNode node:nodes){
                        if(node.getRefObject()!=null){
                            node.setLabel(node.getRefObject().getName());
                        }
                        if(node.getProperties()==null||node.getProperties().size()==0){
                            node.setProperties(null);
                        }
                    }
                    network.setNodes(nodes);
                }

            }
            customerSObject.setNetwork(network);
        }
        setProperties(customerSObjects);
        setName(customerSObjects);
        return customerSObjects;
    }


    public static List<CustomerSObject> buildSObjectWithVersion(List<CustomerSObject> CustomerSObjectList) throws Exception {
        deleteSObjectList.clear();
        List<CustomerSObject> result = new ArrayList<>();
        for (int i=0; i<CustomerSObjectList.size(); i++) {
            CustomerSObject customerSObject = CustomerSObjectList.get(i);
            // 获取对象所有版本
            List<EVersion> versions = customerSObject.getVersions();
            if (versions == null || versions.isEmpty()) {
                continue;
            }
            Map<Long, Long> lastIds = new HashMap<>();
            ArrayList<Form1> forms = customerSObject.getForms();
            if (forms != null && !forms.isEmpty()) {
                forms.forEach(form1 -> lastIds.put(form1.getFid(), form1.getId()));
            }
            for(EVersion eVersion : versions){
                CustomerSObject customerSObjectVersion = buildSObjectByVersion(customerSObject, eVersion, lastIds);
                customerSObjectVersion.setVersions(new ArrayList<>());
                customerSObject = customerSObjectVersion;
                result.add(customerSObjectVersion);
                /**
                 * 创建消亡版本
                 */
                Boolean isDelete = customerSObject.getDeleteVersion();
                if (isDelete != null && isDelete) {
                    Long oid = customerSObjectVersion.getId();
                    CustomerSObject deleteSobject = new CustomerSObject();
                    deleteSobject.setRealTime(customerSObjectVersion.getRealTime());
                    deleteSobject.setId(oid);
                    deleteSobject.setUuid(new IdMakerUtils().nextId());
                    deleteSobject.setName(customerSObjectVersion.getName());
                    deleteSobject.setVersion(customerSObjectVersion.getVersion());
                    ArrayList<Action> deleteActionList = new ArrayList<>();
                    Action action1 = new Action();
                    action1.setId(oid);
                    action1.setOperation(Action.DELETE | Action.BASE);
                    deleteActionList.add(action1);
                    deleteSobject.setActions(deleteActionList);

                    deleteSObjectList.add(deleteSobject);
                }
            }
        }
        result.forEach(o -> o.setVersions(new ArrayList<>()));
        setProperties(result);
        setName(result);
        return result;
    }

    /**
     * 编辑对象版本信息
     * @param CustomerSObject
     * @param eVersion
     * @param lastIds
     * @return
     * @throws Exception
     */
    private static CustomerSObject buildSObjectByVersion(CustomerSObject CustomerSObject, EVersion eVersion, Map<Long, Long> lastIds) throws Exception {
        CustomerSObject result = new CustomerSObject();
        result.setId(CustomerSObject.getId());
        result.setVersions(CustomerSObject.getVersions());
        result.setVersion(CustomerSObject.getVersion());
        result.setActions(CustomerSObject.getActions());
        result.setCode(CustomerSObject.getCode());
        result.setNetwork(CustomerSObject.getNetwork());
        result.setRealTime(CustomerSObject.getRealTime());
        result.setModels(CustomerSObject.getModels());
        result.setSrs(CustomerSObject.getSrs());
        result.setGeoBox(CustomerSObject.getGeoBox());
        result.setTrs(CustomerSObject.getTrs());
        result.setDatas(CustomerSObject.getDatas());
        result.setDynamicData(CustomerSObject.getDynamicData());
        result.setForms(new ArrayList<>(CustomerSObject.getForms()));
        result.setGeoBoxList(CustomerSObject.getGeoBoxList());
        result.setModelList(CustomerSObject.getModelList());
        result.setName(CustomerSObject.getName());
        result.setAttributes(CustomerSObject.getAttributes());
        result.setOtype(CustomerSObject.getOtype());
        result.setParents(CustomerSObject.getParents());
        result.setSdomain(CustomerSObject.getSdomain());
        result.setChildren(CustomerSObject.getChildren());
        result.setDataRef(CustomerSObject.getDataRef());
        result.setParents(CustomerSObject.getParents());

        Long realTime = eVersion.getVtime();
        result.setRealTime(realTime);

        List<EAction> actions = eVersion.getActions();
        if (actions == null || actions.isEmpty()) {
            return null;
        }
        /**
         * 该版本包含的变化信息
         */
        List<EAttribute> attributes_version = eVersion.getAttributes() == null ? new ArrayList<>() : eVersion.getAttributes();
        List<EForm> forms_version = eVersion.getForms() == null ? new ArrayList<>() : eVersion.getForms();
        List<EModel> models_version = eVersion.getModels() == null ? new ArrayList<>() : eVersion.getModels();
        ENetWork netWork_version = eVersion.getNetwork();
        /**
         * 对象原有信息，用于修改时进行替换
         */
        List<Attribute> attributes = new ArrayList<>(); //result.getAttributes() == null ? new ArrayList<>() : result.getAttributes().getAttributeList();
        if (result.getActions() != null && result.getAttributes().getAttributeList() != null) {
            result.getAttributes().getAttributeList().forEach(attribute -> attributes.add(attribute));
        }
        List<Form1> forms = result.getForms() == null ? new ArrayList<>() : result.getForms();
        forms.clear();
        ArrayList<Action> actionList = new ArrayList<>();
        for (EAction action : actions) {
            EActionEvent eActionEvent = action.getOperation();
            if (eActionEvent != null && "DELETE".equals(eActionEvent.getActionOperationType()) && "BASE".equals(eActionEvent.getObjectOperationType())) {
                result.setDeleteVersion(true);
            }

            Action action1 = new Action();
            Long id = action.getId() == null ? 0 : action.getId();
            action1.setId(id);

            EActionEvent operation = action.getOperation();
            String actionOperationType = operation.getActionOperationType() == null ? "" : operation.getActionOperationType();
            String objectOperationType = operation.getObjectOperationType() == null ? "" : operation.getObjectOperationType();

            switch (objectOperationType){
                case "BASE" :
                    action1.setId(result.getId());
                    action1.setOperation(Action.MODIFY | Action.BASE);
                    if (eVersion.getBase() != null) {
                        String code = eVersion.getBase().getCode();
                        if (StringUtils.isNotBlank(code)) {
                            result.setCode(code);
                        }

                        List<OBase> parents = eVersion.getBase().getParentList();
                        if (parents != null && !parents.isEmpty()) {
                            result.setParents(eVersion.getBase().getParentList());
                        }

                        TimeReferenceSystem trs = eVersion.getBase().getTrs();
                        if (trs != null) {
                            result.setTrs(trs);
                        }

                        SpatialReferenceSystem srs = eVersion.getBase().getSrs();
                        if (srs != null) {
                            result.setSrs(srs);
                        }
                        Long vtime = eVersion.getVtime();
                        if (vtime != null) {
                            result.setRealTime(vtime);
                        }

                    }
                    break;
                /**
                 * 编辑属性
                 */
                case "ATTRIBUTE":
                    if (actionOperationType.equals("DELETE")) {
                        action1.setOperation(Action.DELETE | Action.ATTRIBUTE);
                    }
                    // 去除该fid的属性
                    List<Attribute> attributeList = new ArrayList<>(attributes);
                    Integer removeIndex = null;
                    for (int i=0; i<attributes.size(); i++) {
                        Attribute attribute = attributes.get(i);
                        if (id != null && id.equals(attribute.getFid())) {
                            attributes.remove(attribute);
                        }
                    }

                    if (actionOperationType.equals("ADDING") || actionOperationType.equals("MODIFY")) {
                        if (actionOperationType.equals("ADDING")) {
                            action1.setOperation(Action.ADDING | Action.ATTRIBUTE);
                        }
                        if (actionOperationType.equals("MODIFY")) {
                            action1.setOperation(Action.MODIFY | Action.ATTRIBUTE);
                        }
                        // 获取增加的属性
                        List<EAttribute> addAttr = attributes_version.stream().filter(a -> id.equals(a.getFid())).collect(Collectors.toList());
                        if (addAttr.isEmpty()) {
                            continue;
                        }
                        EAttribute eAttribute = addAttr.get(0);
                        Attribute attribute = new Attribute();
                        attribute.setFid(eAttribute.getFid());
                        attribute.setName(eAttribute.getName());
                        attribute.setValue(eAttribute.getValue());
                        attributes.add(attribute);

                        if ("name".equals(attribute.getName()) || "city_name".equals(attribute.getName())) {
                            result.setName(attribute.getValue().toString());
                        }
                    }
                    break;
                /**
                 * 编辑形态
                 */
                case "FORM":
                    if (actionOperationType.equals("DELETE")) {
                        if (lastIds.get(id) != null) {
                            action1.setId(lastIds.get(id));
                        }
                        action1.setOperation(Action.DELETE | Action.FORM);
                    }
                    // 去除该fid的形态
                    List<Form1> form1List = new ArrayList<>(forms);
                    for (Form1 form1 : form1List) {
                        if (id != null && id.equals(form1.getId())) {
                            forms.remove(form1);
                        }
                    }
                    if (actionOperationType.equals("ADDING") || actionOperationType.equals("MODIFY")) {
                        Long newId = new IdMakerUtils().nextId();
                        if (actionOperationType.equals("ADDING")) {
                            action1.setOperation(Action.ADDING | Action.FORM);
                        }
                        if (actionOperationType.equals("MODIFY")) {
                            action1.setOperation(Action.MODIFY | Action.FORM);
                        }
                        // 获取变化的形态
                        List<EForm> addForm = forms_version.stream().filter(a -> id.equals(a.getId())).collect(Collectors.toList());
                        if (addForm.isEmpty()) {
                            continue;
                        }
                        EForm eForm = addForm.get(0);
                        Form1 form1 = new Form1();
                        if (eForm.getGeom() != null && eForm.getGeom().getData() != null && eForm.getGeom().getData().getGeotype() != null && StringUtils.isNotBlank(eForm.getGeom().getData().getGeotype())) {
                            String geoTypeStr = eForm.getGeom().getData().getGeotype();
                            if (geoTypeStr.equals("point")) {
                                form1.setGeotype(21);
                            }
                            if (geoTypeStr.equals("linestring")) {
                                form1.setGeotype(22);
                            }
                            if (geoTypeStr.equals("polygon")) {
                                form1.setGeotype(23);
                            }
                            if (geoTypeStr.equals("multipoint") || geoTypeStr.equals("multiline") || geoTypeStr.equals("multipolygon")) {
                                form1.setGeotype(23);
                            }

                            EGeom eGeom = eForm.getGeom().getData();
                            CustomerGeom customerGeom = new CustomerGeom();
                            CustomerGeom.CustomerData customerData = new CustomerGeom.CustomerData();
                            customerData.setCoordinates(eGeom.getCoordinates());
                            customerData.setGeotype(eGeom.getGeotype());
                            customerGeom.setData(customerData);
                            form1.setGeom(customerGeom);

                        }
                        if (eForm.getType() != null && StringUtils.isNotBlank(eForm.getType())) {
                            String geoTypeStr = eForm.getType();
                            if (geoTypeStr.equals("point")) {
                                form1.setType(EFormEnum.POINT);
                            }
                            if (geoTypeStr.equals("linestring")) {
                                form1.setType(EFormEnum.LINESTRING);
                            }
                            if (geoTypeStr.equals("polygon")) {
                                form1.setType(EFormEnum.POLYGON);
                            }
                            if (geoTypeStr.equals("bim")) {
                                form1.setType(EFormEnum.BIM);
                            }
                            if (geoTypeStr.equals("model")) {
                                form1.setType(EFormEnum.MODEL);
                            }
                        }

                        Long fid = eForm.getFid();
                        form1.setFid(newId);
                        form1.setId(fid);
                        if (lastIds.get(id) != null) {
                            form1.setId(lastIds.get(id));
                        }
                        if (actionOperationType.equals("ADDING")) {
                            form1.setId(newId);
                        }
                        lastIds.put(id, form1.getFid());

                        action1.setId(form1.getId());

                        form1.setDim(eForm.getDim());
                        form1.setMinGrain(eForm.getMinGrain());
                        form1.setMaxGrain(eForm.getMaxGrain());
                        form1.setStyle(eForm.getStyle());
                        PositionSerialize formRef = new PositionSerialize();
                        EFormRef eFormRef = eForm.getFormRef();
                        if (eFormRef != null) {
                            formRef.setName(eFormRef.getName());
                            formRef.setFname(eFormRef.getFname());
                            formRef.setDesc(eFormRef.getDesc());
                            formRef.setExtension(eFormRef.getExtension());
                        }

                        form1.setFormRef(formRef);

                        forms.add(form1);
                    }
                    break;
                /**
                 * 编辑关系
                 */
                case "RELATION":
                    if (actionOperationType.equals("DELETE")) {
                        action1.setOperation(Action.DELETE | Action.RELEATION);
                    }
                    if (actionOperationType.equals("ADDING")) {
                        action1.setOperation(Action.ADDING | Action.RELEATION);
                    }
                    if (actionOperationType.equals("MODIFY")) {
                        action1.setOperation(Action.MODIFY | Action.RELEATION);
                    }
                    if(netWork_version!=null){
                        if(netWork_version.getNodes()!=null){
                            List<ERNode> nodes = netWork_version.getNodes();
                            for(ERNode node:nodes){
                                if(node.getRefObject()!=null){
                                    node.setLabel(node.getRefObject().getName());
                                }
                            }
                        }
                    }
                    result.setNetwork(netWork_version);
                    break;
                /**
                 * 编辑模型
                 */
                case "MODEL":
                    if (actionOperationType.equals("ADDING")) {
                        action1.setOperation(Action.ADDING | Action.MODEL);
                    }
                    if (actionOperationType.equals("DELETE")) {
                        action1.setOperation(Action.DELETE | Action.MODEL);
                    }
                    if (actionOperationType.equals("MODIFY")) {
                        action1.setOperation(Action.MODIFY | Action.MODEL);
                    }
                    break;
                /**
                 * 编辑位置
                 */
                case "POSITION":
                    if (actionOperationType.equals("ADDING")) {

                    }
                    if (actionOperationType.equals("DELETE")) {

                    }
                    if (actionOperationType.equals("MODIFY")) {

                    }
                    break;
            }

            if (action1.getOperation() != null) {
                actionList.add(action1);
            }
        }
        Attributes attributes1 = new Attributes();
        attributes1.setAttributeList(attributes);
        for (Attribute attribute : attributes) {
            Long attrId = attribute.getFid();
            List<Action> actionList1 = actionList.stream().filter(action -> attrId.equals(action.getId())).collect(Collectors.toList());
            if (actionList1.isEmpty()) {
                Action action = new Action();
                action.setId(attrId);
                action.setOperation(Action.MODIFY | Action.ATTRIBUTE);

                actionList.add(action);
            }
        }
        result.setAttributes(attributes1);
        result.setVersions(new ArrayList<>());
        result.setActions(actionList);

        return result;

    }

    private static void setProperties(List<CustomerSObject> CustomerSObjectList) {
        for (CustomerSObject customerSObject : CustomerSObjectList) {
            ENetWork network = customerSObject.getNetwork();
            if (network == null) {
                continue;
            }

            List<ERNode> nodes = network.getNodes();
            if (nodes == null || nodes.isEmpty()) {
                continue;
            }
            for (ERNode node : nodes) {
                EObase refObject = node.getRefObject();
                if (refObject != null) {
                    node.setRelatedObjectId(refObject.getId().toString());
                }
                List<ERNode.Properties> properties  = node.getProperties();
                if (properties == null || properties.isEmpty()) {
                    node.setProperties(null);
                    continue;
                }
                Map<String, Object> map = new HashMap<>();
                properties.forEach(p -> map.put(p.getKey(), p.getValue()));

                node.setProperties(null);
                node.setNodeProperties(map);
            }

            network.setNodes(nodes);
        }
    }


    private static void setName(List<CustomerSObject> CustomerSObjectList) {
        for (CustomerSObject customerSObject : CustomerSObjectList) {
            Attributes attributes = customerSObject.getAttributes();
            if (attributes == null) {
                continue;
            }
            List<Attribute> attributeList = attributes.getAttributeList();
            if (attributeList == null || attributeList.isEmpty()) {
                continue;
            }

            for (Attribute attribute : attributeList) {
                String name = attribute.getName();
                Object value = attribute.getValue();
                if (("name".equals(name) || "city_name".equals(name)) && value != null) {
                    customerSObject.setName(value.toString());
                    break;
                }
            }

        }
    }


    /**
     * 替换ID
     * @param orgContent
     * @param id
     * @param erNodeList node需要单独替换ID
     * @return
     */
    public static String changID(String orgContent, String id, List<ERNode> erNodeList){
        Map<String, Map<String, String>> idMaps = ReadID.idMaps;
        Map<String, String> idMap = idMaps.get(id);
        for(String key:idMap.keySet()){
            orgContent = orgContent.replaceAll(key, idMap.get(key));
        }
        /**单独提取node中的id*/
        if(erNodeList!=null&&erNodeList.size()>0){
            for(ERNode erNode:erNodeList){
                String relatedObjectId = erNode.getRelatedObjectId();
                String nodeId = idMaps.get(relatedObjectId).get(relatedObjectId);
                orgContent = orgContent.replaceAll(relatedObjectId, nodeId);
            }
        }
        return orgContent;
    }
}
