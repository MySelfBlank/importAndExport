package services.export;

import enums.ConstantDict;
import model.IDReset;
import onegis.common.utils.FileUtils;
import onegis.common.utils.JsonUtils;
import utils.PathUtil;

import java.util.List;
import java.util.Map;

public class ExportIDMap {
    /**
     * 导出重置后的ID
     */
    public static void writeNewIds(){
        List<IDReset> newIdList = ExecuteContainer.newIDList;
        FileUtils.writeContent(JsonUtils.objectToJson(newIdList),
                PathUtil.baseDir, ConstantDict.ID_INFO_NAME.getName(), false);
    }
}
