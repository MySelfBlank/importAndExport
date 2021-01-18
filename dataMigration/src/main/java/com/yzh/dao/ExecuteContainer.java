package com.yzh.dao;

import com.yzh.dao.exportModel.EDObject;

import java.util.*;

/**
 * @author Yzh
 * @date 2021-01-09 16:26
 * 数据容器
 */
public class ExecuteContainer {
    /**
     * 数据对象
     */
    public static List<EDObject> dObjectList = new ArrayList<>();
    /**
     * 存储需要下载的模型ID
     */
    public static Set<Long> modelIds = new HashSet<>();
    public static Map<String, String> modelNamesMap = new HashMap<>();

    public static void clear(){
        dObjectList.clear();
    }

    public static void addDObject(List<EDObject> edObjects) {
        if (edObjects != null && !edObjects.isEmpty()) {
            dObjectList.addAll(edObjects);
        }
    }
    public static void addModelId(Long modelId) {
        modelIds.add(modelId);
    }

    public static void addModelName(String modelId, String name) {

        modelNamesMap.put(modelId, name);
    }
}
