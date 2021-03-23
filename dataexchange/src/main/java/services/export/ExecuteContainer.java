package services.export;

import model.EDObject;
import model.ERelation;
import model.IDReset;
import onegis.common.utils.IdMakerUtils;
import onegis.psde.psdm.OType;
import onegis.psde.reference.SpatialReferenceSystem;
import onegis.psde.reference.TimeReferenceSystem;

import java.util.*;

public class ExecuteContainer {
    /**
     * 空间参考
     */
    public static Set<String> srsSystemIdList = new HashSet<>();
    public static List<SpatialReferenceSystem> srsSystemList = new ArrayList<>();
    /**
     * 时间参考
     */
    public static Set<String> trsSystemIdList = new HashSet<>();
    public static List<TimeReferenceSystem> trsSystemList = new ArrayList<>();
    /**
     * 对象类
     */
    public static List<OType> oTypeList = new ArrayList<>();
    public static Set<Long> oTypeIdSet = new HashSet<>();
    /**
     * 关联关系
     */
    public static List<ERelation> relationList = new ArrayList<>();
    /**
     * 数据对象
     */
    public static List<EDObject> dObjectList = new ArrayList<>();
    /**
     * 存储需要下载的模型ID
     */
    public static Set<Long> modelIds = new HashSet<>();
    public static Map<String, String> modelNamesMap = new HashMap<>();
    /**
     * 有动态数据的oid集合
     */
    public static List<Long> oidAllList = new ArrayList<>();


    public static Set<Long> DOTypeIdList = new HashSet<>();
    /**
     * 记录原始ID
     */
    public static Set<String> newIdSets = new HashSet<>();
    /**
     * 记录原始ID和新生成的ID的对应关系
     */
    public static List<IDReset> newIDList = new ArrayList<>();

    /**
     * 清空记录
     */
    public static void clear() {
        srsSystemIdList.clear();
        srsSystemList.clear();
        trsSystemIdList.clear();
        trsSystemList.clear();
        oTypeList.clear();
        oTypeIdSet.clear();
        relationList.clear();
        dObjectList.clear();
        modelIds.clear();
        modelNamesMap.clear();
        oidAllList.clear();
        newIdSets.clear();
        newIDList.clear();
    }

    /**
     * 添加空间参考
     *
     * @param srsSystem
     */
    public static void addSrsSystem(SpatialReferenceSystem srsSystem) {
        if (srsSystem == null) {
            return;
        }
        if (srsSystemList == null) {
            srsSystemList = new ArrayList<>();
        }

        String srsId = srsSystem.getId();
        if (srsId == null || srsId.isEmpty() || srsId.equals("")) {
            return;
        }
        if (srsSystemIdList.contains(srsId)) {
            return;
        }
        srsSystemList.add(srsSystem);
    }

    public static void addSrsSystemId(String srsSystemId) {
        if (srsSystemIdList == null) {
            srsSystemIdList = new HashSet<>();
        }
        srsSystemIdList.add(srsSystemId);
    }

    /**
     * 添加时间参考
     *
     * @param trsSystem
     */
    public static void addTrsSystem(TimeReferenceSystem trsSystem) {
        if (trsSystem == null) {
            return;
        }
        if (trsSystemList == null) {
            trsSystemList = new ArrayList<>();
        }
        String trsId = trsSystem.getId();
        if (trsId == null || trsId.isEmpty() || trsId.equals("")) {
            return;
        }
        if (trsSystemIdList.contains(trsSystem.getId())) {
            return;
        }
        trsSystemList.add(trsSystem);
    }

    public static void addTrsSystemId(String trsSystemId) {
        if (trsSystemIdList == null) {
            trsSystemIdList = new HashSet<>();
        }
        trsSystemIdList.add(trsSystemId);
    }

    /**
     * 添加关系
     *
     * @param relation
     */
    public static void addRelation(ERelation relation) {
        relationList.add(relation);
    }

    /**
     * 添加对象类
     *
     * @param oType
     */
    public static void addOType(OType oType) {
        if (oType == null || oType.getId() == null) {
            return;
        }
        if (oTypeList == null) {
            oTypeList = new ArrayList<>();
        }
        if (oTypeIdSet.contains(oType.getId())) {
            return;
        }
        oTypeList.add(oType);
    }

    public static void addOTypeId(Long otId) {
        if (oTypeIdSet == null) {
            oTypeIdSet = new HashSet<>();
        }

        oTypeIdSet.add(otId);
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

    /**
     * 生成新的ID
     *
     * @param id
     */
    public static void addNewId(String id, List<String> ids) {
        if (!newIdSets.contains(id)) {
            IDReset idReset = new IDReset();
            idReset.setId(id);
            Map<String, String> newIdMap = new HashMap<>();
            for (String oldId : ids) {
                long newID = new IdMakerUtils().nextId();
                newIdMap.put(oldId, newID + "");
            }
            idReset.setIdMpas(newIdMap);
            newIDList.add(idReset);
        }
    }


}
