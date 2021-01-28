package com.yzh.importTask.importUtils;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.yzh.userInfo.PathUtil;
import com.yzh.userInfo.UserInfo;
import com.yzh.utilts.tools.FileTools;

import static com.yzh.importTask.importUtils.FormImportUtil.formStyleImportHandle;
import static com.yzh.importTask.importUtils.IdCache.*;
import static com.yzh.utilts.tools.FileTools.login;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2021/1/19 9:07
 */
public class ImportBaseInfo {
    public static void main(String[] args) throws Exception {



        orderImport("C:\\Users\\bluethink\\Desktop\\导出数据\\测试八个方面1223");
    }

    /**
     * 按照顺序导入
     * @param path
     * @throws Exception
     */
    public static void orderImport(String path) throws Exception{
        login(UserInfo.username, UserInfo.password);
        //处理路径信息
        int i = path.indexOf("(");
        PathUtil.baseInfoDir=path.substring(0,i);
        //对字段的导入
        FieldImportUtil.fieldImport(PathUtil.baseInfoDir+"\\test.fields");

        //导出字段ID到本地
        JSON parse = JSONUtil.parse(fieldOldIdAndNewIdCache);
        FileTools.exportFile(parse, PathUtil.baseInfoDir+"\\fieldId.text","fieldId.text");

        //形态样式
        formStyleImportHandle();
        JSON styleParse = JSONUtil.parse(formStylesOidAndNewId);
        FileTools.exportFile(styleParse,PathUtil.baseInfoDir+"\\formId.text","formId.text");

        //时空域的导入
        //SDomainImportUtil.importSDomain(PathUtil.baseInfoDir+"\\test.sdomain");

        //行为类别的导入
        ModelImportUtil.modelDefImportHandle(PathUtil.baseInfoDir+"\\test.modelDef",PathUtil.baseInfoDir+"\\fieldId.text");
        JSON parseDef = JSONUtil.parse(IdCache.modelDefNewIdAndOldId);
        FileTools.exportFile(parseDef,PathUtil.baseInfoDir+"\\modelDefId.text","modelDefId.text");

        //行为的导入
        ModelImportUtil.modelImportHandle(PathUtil.baseInfoDir+"\\test.models",PathUtil.baseInfoDir+"\\ModelFile",PathUtil.baseInfoDir+"\\modelDefId.text");
        JSON parseModel = JSONUtil.parse(modelNewIdAndOldId);
        FileTools.exportFile(parseModel,PathUtil.baseInfoDir+"\\modelId.text","modelId.text");

        //关系的导入
        RelationImportUtil.upLoadRelation(PathUtil.baseInfoDir+"\\test.relation",PathUtil.baseInfoDir+"\\fieldId.text",PathUtil.baseInfoDir+"\\modelId.text");
        JSON parseRelation = JSONUtil.parse(relationNewIdAndOldId);
        FileTools.exportFile(parseRelation,PathUtil.baseInfoDir+"\\relationId.text","relationId.text");

        //类模板的导入
        OTypeImportUtil.importOTpye();
        JSON parseOType = JSONUtil.parse(otypeNewIdAndOldId);
        FileTools.exportFile(parseOType,PathUtil.baseInfoDir+"\\otpyeId.text","otpyeId.text");

        //清空Id缓存
        IdCache.allClear();

        System.out.println("基本信息导入完毕~");
    }

    /**
     * 去除括号中间的内容
     * @param context
     * @param left
     * @param right
     * @return
     */
    private static String clearBracket(String context, char left, char right) {
        int head = context.indexOf(left);
        if (head == -1) {
            return context;
        } else {
            int next = head + 1;
            int count = 1;
            do {
                if (context.charAt(next) == left) {
                    count++;
                } else if (context.charAt(next) == right) {
                    count--;
                }
                next++;
                if (count == 0) {
                    String temp = context.substring(head, next);
                    context = context.replace(temp, "");
                    head = context.indexOf(left);
                    next = head + 1;
                    count = 1;
                }
            } while (head != -1);
        }
        return context;
    }
}
