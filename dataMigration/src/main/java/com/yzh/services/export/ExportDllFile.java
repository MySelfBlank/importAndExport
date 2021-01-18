package com.yzh.services.export;

import com.yzh.Index;
import com.yzh.dao.Mobj;
import com.yzh.utilts.FileTools;
import onegis.psde.dictionary.ModelLanguageEnum;
import onegis.psde.model.Model;
import onegis.psde.psdm.OType;

import java.util.ArrayList;
import java.util.List;

/**
 * 导出所有行为脚本
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2021/1/12 8:46
 */
public class ExportDllFile {

    public static void writeDllFiles(){
        List<OType> oTypeList = Index.oTypeList;
        // 获取所有需要下载的dll文件地址
        List<String> uriList = new ArrayList<>();
        for(OType oType:oTypeList) {
            if (oType == null || oType.getModels() == null) {
                continue;
            }
            List<Model> modelList = oType.getModels().getModels();
            if (modelList == null || modelList.isEmpty()) {
                continue;
            }
            for (Model model : modelList) {
                ModelLanguageEnum modelLanguage = model.getpLanguage();
                // 只要C++时才进行下载
                if (modelLanguage == null || !modelLanguage.equals(ModelLanguageEnum.C)||!modelLanguage.equals(ModelLanguageEnum.Java)) {
                    continue;
                }
                Mobj cModel = (Mobj) model.getMobj();
                if (cModel != null && cModel.getSource() != null && !cModel.getSource().equals("")) {
                    String uri = cModel.getSource();
                    uriList.add(uri);
                }
            }
        }
        //下载文件
        for (String uri : uriList) {
            FileTools.utileDownLoad(uri,"E:\\test\\"+Index.sDomain.getName()+"\\ModelFile");
        }
    }
}
