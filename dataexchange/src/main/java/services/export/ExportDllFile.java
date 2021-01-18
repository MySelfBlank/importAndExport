package services.export;

import onegis.psde.dictionary.ModelLanguageEnum;
import onegis.psde.model.Mobj;
import onegis.psde.model.Model;
import onegis.psde.psdm.OType;
import onegis.psde.psdm.SObject;
import services.RequestServices;
import services.impl.RequestServicesImpl;
import utils.PathUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ExportDllFile {

    private RequestServices requestServices= new RequestServicesImpl();

    public void writeDllFiles(List<SObject> sObjectList){
        Set<Long> otIds = sObjectList.stream().filter(sObject -> sObject.getOtype() != null)
                .map(sObject -> sObject.getOtype().getId()).collect(Collectors.toSet());
        if(otIds.isEmpty()){
            return;
        }
        try{
            List<OType> oTypeList = requestServices.getOtypes(otIds);
            // 获取所有需要下载的dll文件地址
            List<String> uriList = new ArrayList<>();
            for(OType oType:oTypeList){
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
                    if (modelLanguage == null || !modelLanguage.equals(ModelLanguageEnum.C)) {
                        continue;
                    }
                    try {
                        Mobj cModel = model.getMobj();
                        if (cModel != null && cModel.getSource() != null && !cModel.getSource().equals("")) {
                            String uri = cModel.getSource();
                            uriList.add(uri);
                        }
                    } catch (Exception e) {}
                }
            }
            // 下载文件
            for (String uri : uriList) {
                try {
                    requestServices.downLoadDll(uri, PathUtil.baseDirData);
                } catch (Exception e) { }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


    }
}
