package com.yzh.utilts;

import com.yzh.dao.ExecuteContainer;
import com.yzh.dao.exportModel.EDObject;
import com.yzh.dao.exportModel.ESObject;
import com.yzh.dao.exportModel.EVersion;
import com.yzh.utilts.action.BuildAttributeAction;
import com.yzh.utilts.action.BuildBaseAction;
import com.yzh.utilts.action.BuildFormAction;
import com.yzh.utilts.action.BuildRelationAction;
import onegis.common.utils.GeneralUtils;
import onegis.psde.psdm.SObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Yzh
 * @date 2021-01-09 16:31
 */
public class SObjectHandleUtil {
    public static void handleSObject (List<SObject> sObjects){
        Map<Long, List<Long>> dobjectMap = getEDObjectMap();
        List<ESObject> esObjects = dsSObjects2ex(sObjects, dobjectMap);
    }

    public static Map<Long, List<Long>> getEDObjectMap(){
        Map<Long, List<Long>> map = new HashMap<>(8);
        List<EDObject> dObjectList = ExecuteContainer.dObjectList;
        if (dObjectList.size() > 0){
            dObjectList.forEach(d -> {
                if (map.containsKey(d.getDataSource())){
                    map.get(d.getDataSource()).add(d.getId());
                }else {
                    map.put(d.getDataSource(), new ArrayList<Long>(){{add(d.getId());}});
                }
            });
        }
        return map;
    }

    /**
     * 空间对象列表转交换格式空间列表
     *
     * @param sObjects   空间对象列表
     * @param dobjectMap 空间对象对应产生的数据文件ID
     * @return List<ESObject>
     */
    public  static List<ESObject> dsSObjects2ex(List<SObject> sObjects, Map<Long, List<Long>> dobjectMap) {
        List<ESObject> esObjects = new ArrayList<>();
        if (!GeneralUtils.isNotEmpty(sObjects)) {
            System.out.println("空间对象列表null");
            return esObjects;
        }
        //分組
        Map<Long, List<SObject>> sobjectGroups = sObjects.stream().collect(Collectors.groupingBy(SObject::getId));
        // 获取所有对象的版本变化
        Map<Long, List<EVersion>> allEVersionMap = buildEVersions(sobjectGroups);
        for (Map.Entry<Long, List<SObject>> resMap : sobjectGroups.entrySet()) {
            Long oid = resMap.getKey();
            List<SObject> resList = resMap.getValue();
            ESObject esObject = buildESObject(resList, dobjectMap);
            if (esObject != null) {
                List<EVersion> eVersions = allEVersionMap.get(oid);
                if (GeneralUtils.isNotEmpty(eVersions)) {
                    esObject.setVersions(eVersions);
                }
                esObjects.add(esObject);
            }
        }
        return esObjects;
    }


    /**
     * 根据空间对象分组列表，构建一个增量版本的ESObject
     *
     * @param resList 空间对象列表
     * @return ESObject
     */
    public static ESObject buildESObject(List<SObject> resList, Map<Long, List<Long>> dobjectMap) {
//        if (!GeneralUtils.isNotEmpty(resList)) {
//            return null;
//        }
//        // 找出对象版本最小的那个，也就是当前列表中对象的起始版本
//        SObject startSObject = getOriginal(resList);
//        if (startSObject == null) {
//            return null;
//        }
//        SpatialReferenceSystem spatialReferenceSystem = startSObject.getSrs();
//        TimeReferenceSystem timeReferenceSystem = startSObject.getTrs();
//        if (spatialReferenceSystem != null) {
//            ExecuteContainer.addSrsSystem(startSObject.getSrs());
//            ExecuteContainer.addSrsSystemId(spatialReferenceSystem.getId());
//        }
//
//        if (timeReferenceSystem != null) {
//            ExecuteContainer.addTrsSystem(startSObject.getTrs());
//            ExecuteContainer.addTrsSystemId(timeReferenceSystem.getId());
//        }
//
//        OType oType = startSObject.getOtype();
//        if (oType != null && oType.getId() != null) {
//            ExecuteContainer.addOType(oType);
//            ExecuteContainer.addOTypeId(oType.getId());
//        }
//
        return buildESObject(resList, dobjectMap);
    }


    /**
     * 判断基本信息是否发生变化
     * @param allSObjects
     * @return
     */
    public static Map<Long, List<EVersion>> buildEVersions(Map<Long, List<SObject>> allSObjects) {
        Map<Long, List<EVersion>> result = new HashMap<>();
        for (Map.Entry<Long, List<SObject>> entry : allSObjects.entrySet()) {
            Long oid = entry.getKey();
            // 该对象所有版本,如果只有一个版本，则
            List<SObject> sObjects = entry.getValue();
            if (sObjects.size() <= 1) {
                continue;
            }
            List<EVersion> thisEVersions = buildBaseActions(sObjects);

            result.put(oid, thisEVersions);
        }
        return result;
    }


    /**
     * 构建单个对象的基本信息变化,目前只判断code是否发生变化
     * @param sObjects
     * @return
     */
    private static List<EVersion> buildBaseActions(List<SObject> sObjects) {
        List<EVersion> list = new ArrayList<>();
        SObject lastSObject = sObjects.get(0);
        for (int i=1; i<sObjects.size(); i++) {
            SObject thisSObject = sObjects.get(i);
            EVersion eVersion = new EVersion();
            eVersion.setVtime(thisSObject.getRealTime());
            // 计算基本信息的变化
            BuildBaseAction.setBaseAction(eVersion, lastSObject, thisSObject);
            // 计算属性信息的变化
            BuildAttributeAction.setAttributeAction(eVersion, lastSObject, thisSObject);
            // 计算组成结构的变化
            // 计算形态的变化
            BuildFormAction.setFormAction(eVersion, lastSObject, thisSObject);
            // 计算行为的变化
            // 计算关系的变化
            BuildRelationAction.setRelationAction(eVersion, lastSObject, thisSObject);

            list.add(eVersion);
            lastSObject = thisSObject;
        }
        return list;
    }

}
