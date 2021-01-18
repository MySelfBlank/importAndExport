package com.yzh.utilts;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.yzh.api.MyApi;
import com.yzh.dao.ExecuteContainer;
import com.yzh.dao.exportModel.EDObject;
import onegis.psde.psdm.DObject;
import onegis.psde.psdm.SObject;

import java.util.*;

import static cn.hutool.core.util.ObjectUtil.isEmpty;
import static cn.hutool.core.util.ObjectUtil.isNull;
import static com.yzh.utilts.FileTools.formatData;

/**
 * @author Yzh
 * @date 2021-01-09 16:20
 */
public class EDObjectUTil {
    //获取DObject信息
    public static void getDObjectList (List<SObject> sObjectList){
        if(isEmpty(sObjectList)||isNull(sObjectList)){
            return;
        }
        Set<Long> dobjectFromIds = new HashSet<>();
        sObjectList.forEach(sObject -> {
            Long from = sObject.getId();
            if (from != null && from != 0) {
                dobjectFromIds.add(from);
            }
        });
        //根据对象ID查找Dobject
        if(dobjectFromIds.size()>0){
            List<Long> dobjectIds = new ArrayList<>(dobjectFromIds);
            List<EDObject> edObjects = new ArrayList<>();
            List<List<Long>> partition = Lists.partition(dobjectIds, 10); //将List进行切分，每个大小为10
            try{
                List<DObject> dObjects = new ArrayList<>();
                for (List<Long> list : partition) {
                    dObjects.addAll(queryDObject(list));
                }
                edObjects = dsDobjectsToEDObjects(dObjects);
            }catch (Exception e){
                e.printStackTrace();
            }
            ExecuteContainer.addDObject(edObjects);
        }
    }

    //查询请求
    public static List<DObject> queryDObject (List<Long> dobjectIds){
        Map<String,Object> params = new HashMap<>();
        params.put("fromIds",dobjectIds.toArray());
        String respondStr = HttpUtil.get(MyApi.getDObject.getValue(), params);
        JSONObject object = formatData(respondStr);
        List<DObject> dObjects= JSONUtil.toList(object.getStr("list"),DObject.class);
        return dObjects;
    }

    //将查询到Dobjects转换为EDObjects
    public static List<EDObject> dsDobjectsToEDObjects (List<DObject> dObjects) throws Exception {
        if (isEmpty(dObjects)||isNull(dObjects)) {
            return null;
        }
        List<EDObject> edObjects = new ArrayList<>(dObjects.size());
        for(DObject dObject : dObjects){
            if (dObject.getDataRef() != null && !"".equals(dObject.getDataRef())){
                /**下载模型文件*/
                // 是否需要重写一个方法？？
//                requestServices.downLoadDll(dObject.getDataRef(), PathUtil.baseDirData);
                FileTools.utileDownLoad(dObject.getDataRef(),null);
            }
            EDObject edObject = dsDobjectToEDObject(dObject);
            edObjects.add(edObject);
        }

        return edObjects;
    }

    //将查询到Dobject转换为EDObject
    private static EDObject dsDobjectToEDObject(DObject dObject) {
        EDObject edObject = new EDObject();
        edObject.setId(dObject.getId());
        edObject.setName(dObject.getName());
        if (dObject.getOtype() != null) {
            //edObject.setdType(dObject.getOtype().getId());
            edObject.setdType(dObject.getOtype().getName());
        }
        edObject.setDataSource(dObject.getFrom());
        String dataRef = dObject.getDataRef();
        String fileName = dataRef.substring(dataRef.lastIndexOf("_") + 1);
        edObject.setData(fileName);
        return edObject;
    }
}
