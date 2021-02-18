package com.yzh.importTask.importUtils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yzh.api.MyApi;
import com.yzh.dao.EField;
import com.yzh.dao.EModel;
import com.yzh.dao.ERelation;
import com.yzh.dao.exportModel.EFields;
import com.yzh.importTask.requestEntity.ModelEntity;
import com.yzh.importTask.requestEntity.RelationEntity;
import com.yzh.userInfo.PathUtil;
import com.yzh.userInfo.UserInfo;
import com.yzh.utilts.tools.FileTools;
import onegis.common.utils.JsonUtils;
import onegis.psde.attribute.Field;
import onegis.psde.attribute.Fields;
import onegis.psde.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.yzh.importTask.importUtils.IdCache.fieldOldIdAndNewIdCache;
import static com.yzh.importTask.importUtils.IdCache.relationNewIdAndOldId;
import static com.yzh.utilts.tools.FileTools.login;

;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/25 11:43
 */
public class RelationImportUtil {
    //日志工厂
    private static final Logger logger = LoggerFactory.getLogger(RelationImportUtil.class);


    /**
     * 导入关系,将id保存下来
     *
     * @param url
     */
    public static void upLoadRelation(String url,String fieldIdPath,String modelIdPath) throws Exception {
        String relationStr = FileTools.readFile(url);
        String fieldIdCache = FileTools.readFile(fieldIdPath);
        String modelIdCache = FileTools.readFile(modelIdPath);
        IdCache.fieldOldIdAndNewIdCache=JSONUtil.toBean(fieldIdCache, Map.class);
        IdCache.modelNewIdAndOldId=JSONUtil.toBean(modelIdCache,Map.class);


        //改转换方法
        List<ERelation> relations = JsonUtils.jsonToList(relationStr, ERelation.class);
        for (ERelation relation : relations) {
            logger.debug("关系开始导入==========》读取文件");
            RelationEntity relationEntity = new RelationEntity();
            //将获取到的关系文本内容转换为对象
            relationEntity.setId(relation.getId());
            relationEntity.setName(relation.getName());
            relationEntity.setMappingType(relation.getMappingType());
            relationEntity.setModel(exchangeModel(relation.getModel()));

            if (relation.getFields()!=null) {
                relationEntity.setFields(eFieldsToFields(relation.getFields()));
            }
            //处理响应的数据
            String paramStr = JSONUtil.parseObj(relationEntity).toString();
            String response = HttpUtil.post(MyApi.insertRelation.getValue()+"?token="+ UserInfo.token,paramStr );
            if (FileTools.judgeImportState(response)) {
                logger.error("name为" + relation.getName() + "的关系导入失败");
                continue;
            }
            //对新老的id进行处理
            JSONObject jsonObject = FileTools.formatData(response);

            relationNewIdAndOldId.put(relation.getId(), jsonObject.getLong("id"));
            logger.info("id" + relation.getId() + "新id为" +  jsonObject.getLong("id"));
        }
    }

    public static ModelEntity exchangeModel(EModel model) throws Exception {

        ModelEntity modelEntity = new ModelEntity();
        if (model.getpLanguage()==null){
            return new ModelEntity();
        }
        modelEntity.setpLanguage(Integer.valueOf(model.getpLanguage()));
        modelEntity.setId(Long.parseLong(String.valueOf(Optional.ofNullable(IdCache.modelNewIdAndOldId.get(model.getId().toString())).orElse(-1L))));
        return modelEntity;
    }
     public static EModel modelToEModel(Model model){
        EModel eModel = new EModel();
        eModel.setName(model.getName());
        return eModel;
    }
    public static Fields eFieldsToFields(EFields eFields){
        Fields fields = new Fields();
        List<EField> eFieldList = eFields.getFields();
        List<Field> fieldList = new ArrayList<>();
        for (EField eField : eFieldList) {
            Field field = new Field();
            //替换字段Id
            if(ObjectUtil.isNull(fieldOldIdAndNewIdCache.get(String.valueOf(eField.getId())))){

            }else {
                field.setId(Long.parseLong(String.valueOf(fieldOldIdAndNewIdCache.get(String.valueOf(eField.getId())))));
            }
            field.setName(eField.getName());
            field.setType(null);
            field.setUitype(null);
            fieldList.add(field);
        }
        fields.setFields(fieldList);
        return fields;
    }

    public static void main(String[] args) throws Exception {

        login("ceshi@yzh.com", "123456");
        PathUtil.baseInfoDir="C:\\Users\\bluethink\\Desktop\\导出数据\\测试八个方面1223";
        upLoadRelation( PathUtil.baseInfoDir+"\\test.relation", PathUtil.baseInfoDir+"\\fieldId.text", PathUtil.baseInfoDir+"\\modelId.text");
        JSON parse = JSONUtil.parse(relationNewIdAndOldId);
        FileTools.exportFile(parse, PathUtil.baseInfoDir+"\\relationId.text","relationId.text");
    }
}
