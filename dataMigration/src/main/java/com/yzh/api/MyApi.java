package com.yzh.api;

import static com.yzh.utilts.tools.EnvironmentSelectTool.*;

/**
 * @author Yzh
 * @create 2020-12-03 14:30
 */
public enum MyApi {

    //登录 api
    login("login", finalUcUrl + "/api/v2/account/login"),

    //userInfo
    getUserInfo("getUserInfo", finalUcUrl + "/api/v2/account/authorize"),

    //获取时空域 api
    getDomain("getDomain", finalUrl + "/rest/v0.1.0/datastore/sdomain/query?token=@token&names=@names"),

    getDomain1("getDomain", finalUrl + "/rest/v0.1.0/datastore/sdomain/query"),

    //获取类模板信息根据ID
    getOtypesByIds("getOtypesByIds", finalUrl + "/rest/v0.1.0/datastore/otype/query"),

    //获取时空域下的所有的空间对象
    getObject("getObject", finalUrl + "/rest/v0.1.0/datastore/object/query"),

    //获取时空域下的连接关系 参数信息传入时空域 sdomian
    getNetWork("", "http://bt1.geosts.ac.cn/api/dae" + dev + "/rest/v0.1.0/datastore/getRelationCatalog"),

    //获取时空域下的连接关系
    getRelationById("getRelation", finalUrl + "/rest/v0.1.0/datastore/orelation/query"),

    //获取字段
    getFieldByFid("getFieldByFid", finalUrl + "/rest/v0.1.0/datastore/ofield/query"),

    getStyleById("", finalUrl + "/rest/v0.1.0/datastore/oformstyle/query"),

    //获取行为属性传入id
    getModelById("getModelById", finalUrl + "/rest/v0.1.0/datastore/model/query"),

    //导入字段 Post (允许批量插入)
    insertField("insertField", finalUrl + "/rest/v0.1.0/datastore/ofield/insert?token=@token"),

    //导入形态样式 Post (允许批量插入)
    insertFormStyle("insertFormStyle", finalUrl + "/rest/v0.1.0/datastore/oformstyle/insert?token=@token"),

    //导入关系Post
    insertRelation("insertRelation", finalUrl + "/rest/v0.1.0/datastore/orelation/insert?token=@token"),

    //导入行为
    insertModel("insertModel", finalUrl + "/rest/v0.1.0/datastore/model/insert"),

    uploadModel("uploadModel", finalModelUrl + "/rest/v0.1.0/datastore/slave/model/file/upload"),
    //导入行为类别post
    insertModelDef("insertModelDef", finalUrl + "/rest/v0.1.0/datastore/modeldef/insert"),
    //获取行为类别传入id
    getModelDefById("getModelDefById", finalUrl + "/rest/v0.1.0/datastore/modeldef/query"),

    //行为脚本的下载
    getModelScript("getModelScript", finalHDFSUrl + "/rest/v0.1.0/datastore/slave/hdfs/download"),

    //获取Dobject根据Id
    getDObject("getDObject", finalUrl + "/rest/v0.1.0/datastore/dobject/queryByFrom"),

    //下载dll文件
    getDll("getDll", finalHDFSUrl + "/rest/v0.1.0/datastore/slave/hdfs/download"),

    //获取样式模型
    getModelInfo("getModelInfo", finalModelUrl + "/rest/v0.1.0/datastore/slave/model/file/query"),

    //下载模型url
    downModelUrl("downModelUrl", finalModelUrl + "/rest/v0.1.0/datastore/slave/model/file/download/"),

    insertOtype("insertOtype", finalUrl + "/rest/v0.1.0/datastore/otype/insert"),
    //导入时空域
    insertSDomain("oconnector", finalUrl + "/rest/v0.1.0/datastore/sdomain/edit"),

    //获取连接器
    getConnector("getConnector", finalUrl + "/rest/v0.1.0/datastore/oconnector/query"),

    //创建连接器
    insertConnector("insertConnector", finalUrl + "/rest/v0.1.0/datastore/oconnector/insert"),


    //注销本次登录
    logout("logout", finalUcUrl + "/api/v2/account/logout");


    private final String name;

    private final String value;

    MyApi(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
