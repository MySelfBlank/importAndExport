package com.yzh.importTask.importUtils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yzh.api.MyApi;
import com.yzh.dao.EField;
import com.yzh.dao.EModel;
import com.yzh.dao.exportModel.*;
import com.yzh.userInfo.PathUtil;
import com.yzh.userInfo.UserInfo;
import com.yzh.utilts.tools.FileTools;
import onegis.psde.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static cn.hutool.core.util.ObjectUtil.isNull;
import static com.yzh.importTask.importUtils.IdCache.*;
import static com.yzh.utilts.tools.EnvironmentSelectTool.*;
import static com.yzh.utilts.tools.EnvironmentSelectTool.modelLocalUrl;
import static com.yzh.utilts.tools.FileTools.login;

/**
 * @author Yzh
 * @create 2021-01-14 9:21
 * @details
 */
public class OTypeImportUtil {
    //日志工厂
    private static final Logger logger = LoggerFactory.getLogger(OTypeImportUtil.class);


    public static void importOTpye () throws Exception{

        fieldOldIdAndNewIdCache.clear();
        formStylesOidAndNewId.clear();
        modelNewIdAndOldId.clear();

        IdCache.fieldOldIdAndNewIdCache.putAll(JSONUtil.toBean(FileTools.readFile(PathUtil.baseInfoDir+"\\fieldId.text"), HashMap.class));
        IdCache.formStylesOidAndNewId.putAll(JSONUtil.toBean(FileTools.readFile(PathUtil.baseInfoDir+"\\formId.text"),HashMap.class));
        IdCache.modelNewIdAndOldId.putAll(JSONUtil.toBean(FileTools.readFile(PathUtil.baseInfoDir+"\\modelId.text"),HashMap.class));


        logger.debug("类模板开始导入===========》读取文件");
        String oTypesStr = FileTools.readFile(PathUtil.baseInfoDir+"\\test.otype");
        List<EOType> eClasses = JsonUtils.jsonToList(oTypesStr, EOType.class);
        for (EOType eClass : eClasses) {
            if (!eClass.getFields().getFields().equals("[]")&&eClass.getFields().getFields()!=null){
                EFields eField=eClass.getFields();
                List<EField> fieldList = eField.getFields();
                List<EField> newFieldList = handleFieldId(fieldList);
                eField.setFields(newFieldList);
                eClass.setFields(eField);
            }
            if (!eClass.getFormStyles().getStyles().equals("[]")&&eClass.getFormStyles().getStyles()!=null){
                EFormStyless formStyles=eClass.getFormStyles();
                List<EFormStyles> formStyleList=eClass.getFormStyles().getStyles();
                List<EFormStyles> newFormStyles = handleFormStylesId(formStyleList);
                formStyles.setStyles(newFormStyles);
                eClass.setFormStyles(formStyles);
            }
            //连接关系处理为空
            if (!eClass.getModels().getModels().equals("[]")&&eClass.getModels().getModels()!=null){
                EModels models =eClass.getModels();
                List<EModel> modelList=eClass.getModels().getModels();
                List<EModel> newModels = handleModelId(modelList);
                models.setModels(newModels);
                eClass.setModels(models);
            }
            List<EOType> params = new ArrayList<>();
            params.add(eClass);
            String response = HttpUtil.post(MyApi.insertOtype.getValue()+"?token="+ UserInfo.token, JSONUtil.parseArray(params).toString());
            //错误判断
            if (FileTools.judgeImportState(response)){
                logger.error("id: "+eClass.getId()+"的类模板导入失败");
                continue;
            }
            //处理 response
            JSONArray array = FileTools.formatData2JSONArray(response);
            //上传完成将新老Id记录到Map当中
            otypeNewIdAndOldId.put(eClass.getId(), array.get(0, JSONObject.class).getLong("id"));
            logger.info("id：" +eClass.getId() + "导入完毕新Id为："+array.get(0,JSONObject.class).getLong("id"));
        }
    }

    /**
     * 将字段的老id替换为新id
     * @param fieldList
     * @return
     */
    public static List<EField> handleFieldId(List<EField> fieldList){
        List<EField> newFieldList=new ArrayList<>();
        for (EField field : fieldList) {
            Long newFiledId = Long.parseLong(String.valueOf(IdCache.fieldOldIdAndNewIdCache.get(String.valueOf(field.getId()))));
            field.setId(newFiledId);
            newFieldList.add(field);
        }
        return newFieldList;
    }

    /**
     * 对样式的id进行替换
     * @param styles
     * @return
     */
    public static List<EFormStyles> handleFormStylesId(List<EFormStyles> styles){
        List<EFormStyles> newFormStyles = new ArrayList<>();
        for (EFormStyles style : styles) {
            if (style.getId().equals("null")){
                return new ArrayList<>();
            }
            long newStyleId = 0L;
            if(style.getId()!=0&&style.getType()!=50){//ID为0的是模型，不处理
                Long id= Long.parseLong(String.valueOf(IdCache.formStylesOidAndNewId.get(String.valueOf(style.getId()))));
                if (isNull(id)){
                    continue;
                }
                newStyleId =  id;
            }
            style.setId(newStyleId);
            newFormStyles.add(style);
        }
        return newFormStyles;
    }

    /**
     * 对model和modelDef的id进行替换
     * @param models
     * @return
     */
    public static List<EModel> handleModelId(List<EModel> models){
        List<EModel> newModels = new ArrayList<>();
        for (EModel model : models) {
//            EModelDef newModelDef = ObjectUtil.clone(model.getMdef());
//            if (model.getMdef()!=null&&!model.getMdef().equals("")){
//                Long newModelDefId = IdCache.modelDefNewIdAndOldId.get(String.valueOf(model.getMdef().getId()));
//                newModelDef.setId(newModelDefId);
//            }
//            model.setMdef(newModelDef);
            Long newModelId =  Long.parseLong(String.valueOf(IdCache.modelNewIdAndOldId.get(String.valueOf(model.getId()))));
            model.setId(newModelId);
            newModels.add(model);
        }
        return newModels;
    }
    public static void main(String[] args) throws Exception {
        finalUrl = localHostUrl;
        finalUcUrl = localHostUcUrl;
        finalModelUrl = modelLocalUrl;
        login("ceshi@yzh.com", "123456");
        PathUtil.baseInfoDir="C:\\Users\\Cai\\Desktop\\demo\\测试八个方面1223";
        importOTpye();
        JSON parse = JSONUtil.parse(otypeNewIdAndOldId);
        FileTools.exportFile(parse,PathUtil.baseInfoDir+"\\otpyeId.text","otpyeId.text");
    }
}
