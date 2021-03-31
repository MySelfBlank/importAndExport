package com.yzh.userInfo;

/**
 * 路径工具
 */
public class PathUtil {

    public static String baseInfoDir = "";
    public static String baseInfoDirData = "";

    public static void setDir(String sdomainName, String baseDir) {
        PathUtil.baseInfoDir = baseDir;
        //存放行为脚本
        PathUtil.baseInfoDirData = baseDir  + "\\data";
    }

}
