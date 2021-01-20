package com.yzh;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yzh.dao.EForm;
import com.yzh.dao.ExecuteContainer;
import com.yzh.dao.SDomainOutPutModel;
import com.yzh.services.export.ExportDllFile;
import com.yzh.userInfo.PathUtil;
import com.yzh.userInfo.UserInfo;
import com.yzh.utilts.*;
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
import java.util.Scanner;
import java.util.stream.Collectors;

import static cn.hutool.core.util.ObjectUtil.isEmpty;
import static cn.hutool.core.util.ObjectUtil.isNull;
import static com.yzh.utilts.FileTools.*;
import static com.yzh.utilts.SDomainPagesTools.getPages;
import static com.yzh.utilts.SDomainUtil.getSDomain;
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
        startVoid("C:\\Users\\Cai\\Desktop\\demo","测试八个方面1223",1341568728029622272L);
        logger.debug("开始运行");
        //用户Token
        Scanner input = new Scanner(System.in);
        System.out.println("请选择开发环境：[1]：prod  [2]：dev");
        EnvironmentSelectTool.selectEnv();
        System.out.println("请输入您的账号和密码");
        login("asiayu01@163.com", "yu1306730458");
//        login(input.nextLine().trim(), input.nextLine().trim());

        //get请求 并返回请求结果（时空域信息）
        logger.debug("开始获取时空域");
        if (UserInfo.token == null) {
            logger.error("用户登录失败，程序结束");
            return;
        }
        //分页设置
        List<SDomain> sDomains = new ArrayList<>();
        String sDomainName;

        do {
            System.out.println("请输入时空域名称");
            //获取输入
            sDomainName = input.nextLine();
            getPages(pageNum, pageSize, sDomainName, input, sDomains);
        } while (sDomains.size() == 0);

        int page = pages;
        String stop = "no";
        while (stop.equals("no")) {
            System.out.println("是否选择当前页的时空域[yes/no]");
            if (input.next().equals("yes")) {
                break;
            }
            //采用Switch Catch 控制上下页
            System.out.println("上一页 ：b 下一页 ：n");
            switch (input.next()) {
                case "n":
                    //是否选择下一页
                    if (page > pageNum) {
                        //重新做分页请求
                        pageNum++;
                        getPages(pageNum, pageSize, sDomainName, input, sDomains);
                    }
                    break;
                case "b":
                    //是否选择上一页
                    if (pageNum > 1 && pageNum <= page) {
                        //重新做分页请求
                        pageNum--;
                        getPages(pageNum, pageSize, sDomainName, input, sDomains);
                    } else {
                        System.out.println();
                    }
                    break;
            }

        }


        boolean flag = false;
        Integer i;
        do {
            try {
                System.out.println("请选择时空域前的序号");
                i = input.nextInt() - 1;
                while (i > sDomains.size()) {
                    System.out.println("输入无效请重新输入：");
                    i = input.nextInt() - 1;
                }
                UserInfo.domain = sDomains.get(i).getId();
                sDomain = sDomains.get(i);


                SDomainOutPutModel sDomainOutPutModel = new SDomainOutPutModel();
                SDomainOutPutModel sDomain = getSDomain(sDomainOutPutModel, Index.sDomain);
                JSONObject jsonObject = (JSONObject) JSONUtil.parse(sDomain);
                String path = "E:/test/" + sDomain.getName() + "/test.sdomain";
                exportFile(jsonObject, path,sDomain.getName());
                flag = false;
            } catch (Exception e) {
                System.out.println("输入格式错误");
                e.getMessage();
                flag = true;
                //需要重新建立一个 Scanner 不然会陷入死循环
                input = new Scanner(System.in);
            }
        } while (flag);


        logger.debug("选择的时空域Id为=" + UserInfo.domain);
        //导出时空域基本信息

        //导出时空域下类模板
        OtypeUtilts.getOtype();
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
        //导出所有脚本文件
        ExportDllFile.writeDllFiles();
        for (SObject sObject : sObjectsList) {
            if (isEmpty(sObject) || isNull(sObject)) {
                continue;
            }
            //字段处理
            if (isEmpty(sObject.getAttributes().getAttributeList()) || isEmpty(sObject.getAttributes().getAttributeList())) {
                continue;
            } else {
                attributeList.addAll(sObject.getAttributes().getAttributeList());
            }
            //形态处理
            if (isEmpty(sObject.getForms().getForms()) || isNull(sObject.getForms().getForms())) {
                continue;
            } else {
                sObject.getForms().getForms().stream().sequential().collect(Collectors.toCollection(() -> formList));
            }
        }
        fieldList.addAll(FieldUtils.objectFieldsHandle2(attributeList));
        //导出时空域下所有使用的属性
        //List<Field> fieldList = FieldUtils.objectFieldsHandle(sObjectsList);
        FileTools.exportFile(JSONUtil.parse(fieldList), "E:/test/" + sDomain.getName() + "/test.fields","field");
        //导出时空域下所有使用的样式
        List<EForm> eFormList = FormUtils.dsForms2EForm(formList);
        List<FormStyle> formStyles = FormUtils.objectFromsHandle2(formList);
        //导出所有模型
        FormUtils.downLoadModel(ExecuteContainer.modelIds,"E:\\test\\测试八个方面1223\\ModelFile");
        FileTools.exportFile(JSONUtil.parse(eFormList), "E:/test/" + sDomain.getName() + "/test.forms","form");
        FileTools.exportFile(JSONUtil.parse(formStyles), "E:/test/" + sDomain.getName() + "/test.formStyles","formStyle");
        //导出时空域下所有使用的形态
        //退出账号
        logout();
    }

    public static void startVoid(String path,String sDomainName , long SDomainId) throws Exception {
        login("asiayu01@163.com", "yu1306730458");

        //设置时空域Id
        UserInfo.domain = SDomainId;

        //设置路径
        PathUtil.setDir(sDomainName,path);
        logger.error("选择的时空域Id为=" + UserInfo.domain);
        logger.error("路径信息"+path);
        //导出时空域基本信息
        SDomainOutPutModel sDomainOutPutModel = new SDomainOutPutModel();
        SDomainOutPutModel sDomain = getSDomainById(sDomainOutPutModel,UserInfo.domain);
        JSONObject jsonObject = (JSONObject) JSONUtil.parse(sDomain);
        String localpath = PathUtil.baseInfoDir+ "/test.sdomain";
        exportFile(jsonObject, localpath,sDomain.getName());

        //导出时空域下类模板
        OtypeUtilts.getOtype();
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
            if (isEmpty(sObject.getAttributes().getAttributeList()) || isEmpty(sObject.getAttributes().getAttributeList())) {
                continue;
            } else {
                attributeList.addAll(sObject.getAttributes().getAttributeList());
            }
            //形态处理
            if (isEmpty(sObject.getForms().getForms()) || isNull(sObject.getForms().getForms())) {
                continue;
            } else {
                sObject.getForms().getForms().stream().sequential().collect(Collectors.toCollection(() -> formList));
            }
        }
        for (OType oType : oTypeList) {
            if (isEmpty(oType.getFormStyles().getStyles()) || isNull(oType.getFormStyles().getStyles())) {
                continue;
            } else {
                oType.getFormStyles().getStyles().stream().sequential().collect(Collectors.toCollection(() -> FormStyleList));
            }
        }
        fieldList.addAll(FieldUtils.objectFieldsHandle2(attributeList));
        //导出时空域下所有使用的属性
        //List<Field> fieldList = FieldUtils.objectFieldsHandle(sObjectsList);
        FileTools.exportFile(JSONUtil.parse(fieldList), PathUtil.baseInfoDir+ "/test.fields","field");
        //导出时空域下所有使用的样式
        List<EForm> eFormList = FormUtils.dsForms2EForm(formList);
        FormStyleList.addAll(FormUtils.objectFromsHandle2(formList));
        List<FormStyle> formStyles = new ArrayList<>();
        formStyles.addAll(FormUtils.otpyeFromsHandle2(FormStyleList));
        //导出所有模型（与对象中下载的模型一致）
        //FormUtils.downLoadModel(ExecuteContainer.modelIds,PathUtil.baseInfoDirData);
        FileTools.exportFile(JSONUtil.parse(eFormList), PathUtil.baseInfoDir+ "/test.forms","form");
        FileTools.exportFile(JSONUtil.parse(formStyles), PathUtil.baseInfoDir + "/test.formStyles","formStyle");
        //导出时空域下所有使用的形态
    }
}

/*阿里的fastjson格式化方案*/
//com.alibaba.fastjson.JSONObject parse = (com.alibaba.fastjson.JSONObject) com.alibaba.fastjson.JSONObject.parse(objectJsonStr);
//com.alibaba.fastjson.JSONObject data = (com.alibaba.fastjson.JSONObject) parse.get("data");