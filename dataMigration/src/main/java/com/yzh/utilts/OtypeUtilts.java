package com.yzh.utilts;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.yzh.api.MyApi;
import com.yzh.dao.EField;
import com.yzh.dao.EModel;
import com.yzh.dao.EModelDef;
import com.yzh.dao.exportModel.*;
import com.yzh.userInfo.PathUtil;
import com.yzh.userInfo.UserInfo;
import onegis.psde.attribute.Field;
import onegis.psde.attribute.Fields;
import onegis.psde.form.FormStyle;
import onegis.psde.form.FormStyles;
import onegis.psde.model.Model;
import onegis.psde.model.ModelDef;
import onegis.psde.model.Models;
import onegis.psde.psdm.OType;
import onegis.psde.psdm.SObject;
import onegis.psde.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static cn.hutool.core.util.ObjectUtil.isEmpty;
import static cn.hutool.core.util.ObjectUtil.isNull;
import static com.yzh.Index.*;
import static com.yzh.utilts.tools.FileTools.exportFile;
import static com.yzh.utilts.tools.FileTools.formatData;

/**
 * @author Yzh
 * @create 2020-12-16 17:08
 */
public class OtypeUtilts {
    private static Set<Long> classIDs = new HashSet<>();
    private static final Logger logger = LoggerFactory.getLogger(OtypeUtilts.class);
    static Map<String, Object> params = new HashMap<>();
    static List<JSONObject> jsonObjects = new ArrayList<>();

    public static void getOtype(String DOTypeName) throws Exception {
        params.put("sdomains", UserInfo.domain);
        params.put("loadForm", true);
        params.put("loadDynamicData", false);
        params.put("loadModel", true);
        params.put("loadNetwork", true);
        params.put("loadObjType", true);
        params.put("loadDes", true);
        params.put("loadAction", true);
        params.put("loadChildren", true);
        params.put("loadVersion", true);
        params.put("orderType", "VID");
        params.put("descOrAsc", false);
        String objectJsonStr = HttpUtil.get(MyApi.getObject.getValue(), params);
        JSONObject data = formatData(objectJsonStr);

        List<JSONObject> objectList = null;
        String objectListStr = data.getStr("list");
        List<SObject> sObjects = JsonUtils.jsonToList(objectListStr, SObject.class);
        sObjectsList.addAll(sObjects);
//        objectList = JSONArray.parseArray(objectListStr, JSONObject.class);

        //获取数据对象
        //getDObjectList(sObjects);
        //处理SObject

        //获取当前时空域下的所有类模板Id

        for (SObject sObject : sObjectsList) {
            classIDs.add(sObject.getOtype().getId());
        }
        //获取当前时空域下的所有类模板Id
//        for (JSONObject o : objectList) {
//            JSONObject otype = (JSONObject) o.get("otype");
//            classIDs.add(otype.getLong("id"));
//        }
        logger.debug("当前时空域下使用所有的类模板Id=" + classIDs);
        EConnectorUtils.EConnectorHandel(classIDs);
        params.clear();
        params.put("token", UserInfo.token);
        params.put("ids", classIDs.toArray());
        String otypeInfoStr = HttpUtil.get(MyApi.getOtypesByIds.getValue(), params);
        JSONObject otypeInfoJson = formatData(otypeInfoStr);
        //传递过去对象集合对行为类进行处理
        jsonObjects.addAll(JsonUtils.jsonToList(otypeInfoJson.getStr("list"), JSONObject.class));
//        EModelUtil.getEModel(jsonObjects);
        //需要将轨迹数据使用的类模板拿到(根据)
        if (StrUtil.isNotBlank(DOTypeName) && StrUtil.isNotEmpty(DOTypeName)) {
            //如果输入了轨迹输入的类模板则去查找
            params.clear();
            params.put("token", UserInfo.token);
            params.put("names",DOTypeName);
            String responseStr = HttpUtil.get(MyApi.getOtypesByIds.getValue(), params);
            if (JSONUtil.parseObj(responseStr).getStr("status").equals("200")){
                JSONObject dotypeInfoJson = formatData(responseStr);
                List<OType> list = JsonUtils.jsonToList(dotypeInfoJson.getStr("list"), OType.class);
                if (list.size()==0){
                    throw new RuntimeException("未找到轨迹数据模板");
                }else if (list.size()==1){
                    oTypeList.addAll(list);
                }else {
                    System.out.println("查询到的类模板大于1,无法唯一确定");
                }
            }
        }

        oTypeList.addAll(JsonUtils.jsonToList(otypeInfoJson.getStr("list"), OType.class));
        //处理类模板到可导入状态
        List<EOType> eoTypes = handleOType2EOType(oTypeList);

        //打印类模板
        JSON parse = JSONUtil.parse(eoTypes);
        exportFile(parse, PathUtil.baseInfoDir + "\\test.otype", "Otype");
    }

    public static List<EOType> handleOType2EOType(List<OType> oTypes) {
        List<EOType> eoTypes = new ArrayList<>();
        for (OType oType : oTypes) {
            EOType eoType = new EOType();
            eoType.setId(oType.getId());
            eoType.setName(oType.getName());
            eoType.setDes(oType.getDes());
            eoType.setCode(oType.getCode());
            eoType.setTags(oType.getTags());
            eoType.setIcon(oType.getIcon());
            //处理字段
            eoType.setFields(handleFields(oType.getFields()));
            eoType.setSrs(oType.getSrs());
            eoType.setTrs(oType.getTrs());
            //处理形态样式
            List<EFormStyles> eFormStyles = handleEFormStyle(oType.getFormStyles());
            EFormStyless eFormStyless = new EFormStyless();
            eFormStyless.setStyles(eFormStyles);
            eoType.setFormStyles(eFormStyless);
            //处理行为
            List<EModel> eModels = handleEModel(oType.getModels());
            EModels models = new EModels();
            models.setModels(eModels);
            eoType.setModels(models);
            eoTypes.add(eoType);
        }
        return eoTypes;
    }

    private static List<EFormStyles> handleEFormStyle(FormStyles formStyles) {
        List<EFormStyles> eFormStyles = new ArrayList<>();
        if (isNull(formStyles) || isEmpty(formStyles)) {
            return eFormStyles;
        }
        List<FormStyle> styles = formStyles.getStyles();
        for (FormStyle style : styles) {
            EFormStyles eFormStyle = new EFormStyles();
            eFormStyle.setId(style.getId());
            eFormStyle.setName(style.getName());
            eFormStyle.setData(style.getData());
            eFormStyle.setDes(style.getDes());
            eFormStyle.setDim(style.getDim());
            eFormStyle.setMaxGrain(style.getMaxGrain());
            eFormStyle.setMinGrain(style.getMinGrain());
            eFormStyle.setStyle(Long.parseLong(String.valueOf(style.getStyle().getValue())));
            eFormStyle.setType(Long.parseLong(String.valueOf(style.getType().getValue())));
            eFormStyles.add(eFormStyle);
        }
        return eFormStyles;
    }

    private static List<EModel> handleEModel(Models models) {
        List<EModel> eModels = new ArrayList<>();
        if (isEmpty(models) || isNull(models)) {
            return eModels;
        }
        List<Model> modelS = models.getModels();
        for (Model model : modelS) {
            EModel eModel = new EModel();
            eModel.setId(model.getId());
            eModel.setName(model.getName());
            //处理mdef
            eModel.setMdef(handleModel(model.getMdef()));
//            eModel.setpLanguage(model.getpLanguage().getName());
            eModel.setExecutor(model.getExecutor());
            eModels.add(eModel);
        }
        return eModels;
    }

    public static EModelDef handleModel(ModelDef modelDef) {
        EModelDef eModelDef = new EModelDef();
        eModelDef.setId(modelDef.getId());
        eModelDef.setName(modelDef.getName());
        eModelDef.setActions(modelDef.getActions());
        eModelDef.setMtime(modelDef.getMtime());
        eModelDef.setInTypes(modelDef.getInTypes());
        eModelDef.setOutTypes(modelDef.getOutTypes());
        eModelDef.setIcon(modelDef.getIcon());
        eModelDef.setDes(modelDef.getDes());
        eModelDef.setType(modelDef.getType().getValue());
        return eModelDef;
    }

    public static EFields handleFields(Fields fields) {
        EFields efields = new EFields();
        if (isNull(fields) || isEmpty(fields)) {
            return efields;
        }
        List<EField> efieldList = new ArrayList<>();
        List<Field> fieldList = fields.getFields();
        for (Field field : fieldList) {
            EField efield = new EField();
            efield.setId(field.getId());
            efield.setName(field.getName());
            efieldList.add(efield);
        }
        efields.setFields(efieldList);
        return efields;
    }
}
