package com.yzh.utilts;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yzh.api.MyApi;
import com.yzh.dao.EModelDef;
import com.yzh.userInfo.UserInfo;
import onegis.psde.model.Model;
import onegis.psde.model.ModelDef;
import onegis.psde.model.Models;
import onegis.psde.psdm.OType;

import java.util.*;

import static cn.hutool.core.util.ObjectUtil.isEmpty;
import static cn.hutool.core.util.ObjectUtil.isNull;
import static com.yzh.Index.sDomain;
import static com.yzh.utilts.FileTools.exportFile;
import static com.yzh.utilts.FileTools.forJsonList;

/**
 * 行为类的导出
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/22 15:31
 */
public class EModelDefUtil {
    /**
     * 获取行为类别的id
     * @param oTypeList
     * @return
     */
    public static Set<Long> getModelDefId(List<OType> oTypeList) {
        if (oTypeList == null || oTypeList.size() == 0) {
            return new HashSet<>();
        }
        //处理models数据
        Set<Long> ids = new HashSet<>();
        for (OType models : oTypeList) {
            if (models != null && oTypeList.size() > 0) {
                Models myModels = models.getModels();
                ids.addAll(getModelIds(myModels.getModels()));
            }
        }
        return ids;
    }

    /**
     *
     * @param models
     * @return
     */
    public static Set<Long> getModelIds(List<Model> models) {
        Set<Long> ids = new HashSet<>();
        if (isNull(models) || isEmpty(models)) {
            return new HashSet<>();
        }
        for (Model model : models) {
            if (model.getMdef() != null) {
                ModelDef mdef = model.getMdef();
                if (mdef == null) {
                    continue;
                }
                Long id = mdef.getId();
                ids.add(id);
            }
        }
        return ids;
    }



    /**
     * 行为类别文件下载
     * @param oTypeList
     */
    public static void loadModelDefFile( List<OType> oTypeList) throws Exception {
        Set<Long> ModelDefIds = getModelDefId(oTypeList);
        if (ModelDefIds.size()==0){
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("token", UserInfo.token);
        param.put("ids", ModelDefIds.toArray());
        param.put("DESCOrAsc","true");
        String relationStr = HttpUtil.get(MyApi.getModelDefById.getValue(), param);

        List<ModelDef> list = forJsonList(relationStr,ModelDef.class);
        List<EModelDef> eModelDefs =new ArrayList<>();
        for (ModelDef modelDef : list) {
            if (modelDef!=null){
                EModelDef eModelDef = OtypeUtilts.handleModel(modelDef);
                eModelDefs.add(eModelDef);
            }
        }
        String path = "E:\\test\\" + sDomain.getName() + "\\test.modelDef";
        exportFile(JSONUtil.parse(eModelDefs), path,"modelDef");
    }
        
    }

