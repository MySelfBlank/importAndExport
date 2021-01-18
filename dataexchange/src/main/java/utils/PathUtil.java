package utils;

/**
 * 路径工具
 */
public class PathUtil {
    public static String baseDir="";
    public static String baseDirData="";

    public static void setDir(long count,String sdomainName,String baseDir) {
        PathUtil.baseDir = baseDir + "\\"+ sdomainName+"(0-" + count+")";
        PathUtil.baseDirData = baseDir + "\\"+ sdomainName+"(0-" + count+")\\data";
    }

}
