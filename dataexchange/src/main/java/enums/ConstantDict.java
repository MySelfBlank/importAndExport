package enums;

/**
 * 常量字典
 */
public enum ConstantDict {

    /**根目录*/
    RC_SOBJECT_DATA("datastore.datas"),
    /**空间参照 -- 交换数据格式转出文件名*/
    SRS_DATA_FILE_NAME("test.srs"),
    /**时间参照 -- 交换数据格式转出文件名*/
    TRS_DATA_FILE_NAME("test.trs"),
    /**时空域 -- 交换数据格式转出文件名*/
    SDOMAIN_DATA_FILE_NAME("test.domain"),
    /**类模板 -- 交换数据格式转出文件名*/
     CLASSES_DATA_FILE_NAME("test.classes"),
    /**数据对象 -- 交换数据格式转出文件名*/
    DOBJECT_DATA_FILE_NAME("test.dobject"),
    /**关系 -- 交换数据格式转出文件名*/
    RELATION_DATA_FILE_NAME("test.relation"),
    /**关系数据 -- 交换数据格式转出文件名*/
    GRAPH_DATA_FILE_NAME("test.network"),
    /**对象 -- 交换数据格式转出文件名*/
    SOBJECT_DATA_FILE_NAME("test.object"),
    /**元信息 -- 交换数据格式转出文件名*/
    META_DATA_FILE_NAME("test.metadata"),
    /**数据字典 -- 交换数据格式转出文件名*/
    DATUM_DATA_FILE_NAME("test.datum"),
    /**ID -- 重置的ID信息*/
    ID_INFO_NAME("test.ids");

    private String name;

    ConstantDict(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
