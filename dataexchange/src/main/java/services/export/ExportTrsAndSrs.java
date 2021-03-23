package services.export;

import enums.ConstantDict;
import model.ERefSystem;
import onegis.common.utils.FileUtils;
import onegis.common.utils.JsonUtils;

/**
 * 保存时间参考和空间参考
 */
public class ExportTrsAndSrs {

    /**
     * 保存时间参考
     *
     * @param baseDir 保存路径
     */
    public static void writeTrs(String baseDir) {
        ERefSystem eTrsSystem = new ERefSystem("1001", "onegis", "TIMECRS[\\\"Beidou Time\\\",TDATUM[\\\"Time origin\\\",TIMEORIGIN[2006-01-01T00:00:00Z]],CS[temporal,1],AXIS[\\\"time\\\",future],TIMEUNIT[\\\"week\\\",604800.0,1],AUTHORITY[\\\"ONEGIS\\\",1005],REMARK[\\\"BDT\\\",\\\"北斗时间\\\"]]");
        FileUtils.writeContent(JsonUtils.objectToJson(eTrsSystem), baseDir, ConstantDict.TRS_DATA_FILE_NAME.getName(), false);
    }

    /**
     * 保存空间参考
     *
     * @param baseDir 保存路径
     */
    public static void writeSrs(String baseDir) {
        ERefSystem eSrsSystem = new ERefSystem("4326", "epsg", "GEOGCS[\\\"GCS_WGS_1984\\\",DATUM[\\\"D_WGS_1984\\\",SPHEROID[\\\"WGS_1984\\\",6378137,298.257223563]],PRIMEM[\\\"Greenwich\\\",0],UNIT[\\\"Degree\\\",0.017453292519943295]],VERTCS[\\\"EGM2008_Geoid\\\",VDATUM[\\\"EGM2008_Geoid\\\"],PARAMETER[\\\"Vertical_Shift\\\",0.0],PARAMETER[\\\"Direction\\\",1.0],UNIT[\\\"Meter\\\",1.0]]");
        FileUtils.writeContent(JsonUtils.objectToJson(eSrsSystem), baseDir, ConstantDict.SRS_DATA_FILE_NAME.getName(), false);
    }
}
