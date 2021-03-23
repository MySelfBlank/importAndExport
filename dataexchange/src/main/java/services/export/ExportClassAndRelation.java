package services.export;

import enums.ConstantDict;
import model.EClasses;
import onegis.common.utils.FileUtils;
import onegis.common.utils.JsonUtils;
import onegis.psde.psdm.OType;
import utils.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 保存对象类和关系
 */
public class ExportClassAndRelation {

    private static Set<Long> classIDs = new HashSet<>();

    /**
     * 保存对象类和关系到本地
     */
    public static void writeClassAndRelation() {
        try {
            List<EClasses> classesList = new ArrayList<>();
            List<OType> oTypeList = ExecuteContainer.oTypeList;
            for (OType oType : oTypeList) {
                EClasses eClasses = dsClasses2EClass(oType);
                classesList.add(eClasses);
            }
            if (classesList != null) {
                FileUtils.writeContent(JsonUtils.objectToJson(classesList), PathUtil.baseDir, ConstantDict.CLASSES_DATA_FILE_NAME.getName(), false);
            }
            if (ExecuteContainer.relationList != null || ExecuteContainer.relationList.size() > 0) {
                FileUtils.writeContent(JsonUtils.objectToJson(ExecuteContainer.relationList), PathUtil.baseDir, ConstantDict.RELATION_DATA_FILE_NAME.getName(), false);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }


    }

    public static EClasses dsClasses2EClass(OType oType) throws Exception {
        if (oType == null) {
            return null;
        }
        EClasses eClasses = new EClasses();
        eClasses.setId(oType.getId());
        eClasses.setName(oType.getName());
        eClasses.setConnectors(ConnectorUtils.dsConnectors2EConnectors(oType.getConnectors(), classIDs));
        eClasses.setDesc(oType.getDes());
        eClasses.setFields(FieldUtils.dsFields2DataFile(oType.getFields()));
        eClasses.setForms(FormStyleUtils.dsFormStyles2EForms(oType.getFormStyles()));
        eClasses.setModels(ModelUtils.dsModels2EModels(oType.getModels()));
        eClasses.setSrs("epsg:4326");
        eClasses.setTrs("onegis:1001");
        return eClasses;
    }
}
