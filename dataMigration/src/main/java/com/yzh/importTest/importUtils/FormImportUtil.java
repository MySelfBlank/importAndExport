package com.yzh.importTest.importUtils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yzh.api.MyApi;
import com.yzh.importTest.requestEntity.FormStyleEntity;
import com.yzh.userInfo.UserInfo;
import com.yzh.utilts.FileTools;
import onegis.psde.form.Form;
import onegis.psde.form.FormStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static com.yzh.importTest.importUtils.IdCache.formStylesOidAndNewId;
import static com.yzh.utilts.FileTools.login;

/**
 * @author Yzh
 * @create 2020-12-25 9:23
 * @details
 */
public class FormImportUtil {
    //日志工厂
    private static final Logger logger = LoggerFactory.getLogger(FormImportUtil.class);



    public static void formImportHandle(){
        //读取文件
        logger.debug("形态开始导入===========》读取形态文件");
        String formStylesStr = FileTools.readFile("E:\\test\\中原工_yzh\\test.forms");
        List<Form> forms = FileTools.jsonArray2List(formStylesStr, Form.class);
        //处理形态中的style 信息
        for (Form form : forms) {
            JSONArray jsonArray = JSONUtil.parseArray(form.getStyle());
            handleJsonArray(jsonArray);
            form.setStyle(jsonArray.toString());
        }
    }

    public static void handleJsonArray(JSONArray jsonArray){
        for (int i = 0; i < jsonArray.toArray().length; i++) {
            Long aLong = formStylesOidAndNewId.get(Long.parseLong(jsonArray.get(i).toString()));
            if(aLong!=null){
                //先移除之前的ID
                jsonArray.remove(i);
                //添加新的ID
                jsonArray.add(i,aLong.toString());
            }
        }
    }

    public static void formStyleImportHandle(){
        //读取文件
        logger.debug("形态样式开始导入===========》读取形态样式文件");
        String formStylesStr = FileTools.readFile("E:\\test\\测试八个方面1223\\test.formStyles");
        List<FormStyle> formStyles = FileTools.jsonArray2List(formStylesStr, FormStyle.class);
        //过滤掉使用的默认样式collect
        List<FormStyle> formStylesRemoveDef = formStyles.stream().filter(v -> !v.getName().contains("default_")).collect(Collectors.toList());
        for (FormStyle formStyle : formStylesRemoveDef) {
            FormStyleEntity formStyleEntity = new FormStyleEntity();
            formStyleEntity.setName(formStyle.getName());
            formStyleEntity.setDes(formStyle.getDes());
            formStyleEntity.setStyle(formStyle.getStyle().getValue());
            formStyleEntity.setData(formStyle.getData());
            formStyleEntity.setType(formStyle.getType().getValue());

            String response = HttpUtil.post(MyApi.insertFormStyle.getValue().replace("@token", UserInfo.token), JSONUtil.parse(formStyleEntity).toString());
            //错误判断
            if (FileTools.judgeImportState(response)){
                logger.error("id:"+formStyle.getId()+"的形态样式导入失败");
                continue;
            }
            //处理 response
            JSONObject array = FileTools.formatData(response);
            formStylesOidAndNewId.put(formStyle.getId(),array.getLong("id"));
            logger.info("id" +formStyle.getId() + "导入完毕新Id为："+array.getLong("id"));
        }
    }

    public static void main(String[] args) {
        login("ceshi@yzh.com", "123456");
        formStyleImportHandle();
        JSON parse = JSONUtil.parse(formStylesOidAndNewId);
        FileTools.exportFile(parse,"E:\\test\\测试八个方面1223\\formId.text","formId.text");
    }
}
