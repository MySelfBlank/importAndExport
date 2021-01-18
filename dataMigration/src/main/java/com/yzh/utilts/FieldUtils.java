package com.yzh.utilts;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yzh.api.MyApi;
import com.yzh.dao.EField;
import com.yzh.userInfo.UserInfo;
import onegis.psde.attribute.Attribute;
import onegis.psde.attribute.Field;
import onegis.psde.attribute.Fields;
import onegis.psde.psdm.SObject;
import onegis.psde.util.JsonUtils;
import org.springframework.util.StringUtils;

import java.util.*;

import static cn.hutool.core.util.ObjectUtil.isEmpty;
import static cn.hutool.core.util.ObjectUtil.isNull;


/**
 * @author Yzh
 * @create 2020-12-17 14:22
 * @Details 字段处理
 */
public class FieldUtils {

    public static List<Field> objectFieldsHandle(List<SObject> sObjects) throws Exception {
        List<Field> eFieldList = new ArrayList<>();
        //如果对象列表是空的则直接返回
        if (isNull(sObjects) || isEmpty(sObjects)) {
            return eFieldList;
        }
        List<Attribute> attributeList = new ArrayList<>();
        //属性FIdSet
        Set<Long> fIdSet = new HashSet<>();
        for (SObject sObject : sObjects) {
            if (isNull(sObject.getAttributes().getAttributeList()) || isEmpty(sObject.getAttributes().getAttributeList())) {
                continue;
            } else {
                attributeList.addAll(sObject.getAttributes().getAttributeList());
            }
        }
        //所有对象均未拿到属性信息，返回空的List
        if (isNull(attributeList) || isEmpty(attributeList)) {
            return eFieldList;
        }
        for (Attribute attribute : attributeList) {
            fIdSet.add(attribute.getFid());
        }
        //通过fId查询字段信息
        Map<String, Object> params = MapUtil.builder(new HashMap<String, Object>())
                .put("token", UserInfo.token)
                .put("orderType", "ID")
                .put("descOrAsc", true)
                .put("ids", fIdSet.toArray())
                .build();
        String fieldJsonStr = HttpUtil.get(MyApi.getFieldByFid.getValue(), params);
        JSONObject fieldJsonObj = FileTools.formatData(fieldJsonStr);
        List<JSONObject> fieldJsonObjList = JSONArray.parseArray(fieldJsonObj.get("list").toString(), JSONObject.class);
        eFieldList.addAll(JsonUtils.jsonToList(fieldJsonObj.get("list").toString(), Field.class));
        return eFieldList;
    }

    public static List<Field> objectFieldsHandle2(List<Attribute> attributes) throws Exception {
        List<Field> eFieldList = new ArrayList<>();

        //属性FIdSet
        Set<Long> fIdSet = new HashSet<>();
        //所有对象均未拿到属性信息，返回空的List
        if (isNull(attributes) || isEmpty(attributes)) {
            return eFieldList;
        }
        for (Attribute attribute : attributes) {
            fIdSet.add(attribute.getFid());
        }
        //通过fId查询字段信息
        Map<String, Object> params = MapUtil.builder(new HashMap<String, Object>())
                .put("token", UserInfo.token)
                .put("orderType", "ID")
                .put("descOrAsc", true)
                .put("ids", fIdSet.toArray())
                .build();

        String fieldJsonStr = HttpUtil.get(MyApi.getFieldByFid.getValue(), params);
        JSONObject fieldJsonObj = FileTools.formatData(fieldJsonStr);
        List<JSONObject> fieldJsonObjList = JSONArray.parseArray(fieldJsonObj.get("list").toString(), JSONObject.class);
        eFieldList.addAll(JsonUtils.jsonToList(fieldJsonObj.get("list").toString(), Field.class));
        return eFieldList;
    }

    /**
     * @param fields Json 字段项
     * @return
     */
    public static List<EField> dsField2Field(Fields fields) {
        List<EField> eFields = new ArrayList<>();
        //判断字段是否为空 为空直接返回
        if (isNull(fields) && isEmpty(fields)) {
            return eFields;
        }
        List<Field> fieldList = new ArrayList<>();
        if (!isNull(fields.getFields()) && !isEmpty(fields.getFields())) {
            fieldList.addAll(fields.getFields());
            //处理每一个字段信息
            for (Field field : fieldList) {
                eFields.add(field2EField(field));
            }
        }
        return eFields;
    }

    public static EField field2EField(Field field) {
        EField eField = new EField();
        eField.setId(field.getId());
        eField.setCaption(field.getCaption());
        eField.setDesc(field.getDes());
        eField.setName(field.getName());
        //值域处理
        Map<String, Object> domain = new HashMap<>();
        if (!StringUtils.isEmpty(field.getDomain())) {
            //值域存储的为JSON串
            Map<String, Object> map = JSON.parseObject(field.getDomain(), Map.class);
            map.forEach((k, v) -> {
                if (k.equals("Range")) {
                    domain.put("type", "list");
                } else {
                    domain.put("type", "range");
                }
                if (v instanceof List) {
                    List value = (List) v;
                    //移除空的集合
                    ((List<?>) v).removeAll(Collections.singleton(null));
                    if (isEmpty(v) && ((List<?>) v).size() == 0) {
                        domain.remove("type");
                        //结束本次循环
                        return;
                    }
                    domain.put("value", value);
                } else {
                    domain.put("value", v);
                }
            });

        }
        if (domain.size() > 0) {
            eField.setDomainMap(domain);
        }
        //类型处理
        if (field.getType().getName().equalsIgnoreCase("date")) {
            eField.setType("datetime");
        } else {
            eField.setType(field.getType().getName());
        }

        //默认值处理
        if (ObjectUtil.isNotNull(field.getDefaultValue())) {
            eField.setDefaultValue(field.getDefaultValue().toString());
        }
        return eField;
    }
}
