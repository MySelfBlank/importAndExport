package com.yzh.importTask.importUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yzh
 * @create 2021-01-12 11:08
 * @details
 */
public class IdCache {
    //缓存全局可用
    public static Map<Long, Long> fieldOldIdAndNewIdCache = new HashMap<>();

    public static Map<Long, Long> formStylesOidAndNewId = new HashMap<>();

    public static Map<Long, Long> relationNewIdAndOldId = new HashMap<>();

    //类模板新旧ID缓存
    public static Map<Long, Long> otypeNewIdAndOldId = new HashMap<>();

    public static Map<Long, Long> modelNewIdAndOldId = new HashMap<>();

    public static Map<Long, Long> modelDefNewIdAndOldId = new HashMap<>();


    public static void allClear() {
        fieldOldIdAndNewIdCache.clear();
        formStylesOidAndNewId.clear();
        relationNewIdAndOldId.clear();
        otypeNewIdAndOldId.clear();
        modelNewIdAndOldId.clear();
        modelDefNewIdAndOldId.clear();
    }
}


