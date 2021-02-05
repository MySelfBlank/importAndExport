package com.yzh.utilts.tools;

import java.util.Scanner;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/23 9:51
 */
public class EnvironmentSelectTool {
    public static String dev="";
    public static String devurl= "http://bt1.geosts.ac.cn/api/dae-dev/datastore";
    public static String produrl = "http://bt1.geosts.ac.cn/api/dae/datastore";
    public static String localHostUrl = "http://172.16.4.129:8085/datastore";
    public static String prodHDFSUrl = "http://bt1.geosts.ac.cn/api/dae/hdfs-service/hdfs";

    public static String modelDevUrl="http://bt1.geosts.ac.cn/api/dae-dev/model-service/model";
    public static String modelProdUrl="http://bt1.geosts.ac.cn/api/dae/model-service/model";
    public static String modelLocalUrl="http://172.16.4.129:8090/model";
    public static String devHDFSUrl = "http://bt1.geosts.ac.cn/api/dae-dev/hdfs-service/hdfs";

    public static String devUcUrl= "http://bt1.geosts.ac.cn/api/uc-dev";
    public static String prodUcUrl = "http://bt1.geosts.ac.cn/api/uc";
    public static String localHostUcUrl = "http://172.16.4.129:8085/btuc";
    public static String localHDFSUrl = "http://172.16.4.129:8085/hdfs";


    public static String finalUrl = produrl;
    public static String finalUcUrl=prodUcUrl;
    public static String finalModelUrl=modelProdUrl;
    public static String finalHDFSUrl = devHDFSUrl;


}
