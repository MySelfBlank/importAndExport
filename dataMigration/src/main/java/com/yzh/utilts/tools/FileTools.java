package com.yzh.utilts.tools;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yzh.api.MyApi;
import com.yzh.userInfo.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/16 9:43
 * @ Desc          :  对登录，文件导出方法的封装
 */
public class FileTools {
    private static final Logger logger = LoggerFactory.getLogger(FileTools.class);

    /**
     * @param username
     * @param password
     */
    public static void login(String username, String password) {
        HashMap<String, Object> login = new HashMap<>();
        //设置登录参数
        login.put("username", username);
        login.put("password", password);
        //post请求
        String post = HttpUtil.post(MyApi.login.getValue(), login);
        //登录失败处理
        JSONObject object = JSONUtil.parseObj(post);
        if (object.getStr("status").equals("453")) {
            logger.error(object.getStr("message"));
            return;
        }
        //数据处理
        JSONObject data = formatData(post);

        logger.debug("存储用户信息");
        UserInfo.token = data.get("token").toString();
        logger.debug("用户Token信息=" + data.get("token").toString());

        Map<String, Object> param = new HashMap<>();
        param.put("token", UserInfo.token);
        String result = HttpUtil.get(MyApi.getUserInfo.getValue(), param);
        JSONObject userData = formatData(result);
        UserInfo.userId = userData.get("id").toString();
        UserInfo.email = userData.getStr("email");
        logger.debug("用户的ID=" + UserInfo.userId + " 邮箱是=" + UserInfo.email);
    }

    /**
     * 获取JSON对象中的list
     *
     * @param object
     * @return
     */
    public static JSONObject formatData(String object) {
        JSONObject sourceData = JSONUtil.parseObj(object);
        return (JSONObject) JSONUtil.parse(sourceData.get("data"));
    }

    public static JSONArray formatData2JSONArray(String object) {
        JSONObject sourceData = JSONUtil.parseObj(object);
        return (JSONArray) JSONUtil.parse(sourceData.get("data"));
    }

    public static Boolean judgeImportState(String response) {
        JSONObject sourceData = JSONUtil.parseObj(response);
        if (!sourceData.getInt("status").equals(200)) {
            return true;
        }
        return false;
    }

    public static <T> List forJsonList(String object, Class<T> tClass) throws Exception {
        JSONObject sourceData = JSONUtil.parseObj(object);
        JSONObject clearData = (JSONObject) JSONUtil.parse(sourceData.get("data"));
        JSONArray clearDatalist = (JSONArray) clearData.get("list");
//        List<JSON> relations =new ArrayList<>();
        List<T> relations = clearDatalist.toList(tClass);
//        List<Relation> relations = JsonUtils.jsonToList(clearData.get("list").toString(), Relation.class);
//        for (Object o : clearDatalist) {
//            relations.add(JSONUtil.parse(o));
//        }
        return relations;
    }

    public static void logout() {
        HttpUtil.get(MyApi.logout.getValue());
        logger.debug("运行结束");
    }

    /**
     * @param jsonObject
     * @desc 将JSON对象导出到本地文件
     */
    public static void exportFile(JSONObject jsonObject, String pathName, String fileName) {
        logger.debug("将" + fileName + "数据输出到本地");
        //创建一个文件路径
        File file = new File(pathName);
        FileWriter writer = null;
        //判断目录
        //判断文件是否存在
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }
//            try {
//                file.mkdirs();
//                //创建该文件
//                file.createNewFile();

            //将查询内容写到文件当中
            writer = new FileWriter(file);
            writer.write(jsonObject.toString());
            writer.flush();
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportFile(JSON json, String pathName, String fileName) {
        logger.debug("将" + fileName + "数据输出到本地");
        //创建一个文件路径
        File file = new File(pathName);
        FileWriter writer = null;
        //判断目录
        //判断文件是否存在
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }
//            try {
//                file.mkdirs();
//                //创建该文件
//                file.createNewFile();

            //将查询内容写到文件当中
            writer = new FileWriter(file);
            writer.write(json.toString());
            writer.flush();
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportFile(String json, String pathName, String fileName) {
        logger.debug("将" + fileName + "数据输出到本地");
        //创建一个文件路径
        File file = new File(pathName);
        FileWriter writer = null;
        //判断目录
        //判断文件是否存在
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }
//            try {
//                file.mkdirs();
//                //创建该文件
//                file.createNewFile();

            //将查询内容写到文件当中
            writer = new FileWriter(file);
            writer.write(json.toString());
            writer.flush();
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportFile(JSONArray json, String pathName) {
        logger.debug("将数据输出到本地");
        //创建一个文件路径
        File file = new File(pathName);
        FileWriter writer = null;
        //判断目录
        //判断文件是否存在
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }

            //将查询内容写到文件当中
            writer = new FileWriter(file);
            writer.write(json.toString());
            writer.flush();
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件读取方法
     *
     * @param fileUrl 文件的路径
     * @return
     */
    public static String readFile(String fileUrl) {
        FileReader fileReader = new FileReader(fileUrl);
        return fileReader.readString();
    }

    /**
     * 将Json数组转换为List
     *
     * @param JsonArrayStr Json数组字符串
     * @param tClass       需要转换的List 类型
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonArray2List(String JsonArrayStr, Class<T> tClass) {
        JSONArray jsonArray = JSONUtil.parseArray(JsonArrayStr);
        return jsonArray.toList(tClass);
    }

    /**
     * 获取文件夹下所有文件
     *
     * @param fileUrl
     * @return
     */
    public static List<File> getFiles(String fileUrl) {
        //目标集合fileList
        List<File> fileList = new ArrayList<>();
        File file = new File(fileUrl);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File fileIndex : files) {
                //如果文件为目录则进行递归搜索
                if (fileIndex.isDirectory()) {
                    getFiles(fileIndex.getPath());
                } else {
                    //如果文件为普通文件，则将文件加入集合
                    fileList.add(fileIndex);
                }
            }
        }
        return fileList;
    }

    /**
     * dll文件的下载
     *
     * @param srcPath
     * @param downloadPath
     */
    public static void utileDownLoad(String srcPath, String downloadPath) {
        try {
            URL url = new URL(MyApi.getDll.getValue() + "?srcPath=" + srcPath);
            URLConnection con = url.openConnection();
            InputStream input = con.getInputStream();
            // 本例是储存到本地文件系统，fileRealName为你想存的文件名称
            String fileName = srcPath.substring(srcPath.lastIndexOf("/") + 1).replaceAll("\\?", "_");
            File dest = new File(downloadPath + "/" + fileName);
            //获取父目录
            File fileParent = dest.getParentFile();
            if (!fileParent.exists()) {
                fileParent.mkdirs();
            }
            OutputStream output = new FileOutputStream(dest);
            int len = 0;
            byte[] ch = new byte[1024];
            while ((len = input.read(ch)) != -1) {
                output.write(ch, 0, len);
                output.flush();
            }
            output.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


