package com.yzh.dao;

import onegis.psde.util.JsonUtils;

/**
 * @author Yzh
 * @create 2020-12-17 8:38
 */
public class EFormRef {
    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String desc;

    /**
     * 文件名
     */
    private String fname;

    private Long fid;

    /**
     * 文件扩展名
     */
    private String extension;

    public EFormRef() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Long getFid() {
        return fid;
    }

    public void setFid(Long fid) {
        this.fid = fid;
    }

    public static void main(String[] args) throws Exception {
        EFormRef eFormRef = new EFormRef();
        eFormRef.setName("建筑");
        eFormRef.setDesc("这是一个楼");
        eFormRef.setFname("Vectorworks2016-IFC2x3-EQUA_IDA_ICE");
        eFormRef.setExtension("gltf");
        System.out.println(JsonUtils.objectToJson(eFormRef));
    }
}
