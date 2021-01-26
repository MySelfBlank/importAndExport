package services.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import enums.ConstantDict;
import onegis.common.paging.PageInfo;
import onegis.psde.psdm.SObject;
import services.export.*;
import services.ExportServices;
import services.RequestServices;
import utils.PathUtil;

import java.util.ArrayList;
import java.util.List;

import static services.export.ExecuteContainer.DOTypeIdList;


public class ExportServicesImpl implements ExportServices {
    private RequestServices requestServices= new RequestServicesImpl();
    private ExportDllFile exportDllFile = new ExportDllFile();
    private ExportSDomain exportSDomain = new ExportSDomain();
    private ExportNetwork exportNetwork = new ExportNetwork();
    private ExportDobject exportDobject = new ExportDobject();
    private ExportDynamicDate exportDynamicDate = new ExportDynamicDate();
    private ExportModel exportModel = new ExportModel();
    @Override
    public void exportSobject(String path,String sdomainId,String sdomainName){
        System.out.println("------------------开始导出数据--------------------");
        long startTime = System.currentTimeMillis();
        ExecuteContainer.clear();
        List<String> ids = new ArrayList<>();
        try{
            PageInfo<String> listPageInfo = requestServices.queryObjectIds(sdomainId, "1", "1000");
            ids.addAll(listPageInfo.getList());
            long total = listPageInfo.getTotal();
            PathUtil.setDir(total,sdomainName,path);//设置路径
            double region = Math.ceil(total/(double)1000) +1;
            List<SObject> sObjects = new ArrayList<>();
            for(int i=1;i<region;i++){
                if(i!=1){
                    listPageInfo = requestServices.queryObjectIds(sdomainId, i+"", "1000");
                    ids.addAll(listPageInfo.getList());
                }
                sObjects = requestServices.querySObject(sdomainId, listPageInfo.getList());
                /**导出动态数据*/
                exportDynamicDate.downloadDynamicData(sObjects);
                /** 导出含有动态数据的类模板Id*/
                if (DOTypeIdList.size()!=0){
                    FileUtil.writeString(JSONUtil.parseArray(DOTypeIdList).toString(),path+"\\"+sdomainName+"\\DOtypeId.text","utf-8");
                }
                handleSObject(sObjects,i);
            }
            /**导出模型文件*/
            exportModel.downloadModel();
            System.out.println("------------------开始导出对象类和关系--------------------");
            /**导出对象类和关系*/
            ExportClassAndRelation.writeClassAndRelation();
            /**导出DObject文件*/
            exportDobject.writeDoject();
            /**导出关联关系网*/
            exportNetwork.wiriteNetwork(PathUtil.baseDir,Long.parseLong(sdomainId));
            /**导出时空域信息*/
            exportSDomain.writeSDomain(sObjects,PathUtil.baseDir, ConstantDict.SDOMAIN_DATA_FILE_NAME.getName());
            /**导出空间参考*/
            ExportTrsAndSrs.writeSrs(PathUtil.baseDir);
            /**导出时间参考*/
            ExportTrsAndSrs.writeTrs(PathUtil.baseDir);
            /**导出元数据*/
            ExportMetadata.writeMetaData(PathUtil.baseDir);
            /**导出数据字典*/
            ExportDatum.writeDatum(PathUtil.baseDir);
            /**导出重置的ID数据*/
            ExportIDMap.writeNewIds();
            long endTime = System.currentTimeMillis();
            long useTime = endTime - startTime;
            System.out.println(String.format("====================导出成功,共耗时 %s 小时 %s 分 %s秒====================", useTime/3600000, useTime%3600000/60000, useTime%3600000%60000/1000));
        }catch (Exception e){
            System.out.println("查询数据错误："+e.getMessage());
        }
    }

    /**
     * 处理SObject
     * @param sObjects
     * @param region
     * @throws Exception
     */
    private void handleSObject(List<SObject> sObjects,Integer region){
        /**下载dll文件*/
        exportDllFile.writeDllFiles(sObjects);
        /**处理DObject文件*/
        exportDobject.dsDObject2DataFile(sObjects);
        /**导出SObject数据*/
        ExportSObject.writeSObject(sObjects,region);
    }




}
