package com.yzh.importTask.importUtils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.yzh.api.MyApi;
import com.yzh.dao.EModel;
import com.yzh.dao.EModelDef;
import com.yzh.importTask.requestEntity.Model;
import com.yzh.importTask.requestEntity.ModelDefEntity;
import com.yzh.importTask.requestEntity.ModelEntity;
import com.yzh.userInfo.PathUtil;
import com.yzh.userInfo.UserInfo;
import com.yzh.utilts.tools.FileTools;
import com.yzh.utilts.HttpClientUtils;
import onegis.common.utils.JsonUtils;
import onegis.psde.attribute.Field;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static com.yzh.importTask.importUtils.FieldImportUtil.fieldImport;
import static com.yzh.importTask.importUtils.IdCache.*;
import static com.yzh.utilts.tools.FileTools.login;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2021/1/12 14:53
 */
public class ModelImportUtil {
    //日志工厂
    private static final Logger logger = LoggerFactory.getLogger(ModelImportUtil.class);

    /**
     * 对行为的导入
     * @param url
     */
    public static void modelImportHandle(String url,String modelUrl,String idPath) throws Exception{
        logger.debug("模型开始导入=====》读取文件");
        String modelStr = FileTools.readFile(url);
        String idCash = FileTools.readFile(idPath);
        Map<Long,Long> map = JSONUtil.toBean(idCash, Map.class);
        modelDefNewIdAndOldId.clear();
        modelDefNewIdAndOldId.putAll(map);
        List<EModel> models = JsonUtils.jsonToList(modelStr, EModel.class);

        for (EModel model : models) {
            ModelEntity modelEntity = new ModelEntity();
            modelEntity.setName(model.getName());
            EModelDef mdef = model.getMdef();
            ModelDefEntity modelDefEntity = new ModelDefEntity();
            //在缓存找不到的话去查一下
            if (ObjectUtil.isNull(modelDefNewIdAndOldId.get(mdef.getId().toString()))){
                List<EModelDef> list = FileTools.forJsonList(HttpUtil.get(MyApi.getModelDefById.getValue() + "?names=" + model.getMdef().getName()), EModelDef.class);
                modelDefEntity.setId(list.get(0).getId());
            }else {
                modelDefEntity.setId(Long.parseLong(String.valueOf(modelDefNewIdAndOldId.get(mdef.getId().toString()))));
            }
            //EModelDef newModelDef = JSONUtil.parse(modelDefEntity).toBean(EModelDef.class);
            modelEntity.setMdef(modelDefEntity);

            modelEntity.setpLanguage(Integer.valueOf(model.getpLanguage()));
            if (model.getpLanguage().equals("1")||model.getpLanguage().equals("2")){

                if (model.getMobj().getScript()!=null) {
                    if (!model.getMobj().getScript().isEmpty()) {
//                    uploadModles(modelUrl);
                        modelEntity.setMobj(model.getMobj());

                    }
                }
            } {
                String[] mobj = new String[]{};
                modelEntity.setMobj(null);
            }
            modelImportHandle(modelEntity,model);
        }

    }
    public static void modelImportHandle(ModelEntity modelEntity,EModel model) throws Exception {
        //处理响应的数据
        JSONObject paramStr = JSONUtil.parseObj(modelEntity);
        String response = HttpUtil.post(MyApi.insertModel.getValue()+"?token="+ UserInfo.token, paramStr.toString());
        if (FileTools.judgeImportState(response)) {
            logger.error("name为" + model.getName() + "的关系导入失败");
            return;
        }
        //对新老的id进行处理
        JSONObject jsonObject1 = FileTools.formatData(response);

        modelNewIdAndOldId.put(model.getId(), jsonObject1.getLong("id"));
        logger.debug("id" + model.getId() + "新id为" + jsonObject1.getLong("id"));
    }



    /**
     * 上传模型
     * @param directoryPath
     * @return
     * @throws Exception
     */
    private static Map<String, String> uploadModles(String directoryPath) throws Exception{
        Map<String, String> resultMap = new HashMap<>();
        // 读取该路径下所有文件
        List<File> fileList = new ArrayList<>();
        fileList=FileTools.getFiles(directoryPath);
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
                Model model = uploadModel(file);
                if (model!=null){
                    resultMap.put(model.getFname(), model.getFid());
                }
            }

        }
        return resultMap;
    }

    /**
     * 通过接口上传模型
     * @param file
     * @return
     * @throws Exception
     */
    public static Model uploadModel(File file) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(),
                ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
        Map<String,String> params = new HashMap<>();
        params.put("token",UserInfo.token);
        params.put("name",file.getName().substring(0, file.getName().lastIndexOf(".")));
        Map<String, MultipartFile> multipartFileMap = new HashMap<>();
        multipartFileMap.put("file", multipartFile);
        String result = HttpClientUtils.doPostByte(MyApi.uploadModel.getValue(), params, multipartFileMap);
        onegis.result.response.ResponseResult responseResult = JsonUtils.jsonToPojo(result, onegis.result.response.ResponseResult.class);
        if (responseResult.getStatus() == 200) {
            Model model = JSON.parseObject(JSON.toJSONString(responseResult.getData()), Model.class);
            System.out.println("模型" +  model.getFname() +"保存成功，fid：" + model.getFid());
            return model;
        }else {
            System.out.println("模型" +  file.getName() +"保存失败" );
        }
        return null;
    }
    /**
     * 对行为类别进行导入
     * @param url
     */
    public static void modelDefImportHandle(String url,String idPath) throws IOException {
        String modelDefStr = FileTools.readFile(url);
        String idCache = FileTools.readFile(idPath);
        fieldOldIdAndNewIdCache=JSONUtil.toBean(idCache,Map.class);
        List<EModelDef> modelDefs = FileTools.jsonArray2List(modelDefStr, EModelDef.class);

        for (EModelDef modelDef : modelDefs) {
            logger.debug("行为类别开始导入======》文件对象转换");
            ModelDefEntity modelDefEntity = new ModelDefEntity();
            if (modelDef.getIcon().equals("")){
                modelDefEntity.setIcon(modelDef.getIcon());
            }
            modelDefEntity.setName(modelDef.getName());
            modelDefEntity.setType(modelDef.getType());
            //对输入类型id进行处理
            if (modelDef.getInTypes().equals("[]")||modelDef.getInTypes().isEmpty()) {
                modelDefEntity.setInTypes(modelDef.getInTypes());
            }else{
                JSONArray array = JSONUtil.parseArray(modelDef.getInTypes());
                List<Field> foreInFields = JsonUtils.jsonToList(array.toString(), Field.class);
                //调用字段的导入
                fieldImport(foreInFields);
                List<Field> eFields = new ArrayList<>();
                for (int i = 0; i <foreInFields.size(); i++) {
                    Field field = foreInFields.get(i);
                    field.setId(Optional.ofNullable(fieldOldIdAndNewIdCache.get(foreInFields.get(i).getId())).orElse(-1L));
                    eFields.add(field);
                }
                String newModelDef = JSONUtil.parse(eFields).toString();
                modelDefEntity.setInTypes(newModelDef);
            }
            //对输出类型id进行处理
            if (modelDef.getOutTypes().isEmpty()||modelDef.getOutTypes().equals("[]")) {
                modelDefEntity.setOutTypes(modelDef.getOutTypes());
            }else {
                JSONArray array = JSONUtil.parseArray(modelDef.getOutTypes());
                List<Field> foreFields = JsonUtils.jsonToList(array.toString(), Field.class);
                //调用字段的导入
                fieldImport(foreFields);
                List<Field> eFields = new ArrayList<>();
                for (int i = 0; i <foreFields.size(); i++) {
                    Field field = foreFields.get(i);
                    Long id = Optional.ofNullable(fieldOldIdAndNewIdCache.get(foreFields.get(i).getId())).orElse(-1L);
                    field.setId(id);
                    eFields.add(field);
                }
                String newModelDef = JSONUtil.parse(eFields).toString();
                modelDefEntity.setOutTypes(newModelDef);
            }
            modelDefEntity.setDes(modelDef.getDes());
            modelDefEntity.setTags(modelDef.getTags());

            //处理响应的数据
            String paramStr = JSONUtil.parseObj(modelDefEntity).toString();
            String response = HttpUtil.post(MyApi.insertModelDef.getValue()+"?token="+ UserInfo.token, paramStr);
            if (FileTools.judgeImportState(response)) {
                logger.error("name为" + modelDef.getName() + "的关系导入失败");
                continue;
            }
            //对新老的id进行处理
            JSONObject jsonObject = FileTools.formatData(response);

            modelDefNewIdAndOldId.put(modelDef.getId(), jsonObject.getLong("id"));
            logger.info("id" + modelDef.getId() + "新id为" + jsonObject.getLong("id"));
        }

    }

    public static void main(String[] args) throws Exception {
        login("ceshi@yzh.com", "123456");
        PathUtil.baseInfoDir="C:\\Users\\bluethink\\Desktop\\导出数据\\测试八个方面1223";
//        modelDefImportHandle(PathUtil.baseInfoDir+"\\test.modelDef",PathUtil.baseInfoDir+"\\fieldId.text");
        modelImportHandle(PathUtil.baseInfoDir+"\\test.models",PathUtil.baseInfoDir+"\\data",PathUtil.baseInfoDir+"\\modelDefId.text");
        cn.hutool.json.JSON parseDef = JSONUtil.parse(modelNewIdAndOldId);
        FileTools.exportFile(parseDef,"E:\\test\\测试八个方面1223\\modelId.text","modelId.text");
    }
}
