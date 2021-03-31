package com.yzh.dao;

import com.yzh.dao.exportModel.EDObject;
import onegis.psde.attribute.Field;

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

    public static Set<Long> otypeFieldIds = new HashSet<>();

//    public static Set<Long> modelIds = new HashSet<>();
    public static List<EModel> eModelList = new ArrayList<>();

    public static List<Field> fieldList = new ArrayList<>();

    public static Set<Long> RelationIds = new HashSet<>();

    public static void clear() {
        dObjectList.clear();
        fieldList.clear();
        otypeFieldIds.clear();
        eModelList.clear();
        modelIds.clear();
        RelationIds.clear();
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
