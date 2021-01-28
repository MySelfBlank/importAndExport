package com.yzh;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yzh.dao.EForm;
import com.yzh.dao.SDomainOutPutModel;
import com.yzh.services.export.ExportDllFile;
import com.yzh.userInfo.PathUtil;
import com.yzh.userInfo.UserInfo;
import com.yzh.utilts.*;
import com.yzh.utilts.tools.FileTools;
import onegis.psde.attribute.Attribute;
import onegis.psde.attribute.Field;
import onegis.psde.form.Form;
import onegis.psde.form.FormStyle;
import onegis.psde.psdm.OType;
import onegis.psde.psdm.SDomain;
import onegis.psde.psdm.SObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.hutool.core.util.ObjectUtil.*;
import static com.yzh.utilts.FieldUtils.handleOtypeFields;
import static com.yzh.utilts.tools.FileTools.*;
import static com.yzh.utilts.SDomainUtil.getSDomainById;

/**
 * @author Yzh
 * @create 2020-12-03 11:35
 */

public class Index {
    public static int pages;
    private static int pageNum = 1;
    private final static int pageSize = 10;
    public static SDomain sDomain;
    public static List<SObject> sObjectsList = new ArrayList<>();
    public static List<OType> oTypeList = new ArrayList<>();
    //
    private static final Logger logger = LoggerFactory.getLogger(Index.class);

    public static void main(String[] args) throws Exception {
        startVoid("C:\\Users\\Cai\\Desktop\\demo", "测试八个方面1223", 1341568728029622272L, null);
    }

    public static void startVoid(String path, String sDomainName, long SDomainId, String DOTypeName) throws Exception {
        login(UserInfo.username, UserInfo.password);

        //设置时空域Id
        UserInfo.domain = SDomainId;

        //设置路径
        PathUtil.setDir(sDomainName, path);
        logger.error("选择的时空域Id为=" + UserInfo.domain);
        logger.error("路径信息" + path);
        //导出时空域基本信息
        SDomainOutPutModel sDomainOutPutModel = new SDomainOutPutModel();
        SDomainOutPutModel sDomain = getSDomainById(sDomainOutPutModel, UserInfo.domain);
        JSONObject jsonObject = (JSONObject) JSONUtil.parse(sDomain);
        String localpath = PathUtil.baseInfoDir + "/test.sdomain";
        exportFile(jsonObject, localpath, sDomain.getName());

        //导出时空域下类模板
        OtypeUtilts.getOtype(DOTypeName);
        //导出时空域下的关系
        ERelationUtil.getRelation(sObjectsList);
        //导出时空域下的行为
        EModelUtil.getModelsFile(oTypeList);
        EModelUtil.getEModelScriptFile();
        //导出时空域下的行为类别
        EModelDefUtil.loadModelDefFile(oTypeList);
        //导出时空域下的空间参照
        ESrsUtil.getSrs(oTypeList);
        //导出时空域下的时间参照
        ETrsUtil.getTrs(oTypeList);
        //字段集合
        List<Field> fieldList = new ArrayList<>();
        //属性集合
        List<Attribute> attributeList = new ArrayList<>();
        //形态集合
        List<Form> formList = new ArrayList<>();
        List<FormStyle> FormStyleList = new ArrayList<>();
        //导出所有脚本文件
        ExportDllFile.writeDllFiles();
        for (SObject sObject : sObjectsList) {
            if (isEmpty(sObject) || isNull(sObject)) {
                continue;
            }
            //字段处理
            if (isNotEmpty(sObject.getAttributes().getAttributeList()) && isNotNull(sObject.getAttributes().getAttributeList())) {
                attributeList.addAll(sObject.getAttributes().getAttributeList());
            }
            //形态处理
            if (isNotEmpty(sObject.getForms().getForms()) && isNotNull(sObject.getForms().getForms())) {
                sObject.getForms().getForms().stream().sequential().collect(Collectors.toCollection(() -> formList));
            }
        }
        for (OType oType : oTypeList) {
            //类模板字段处理
            if (isNotEmpty(oType.getFields().getFields()) && isNotNull(oType.getFields().getFields())) {
                handleOtypeFields(oType.getFields().getFields());
            }
            //类模板形态处理
            if (isNotEmpty(oType.getFormStyles().getStyles()) && isNotNull(oType.getFormStyles().getStyles())) {
                oType.getFormStyles().getStyles().stream().sequential().collect(Collectors.toCollection(() -> FormStyleList));
            }
        }
        fieldList.addAll(FieldUtils.objectFieldsHandle2(attributeList));
        //导出时空域下所有使用的属性
        //List<Field> fieldList = FieldUtils.objectFieldsHandle(sObjectsList);
        FileTools.exportFile(JSONUtil.parse(fieldList), PathUtil.baseInfoDir + "/test.fields", "field");
        //导出时空域下所有使用的样式
        List<EForm> eFormList = FormUtils.dsForms2EForm(formList);
        FormStyleList.addAll(FormUtils.objectFromsHandle2(formList));
        List<FormStyle> formStyles = new ArrayList<>();
        formStyles.addAll(FormUtils.otpyeFromsHandle2(FormStyleList));
        //导出所有模型（与对象中下载的模型一致）
        //FormUtils.downLoadModel(ExecuteContainer.modelIds,PathUtil.baseInfoDirData);
        FileTools.exportFile(JSONUtil.parse(eFormList), PathUtil.baseInfoDir + "/test.forms", "form");
        FileTools.exportFile(JSONUtil.parse(formStyles), PathUtil.baseInfoDir + "/test.formStyles", "formStyle");
        //导出时空域下所有使用的形态
    }
}