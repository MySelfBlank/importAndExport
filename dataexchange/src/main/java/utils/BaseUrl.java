package utils;

public class BaseUrl {

    //public static final String DATASTORE_URL = "http://bt1.geosts.ac.cn/api/dae/datastore";
    public static String DATASTORE_URL = "http://192.168.1.104:8080/datastore";
    //"http://bt1.geosts.ac.cn/api/dae/datastore";
    public static String MODEL_URL = "http://192.168.1.105:8080/model";
    //"http://bt1.geosts.ac.cn/api/dae/model-service/model";
    public static String HDFS_URL = "http://192.168.1.105:8080/hdfs";
    //"http://bt1.geosts.ac.cn/api/dae/hdfs-service/hdfs";
    public static String GEOMESA_URL = "http://192.168.1.104:8080/geomesa";
    public static String UC_URL = "http://192.168.1.104:8080/btuc";
    //"http://bt1.geosts.ac.cn/api/uc";
    public static String token = "";


    public static String localHostUrl = "http://172.16.4.129:8085/datastore";
    public static String modelLocalUrl = "http://172.16.4.129:8090/model";
    public static String localHostUcUrl = "http://172.16.4.129:8085/btuc";
}
