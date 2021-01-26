package com.yzh.utilts;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.google.protobuf.ByteString;
import com.yzh.Index;
import com.yzh.api.MyApi;
import com.yzh.dao.EForm;
import com.yzh.dao.EFormRef;
import com.yzh.dao.ExecuteContainer;
import com.yzh.userInfo.UserInfo;
import onegis.common.utils.GeneralUtils;
import onegis.common.utils.HttpUrlConnectionUtils;
import onegis.common.utils.StreamUtils;
import onegis.protobuf.model.PbModelData;
import onegis.psde.dictionary.FormEnum;
import onegis.psde.form.AForm;
import onegis.psde.form.Form;
import onegis.psde.form.FormStyle;
import onegis.psde.form.ModelBlock;
import onegis.psde.psdm.SObject;
import onegis.psde.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static cn.hutool.core.util.ObjectUtil.*;

/**
 * @author Yzh
 * @create 2020-12-18 17:03
 * @details
 */
public class FormUtils {
    private static final Logger logger = LoggerFactory.getLogger(Index.class);
    public static List<Form> objectFromsHandle(List<SObject> sObjects) throws Exception {
        List<Form> fromList = new ArrayList<>();
        if (isNull(sObjects) || isEmpty(sObjects)) {
            return fromList;
        }
        List<Form> forms = new ArrayList<>();
        for (SObject sObject : sObjects) {
            if (isEmpty(sObject.getForms()) || isNull(sObject.getForms())) {
                continue;
            } else {
                forms.addAll(sObject.getForms().getForms());
            }
        }
        if (isNull(forms) || isEmpty(forms)) {
            return fromList;
        }
        Set<Long> fids = new HashSet<>();
        for (Form form : forms) {
            fids.add(form.getFid());
        }
        //通过fId查询字段信息
        Map<String, Object> params = MapUtil.builder(new HashMap<String, Object>())
                .put("token", UserInfo.token)
                .put("orderType", "ID")
                .put("descOrAsc", true)
                .put("ids", fids.toArray())
                .build();
        String formJsonStr = HttpUtil.get(MyApi.getFieldByFid.getValue(), params);
        JSONObject formJsonObj = FileTools.formatData(formJsonStr);
        List<JSONObject> fieldJsonObjList = JSONArray.parseArray(formJsonObj.get("list").toString(), JSONObject.class);
        fromList.addAll(JsonUtils.jsonToList(formJsonObj.get("list").toString(), Form.class));

        return fromList;
    }

    public static List<FormStyle> objectFromsHandle2(List<Form> fromList) throws Exception {
        if (isNull(fromList) || isEmpty(fromList)) {
            return new ArrayList<>();
        }
        Set<String> formList = new HashSet<>();
        StringBuffer buffer = new StringBuffer();
        for (Form form : fromList) {
            //形态不是模型的时候取样式id
            if (!form.getType().getName().equalsIgnoreCase("model")&&!form.getType().getName().equalsIgnoreCase("bim")) {
                //取形态中的样式Id
                JSONArray jsonArray = JSONArray.parseArray(form.getStyle());
                if (isNotNull(jsonArray) && isNotEmpty(jsonArray)) {
                    for (Object o : jsonArray) {
                        buffer.append("," + o);
                    }
                }

            }
        }
        //去除第一位多余的，
        buffer.deleteCharAt(0);
        System.out.println(buffer);
        String[] split = buffer.toString().split(",");
        formList.addAll(Arrays.asList(split));
        System.out.println(formList);
        //请求样式数据
        Map<String, Object> params = MapUtil.builder(new HashMap<String, Object>())
                .put("token", UserInfo.token)
                .put("orderType", "ID")
                .put("descOrAsc", true)
                .put("ids", formList.toArray())
                .build();
        String styleJsonStr = HttpUtil.get(MyApi.getStyleById.getValue(), params);
        JSONObject stylejsonObj = FileTools.formatData(styleJsonStr);
        String styleListStr = stylejsonObj.getStr("list");
        if (StringUtils.isEmpty(styleListStr)) {
            return new ArrayList<>();
        }
        List<FormStyle> formStyles = JsonUtils.jsonToList(styleListStr, FormStyle.class);
        return formStyles;
    }

    public static List<FormStyle> otpyeFromsHandle2(List<FormStyle> fromList) throws Exception {
        if (isNull(fromList) || isEmpty(fromList)) {
            return new ArrayList<>();
        }
        Set<Long> formList = new HashSet<>();
        StringBuffer buffer = new StringBuffer();
        List<FormStyle> formStyles = new ArrayList<>();
        for (FormStyle form : fromList) {
            //形态不是模型的时候取样式id
            if(form.getName()!=null){
                formList.add(form.getId());
                if (form.getName().contains("default_")){
                    formStyles.add(form);
                }
            }

        }
//        for (Form form : fromList) {
//            //形态不是模型的时候取样式id
//            if (!form.getType().getName().equalsIgnoreCase("model")&&!form.getType().getName().equalsIgnoreCase("bim")) {
//                //取形态中的样式Id
//                JSONArray jsonArray = JSONArray.parseArray(form.getStyle());
//                if (isNotNull(jsonArray) && isNotEmpty(jsonArray)) {
//                    for (Object o : jsonArray) {
//                        buffer.append("," + o);
//                    }
//                }
//
//            }
//        }
        //去除第一位多余的，
//        buffer.deleteCharAt(0);
//        System.out.println(buffer);
//        String[] split = buffer.toString().split(",");
//        formList.addAll(Arrays.asList(split));
        System.out.println("需要导出的样式Id"+formList);
        //请求样式数据
        Map<String, Object> params = MapUtil.builder(new HashMap<String, Object>())
                .put("token", UserInfo.token)
                .put("orderType", "ID")
                .put("descOrAsc", true)
                .put("ids", formList.toArray())
                .build();
        String styleJsonStr = HttpUtil.get(MyApi.getStyleById.getValue(), params);
        JSONObject stylejsonObj = FileTools.formatData(styleJsonStr);
        String styleListStr = stylejsonObj.getStr("list");
        if (StringUtils.isEmpty(styleListStr)) {
            return new ArrayList<>();
        }
         formStyles.addAll( JsonUtils.jsonToList(styleListStr, FormStyle.class));
        return formStyles;
    }

    public static List<EForm> dsForms2EForm(List<Form> forms) throws Exception {
        if (isEmpty(forms) || isNull(forms)) {
            return new ArrayList<>();
        }
        List<EForm> eForms = new ArrayList<>();
        for (Form form : forms) {
            getFormModel(form);
            EForm eForm = dsForm2EForm(form);
            eForms.add(eForm);
        }

        return eForms.stream().distinct().collect(Collectors.toList());
    }

    public static EForm dsForm2EForm(Form form) {
        EForm eForm = new EForm();
        eForm.setId(form.getId());
        eForm.setDim(form.getDim());
        eForm.setType(FormEnum.getEnum(form.getType().getValue()).getName());
        eForm.setMaxGrain(form.getMaxGrain());
        eForm.setMinGrain(form.getMinGrain());
        eForm.setFormRef(dsFormRef2FormRef(form.getFormref()));
        eForm.setStyle(form.getStyle());
        return eForm;
    }

    private static EFormRef dsFormRef2FormRef(AForm aForm) {
        if (aForm == null) {
            return null;
        }
        EFormRef eFormRef = new EFormRef();
        if (aForm instanceof ModelBlock) {
            ModelBlock block = (ModelBlock) aForm;
            eFormRef.setName(block.getName());
            eFormRef.setDesc(block.getDes());
            eFormRef.setExtension(block.getExtension());
            eFormRef.setFname(block.getFname());
            eFormRef.setFid(block.getRefid());
        }
        return eFormRef;
    }

    /**
     * 获取形态中的引用模型
     * @param form
     */
    public static void getFormModel(Form form){
        if (form.getFormref().getRefid()==0){
            return;
        }
        if (!form.getType().getName().equals("Model")){
            return;
        }
        EFormRef eFormRef = new EFormRef();
        if (FormEnum.MODEL.equals(form.getType())) {
            ModelBlock modelBlock = (ModelBlock) form.getFormref();
            eFormRef.setName(GeneralUtils.isNotEmpty(modelBlock.getName()) ? modelBlock.getName() : "");
            eFormRef.setDesc(GeneralUtils.isNotEmpty(modelBlock.getDes()) ? modelBlock.getDes() : "");

            String fname = modelBlock.getFname();
            if (GeneralUtils.isNotEmpty(fname)){
                fname = fname.replaceAll("/", "_").replaceAll(":", "");
            }
            fname = fname + "_" + modelBlock.getRefid();
            if (!GeneralUtils.isNotEmpty(fname) || fname.contains("?")){
                fname = modelBlock.getName() + "_" + modelBlock.getRefid() + "";
            }
            eFormRef.setFname(fname);
            eFormRef.setExtension(GeneralUtils.isNotEmpty(modelBlock.getExtension()) ? modelBlock.getExtension() : "");

            if (!"".equals(eFormRef.getName()) && !"".equals(eFormRef.getFname())) {
                dsForm2EForm(form).setFormRef(eFormRef);
                Long modelID = modelBlock.getRefid();
                ExecuteContainer.addModelId(modelID);
                ExecuteContainer.addModelName(modelID+"", fname);
            } else if (modelBlock.getRefid() != null) {
                Long fid = modelBlock.getRefid();

                try {
                    // 读取模型信息(线上)
                    List<ModelBlock> modelBlocks = getModel(fid);
                    if (modelBlocks != null && modelBlocks.size() > 0) {
                        modelBlock = modelBlocks.get(0);
                        eFormRef.setName(GeneralUtils.isNotEmpty(modelBlock.getName()) ? modelBlock.getName() : "");
                        eFormRef.setDesc(GeneralUtils.isNotEmpty(modelBlock.getDes()) ? modelBlock.getDes() : "");

                        fname = modelBlock.getFname();
                        if (GeneralUtils.isNotEmpty(fname)){
                            fname = fname.replaceAll("/", "_").replaceAll(":", "");
                        }
                        fname = fname + "_" + modelBlock.getRefid();
                        if (!GeneralUtils.isNotEmpty(fname) || fname.contains("?")){
                            fname = modelBlock.getName() + "_" + fid + "";
                        }
                        eFormRef.setFname(fname);

                        eFormRef.setExtension(GeneralUtils.isNotEmpty(modelBlock.getExtension()) ? modelBlock.getExtension() : "");
                        dsForm2EForm(form).setFormRef(eFormRef);

                        ExecuteContainer.addModelId(fid);
                        ExecuteContainer.addModelName(fid+"", fname);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取模型信息
     * @param fid
     * @return
     */
    public static List<ModelBlock> getModel(Long fid) throws Exception {
        Map<String, Object> params =MapUtil.builder(new HashMap<String, Object>())
                .put("fid",fid).build();
        String modelInfo = HttpUtil.get(MyApi.getModelInfo.getValue(), params);
        if (modelInfo!=null){
            List<ModelBlock> modelBlocks = JsonUtils.jsonToList(JsonUtils.objectToJson(modelInfo), ModelBlock.class);
            return modelBlocks;
        }
        return new ArrayList<>();
    }


    public static void downLoadModel(Set<Long> modelIds,String savePath){
        for (Long modelId : modelIds) {
            if (!GeneralUtils.isNotEmpty(modelId)) {
                return;
            }
            try {
                System.out.println("----:"+modelId);
                InputStream inputStream = HttpUrlConnectionUtils.getInputStream(MyApi.downModelUrl.getValue() + modelId, "POST", null);
                byte[] result = StreamUtils.inputStreamToByte(inputStream);
                PbModelData.Pb3DModelResponseResult resData = PbModelData.Pb3DModelResponseResult.newBuilder().build().parseFrom(result);
                if (resData.getStatus() == 200) {
                    PbModelData.Pb3DModelFile fileMode = resData.getFileModel();
                    ByteString fileData = fileMode.getFileData();
                    byte[] data = fileData.toByteArray();
                    String fileName = fileMode.getFileName().replaceAll("/", "_").replaceAll(":", "");
                    String fname = ExecuteContainer.modelNamesMap.get(modelId+"");
                    if (fname != null && !"".equals(fname)) {
                        fileName = fname;
                    }
                    StreamUtils.buffersWriteFile(data, savePath, fileName + "." + fileMode.getFileExt());
                    System.out.println(savePath + fname + "." + fileMode.getFileExt() + "-- 下载成功！");
                } else {
                    logger.error("下载模型文件：" + modelId + ",失败");
                }
            } catch (Exception e) {
                logger.error("下载模型文件：" + modelId + ",异常 -- " + e.getMessage());
            }
        }

    }

}
