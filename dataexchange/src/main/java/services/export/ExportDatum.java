package services.export;

import enums.ConstantDict;
import onegis.common.utils.FileUtils;

/**
 * 导出数据字典
 */
public class ExportDatum {

    public static void writeDatum(String path) {
        FileUtils.writeContent(datum, path, ConstantDict.DATUM_DATA_FILE_NAME.getName(), false);
    }


    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    //字典信息
    private static String datum = "[{\n" +
            "\t\"name\": \"valueType\",\n" +
            "\t\"desc\": \"数据类型\",\n" +
            "\t\"content\": [{\n" +
            "\t\t\"key\": \"short\",\n" +
            "\t\t\"name\": \"16位整型\",\n" +
            "\t\t\"value\": 1\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"key\": \"int\",\n" +
            "\t\t\"name\": \"32位整型\",\n" +
            "\t\t\"value\": 7\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"key\": \"long\",\n" +
            "\t\t\"name\": \"64位整型\",\n" +
            "\t\t\"value\": 2\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"key\": \"float\",\n" +
            "\t\t\"name\": \"32位浮点型\",\n" +
            "\t\t\"value\": 3\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"key\": \"double\",\n" +
            "\t\t\"name\": \"64位浮点型\",\n" +
            "\t\t\"value\": 4\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"key\": \"text\",\n" +
            "\t\t\"name\": \"字符型\",\n" +
            "\t\t\"value\": 5\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"key\": \"datetime\",\n" +
            "\t\t\"name\": \"日期型\",\n" +
            "\t\t\"value\": 6\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"key\": \"boolean\",\n" +
            "\t\t\"name\": \"布尔型\",\n" +
            "\t\t\"value\": 8\n" +
            "\t}]\n" +
            "},\n" +
            "{\n" +
            "\t\"name\": \"valueDomain\",\n" +
            "\t\"desc\": \"值域\",\n" +
            "\t\"content\": [{\n" +
            "\t\t\"key\": \"list\",\n" +
            "\t\t\"name\": \"列表\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"key\": \"range\",\n" +
            "\t\t\"name\": \"范围\"\n" +
            "\t}]\n" +
            "},\n" +
            "{\n" +
            "\t\"name\": \"spatialDataType\",\n" +
            "\t\"desc\": \"空间数据类型\",\n" +
            "\t\"content\": [{\n" +
            "\t\t\"key\": \"point\",\n" +
            "\t\t\"name\": \"点\",\n" +
            "\t\t\"value\": 21\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"key\": \"linestring\",\n" +
            "\t\t\"name\": \"线\",\n" +
            "\t\t\"value\": 22\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"key\": \"polygon\",\n" +
            "\t\t\"name\": \"面\",\n" +
            "\t\t\"value\": 23\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"key\": \"dem\",\n" +
            "\t\t\"name\": \"规则格网\",\n" +
            "\t\t\"value\": 32\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"key\": \"isohypse\",\n" +
            "\t\t\"name\": \"等高线\",\n" +
            "\t\t\"value\": 31\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"key\": \"tin\",\n" +
            "\t\t\"name\": \"不规则三角网\",\n" +
            "\t\t\"value\": 33\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"key\": \"model\",\n" +
            "\t\t\"name\": \"三维模型\",\n" +
            "\t\t\"value\": 50\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"key\": \"ellipsoid\",\n" +
            "\t\t\"name\": \"椭球\",\n" +
            "\t\t\"value\": 61\n" +
            "\t}]\n" +
            "},\n" +
            "{\n" +
            "\t\"name\": \"relationType\",\n" +
            "\t\"desc\": \"对象类关系\",\n" +
            "\t\"content\": [{\n" +
            "\t\t\"key\": \"realization\",\n" +
            "\t\t\"name\": \"继承\",\n" +
            "\t\t\"value\": 2\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"key\": \"aggregation\",\n" +
            "\t\t\"name\": \"聚合\",\n" +
            "\t\t\"value\": 4\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"key\": \"composition\",\n" +
            "\t\t\"name\": \"组合\",\n" +
            "\t\t\"value\": 8\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"key\": \"dependency\",\n" +
            "\t\t\"name\": \"依赖\",\n" +
            "\t\t\"value\": 16\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"key\": \"association\",\n" +
            "\t\t\"name\": \"关联\",\n" +
            "\t\t\"value\": 32\n" +
            "\t}]\n" +
            "},\n" +
            "{\n" +
            "\t\"name\": \"relationMap\",\n" +
            "\t\"desc\": \"对象类关系映射\",\n" +
            "\t\"content\": [{\n" +
            "\t\t\"key\": \"onetoone\",\n" +
            "\t\t\"name\": \"一对一\",\n" +
            "\t\t\"value\": 1\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"key\": \"onetomany\",\n" +
            "\t\t\"name\": \"一对多\",\n" +
            "\t\t\"value\": 2\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"key\": \"manytoone\",\n" +
            "\t\t\"name\": \"多对一\",\n" +
            "\t\t\"value\": 3\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"key\": \"manytomany\",\n" +
            "\t\t\"name\": \"多对多\",\n" +
            "\t\t\"value\": 4\n" +
            "\t}]\n" +
            "},\n" +
            "{\n" +
            "\t\"name\": \"actionOperationType\",\n" +
            "\t\"desc\": \"动作操作\",\n" +
            "\t\"content\": [{\n" +
            "\t\t\"key\": \"ADDING\",\n" +
            "\t\t\"name\": \"增加\",\n" +
            "\t\t\"value\": 1\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"key\": \"DELETE\",\n" +
            "\t\t\"name\": \"删除\",\n" +
            "\t\t\"value\": 2\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"key\": \"MODIFY\",\n" +
            "\t\t\"name\": \"修改\",\n" +
            "\t\t\"value\": 4\n" +
            "\t}]\n" +
            "},\n" +
            "{\n" +
            "\t\"name\": \"objectOperationType\",\n" +
            "\t\"desc\": \"对象操作\",\n" +
            "\t\"content\": [{\n" +
            "\t\t\"key\": \"BASE\",\n" +
            "\t\t\"name\": \"基本信息\",\n" +
            "\t\t\"value\": 32\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"key\": \"ATTRIBUTE\",\n" +
            "\t\t\"name\": \"属性信息\",\n" +
            "\t\t\"value\": 64\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"key\": \"FORM\",\n" +
            "\t\t\"name\": \"空间形态\",\n" +
            "\t\t\"value\": 128\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"key\": \"RELATION\",\n" +
            "\t\t\"name\": \"关联关系\",\n" +
            "\t\t\"value\": 256\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"key\": \"COMPOSE\",\n" +
            "\t\t\"name\": \"组成结构\",\n" +
            "\t\t\"value\": 512\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"key\": \"MODEL\",\n" +
            "\t\t\"name\": \"行为能力\",\n" +
            "\t\t\"value\": 1024\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"key\": \"POSITION\",\n" +
            "\t\t\"name\": \"位置\",\n" +
            "\t\t\"value\": 2048\n" +
            "\t}]\n" +
            "}]";
}
