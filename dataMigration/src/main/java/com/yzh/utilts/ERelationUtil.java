package com.yzh.utilts;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yzh.api.MyApi;
import com.yzh.dao.EField;
import com.yzh.dao.EModel;
import com.yzh.dao.ERelation;
import com.yzh.dao.exportModel.EFields;
import com.yzh.userInfo.PathUtil;
import com.yzh.userInfo.UserInfo;
import com.yzh.utilts.tools.FileTools;
import onegis.psde.attribute.Field;
import onegis.psde.attribute.Fields;
import onegis.psde.model.Model;
import onegis.psde.psdm.SObject;
import onegis.psde.relation.Network;
import onegis.psde.relation.REdge;
import onegis.psde.relation.RNode;
import onegis.psde.relation.Relation;
import onegis.psde.util.JsonUtils;

import java.util.*;

import static cn.hutool.core.util.ObjectUtil.isEmpty;
import static cn.hutool.core.util.ObjectUtil.isNull;
import static com.yzh.utilts.tools.FileTools.exportFile;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/18 14:29
 */
public class ERelationUtil {
    /**
     * 获取连接网格
     *
     * @param sObjects
     */
    public static void getRelation(List<SObject> sObjects) throws Exception {

        Set<Long> RelationIds = new HashSet<>();
        if (sObjects == null || sObjects.size() == 0) {
            return;
        }
        //从对象中取出NetWork获取关系id
        for (SObject sObject : sObjects) {
            Network network = sObject.getNetwork();
            if (network == null || network.getNodes().size() == 0) {
                continue;
            }
            List<RNode> nodes = network.getNodes();
            RelationIds.addAll(getRelationId(nodes));
        }
        getRelationFile(RelationIds);
    }

    /**
     * 获取关系id
     *
     * @param nodes
     * @return
     */
    public static Set<Long> getRelationId(List<RNode> nodes) {
        Set<Long> RelationIds = new HashSet<>();
        if (isNull(nodes) || isEmpty(nodes)) {
            return new HashSet<>();
        }
        for (RNode node : nodes) {
            REdge edge = node.getEdge();
            if (edge == null) {
                continue;
            }
            Relation relation = edge.getRelation();
            if (relation == null) {
                continue;
            }
            RelationIds.add(relation.getId());
        }
        return RelationIds;
    }

    /**
     * 根据id打印关系数据到本地
     *
     * @param ids
     * @throws Exception
     */
    public static void getRelationFile(Set<Long> ids) throws Exception {
        //根据关系id打印出关系数据到本地
        Map<String, Object> param = new HashMap<>();
        param.put("token", UserInfo.token);
        param.put("ids", ids.toArray());
        param.put("loadField", true);
        param.put("loadModel", true);
        param.put("loadField", true);

        String relationStr = HttpUtil.get(MyApi.getRelationById.getValue(), param);
        JSONObject jsonObject = FileTools.formatData(relationStr);

        List<Relation> list = JsonUtils.jsonToList(jsonObject.getStr("list"), Relation.class);

        List<ERelation> eRelations = dsRelations2ERelation(list);
        String path = PathUtil.baseInfoDir + "\\test.relation";
        exportFile(JSONUtil.parse(eRelations), path, "relation");
    }

    /**
     * 对连接关系进行处理
     *
     * @param relations
     * @return
     * @throws Exception
     */
    public static List<ERelation> dsRelations2ERelation(List<Relation> relations) throws Exception {
        if (relations == null) {
            return new ArrayList<>();
        }
        List<ERelation> eRelations = new ArrayList<>();
        for (Relation relation : relations) {
            ERelation eRelation = new ERelation();
            eRelation.setId(relation.getId());
            eRelation.setName(relation.getName());
            eRelation.setFields(handleFieldsToEfields(relation.getFields()));
            eRelation.setMappingType(relation.getMappingType().getValue());
            eRelation.setModel(handOldModelToNewModel(relation.getModel()));
            eRelations.add(eRelation);
        }
        return eRelations;
    }

    public static EModel handOldModelToNewModel(Model model) {
        if (model == null) {
            return new EModel();
        }
        EModel newModel = new EModel();
        newModel.setId(model.getId());
        newModel.setName(model.getName());
        newModel.setpLanguage(String.valueOf(model.getpLanguage().getValue()));

        return newModel;
    }

    public static EFields handleFieldsToEfields(Fields fields) {
        EFields eFields = new EFields();
        if (isNull(fields) || isEmpty(fields)) {
            return new EFields();
        }
        List<EField> eFieldList = new ArrayList<>();
        List<Field> fieldList = fields.getFields();
        for (Field field : fieldList) {
            EField eField = new EField();
            eField.setId(field.getId());
            eField.setUitype(field.getUitype().getValue());
            eField.setType(field.getType().getName());
            eField.setName(field.getName());
            eFieldList.add(eField);
        }
        eFields.setFields(eFieldList);
        return eFields;
    }


}
