package com.yzh.utilts;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yzh.api.MyApi;
import com.yzh.dao.EModel;
import com.yzh.dao.Mobj;
import com.yzh.userInfo.PathUtil;
import com.yzh.userInfo.UserInfo;
import onegis.psde.model.Model;
import onegis.psde.model.Models;
import onegis.psde.psdm.OType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import static com.yzh.utilts.tools.FileTools.exportFile;
import static com.yzh.utilts.tools.FileTools.forJsonList;
import static java.util.Objects.isNull;


/**
 * 行为的导出
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/22 9:44
 */
public class EModelUtil {
    
    static List<EModel> list=new ArrayList<>();
    
    /**
     * 获取行为id
     * @param modelsList
     */
    public static Set<Long> getModel(List<OType> modelsList){
        if (modelsList==null||modelsList.size()==0){
            return new HashSet<>();
        }
        Set<Long> ids = new HashSet<>();
        for (OType models : modelsList) {
            if (models!=null&&modelsList.size()!=0) {
                Models myModels = models.getModels();
                List<Model> models2 = myModels.getModels();
                if (models2==null){
                    continue;
                }
                for (Model model : models2) {
                    Long id = model.getId();
                    ids.add(id);
                }
            }
        }
        return ids;
    }

    /**
     * 通过行为id获取行为数据
     * @param modelsList
     */
    public static void getModelsFile(List<OType> modelsList) throws Exception {
        Set<Long> modelIds = getModel(modelsList);
        if(modelIds.size()==0){
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("token", UserInfo.token);
        param.put("ids", modelIds.toArray());
        param.put("DESCOrAsc", true);
        String relationStr = HttpUtil.get(MyApi.getModelById.getValue(), param);

        list.addAll(forJsonList(relationStr, EModel.class));

        String path = PathUtil.baseInfoDir + "\\test.models";
        exportFile(JSONUtil.parse(list), path, "Models");

    }

    //下载行为下的脚本文件
    public static void getEModelScriptFile() throws IOException {
        Set<String> eModelFile = getEModelScript();
        if (eModelFile.size()==0){
            return;
        }
        int i=1;
        for (String script : eModelFile) {
            //获取文件后缀
            String s = FileNameUtil.extName(script);
            URL url = new URL(MyApi.getModelScript.getValue() + "?srcPath=" + script);
            URLConnection con = url.openConnection();
            InputStream inputStream = con.getInputStream();
            int index;
            byte[] bytes = new byte[1024];
            File file = new File(PathUtil.baseInfoDirData+"\\test"+i+"."+s);
            //判断目录
            //判断文件是否存在
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            //将响应文件打印到本地
            FileOutputStream downloadFile = new FileOutputStream(file);
            while ((index = inputStream.read(bytes)) != -1) {
                downloadFile.write(bytes, 0, index);
                downloadFile.flush();
            }
            downloadFile.close();
            inputStream.close();
            i++;
        }
    }


    /**
     * 获取行为脚本
     * @return
     */
    public static Set<String> getEModelScript(){

        if (list==null||list.size()==0){
            return new HashSet<>();
        }
        Set<String> scriptSet = new HashSet<>();
        String [] languages = new String[]{"1","2"};
        for (EModel eModel : list) {
            if (!Arrays.asList(languages).contains(eModel.getpLanguage())){
                continue;
            }
            Mobj mobj = eModel.getMobj();
            if (!isNull(mobj)){
                if (mobj.getScript()!=null&&!mobj.getScript().equals("")) {
                    scriptSet.add(mobj.getScript());
                }
            }
        }
        return scriptSet;
    }


}
