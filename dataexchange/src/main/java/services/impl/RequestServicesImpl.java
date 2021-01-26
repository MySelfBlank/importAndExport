package services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bt.google.protobuf.ByteString;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.*;
import onegis.common.paging.PageInfo;
import onegis.common.utils.FileUtils;
import onegis.common.utils.GeneralUtils;
import onegis.common.utils.IdMakerUtils;
import onegis.common.utils.JsonUtils;
import onegis.exception.BaseException;
import onegis.message.ResponseMessage;
import onegis.protobuf.model.PbModelData;
import onegis.psde.catalog.RelationCatalog;
import onegis.psde.catalog.SDomainRelationCatalog;
import onegis.psde.dictionary.OrderByEnum;
import onegis.psde.dynamicdata.DynamicDatas;
import onegis.psde.filter.SpatialFilter;
import onegis.psde.form.ModelBlock;
import onegis.psde.psdm.DObject;
import onegis.psde.psdm.OType;
import onegis.psde.psdm.SDomain;
import onegis.psde.psdm.SObject;
import onegis.psde.relation.Network;
import onegis.result.ResultData;
import org.apache.http.entity.ContentType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import services.export.ExecuteContainer;
import services.RequestServices;
import services.importData.ReadSObject;
import utils.BaseUrl;
import utils.HttpClientUtils;
import utils.PathUtil;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;

import static org.jaitools.CollectionFactory.map;

public class RequestServicesImpl implements RequestServices {

    @Override
    public PageInfo<String> queryObjectIds(String sdomainId, String pageNum, String pageSize) throws Exception{
        Map<String, String> params = new HashMap<>();
        params.put("sDomainId", sdomainId);
        params.put("pageSize", pageSize);
        params.put("pageNum", pageNum);
        ResponseResult responseResult = HttpClientUtils.doGet(BaseUrl.DATASTORE_URL + "/rest/v0.1.0/datastore/object/getAllOIdsBySDomain", params);
        Object data = responseResult.getData();
        JSONObject jsonObject = JSONObject.parseObject(data.toString());
        PageInfo pageInfo = jsonObject.toJavaObject(PageInfo.class);
        return pageInfo;
    }

    @Override
    public List<SObject> querySObject(String sdomainId, List<String> ids) {
        String POST_URL = BaseUrl.DATASTORE_URL + "/rest/v0.1.0/datastore/object/query1";
        SpatialFilter spatialFilter = new SpatialFilter();
        spatialFilter.setLoadForm(true);
        spatialFilter.setLoadDynamicData(false);
        spatialFilter.setLoadModel(true);
        spatialFilter.setLoadNetwork(true);
        spatialFilter.setLoadObjType(true);
        spatialFilter.setLoadDes(true);
        spatialFilter.setLoadAction(true);
        spatialFilter.setLoadChildren(true);
        spatialFilter.setLoadVersion(true);
        spatialFilter.setOrderType(OrderByEnum.VID);
        spatialFilter.setDescOrAsc(false);
        HashSet<Long> sdomains = new HashSet<>();
        sdomains.add(Long.parseLong(sdomainId));
        spatialFilter.setSdomains(sdomains);
        if (ids != null && !ids.isEmpty()) {
            HashSet<String> oIds = new HashSet<>();
            oIds.addAll(ids);
            spatialFilter.setIds(oIds);
        }
        try{
            ResponseResult responseResult = HttpClientUtils.doPostWithJson(POST_URL, JsonUtils.objectToJson(spatialFilter));
            if (responseResult.getStatus() != ResponseMessage.OK.getStatus()) {
                System.out.println(String.format("IDS: %s 的对象查询失败，失败原因：%s", JSONObject.toJSONString(ids), responseResult.getMessage()));
                return new ArrayList<>();
            }
            Object data = responseResult.getData();
            PageInfo resultdata = JsonUtils.jsonToPojo(JsonUtils.objectToJson(data), PageInfo.class);
            Object resultdataData = resultdata.getList();
            List<SObject> sObjects =  JsonUtils.jsonToList(JsonUtils.objectToJson(resultdataData), SObject.class);
            return  sObjects;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseResult queryDomain(int pageNum, int pageSize, String name) throws Exception{
        String url = BaseUrl.DATASTORE_URL + "/rest/v0.1.0/datastore/sdomain/query?pageNum=" + pageNum + "&pageSize=" + pageSize;
        if (name!= null && !name.equals("")) {
            url += "&names=" + URLEncoder.encode(name,"utf-8");
        }
        ResponseResult responseResult = HttpClientUtils.doGet(url);
        return responseResult;
    }

    @Override
    public JSONObject queryUser(String userName, String pwd) throws Exception{
        String url = BaseUrl.UC_URL + "/api/v2/account/login?"+"username=" + userName + "&" + "password=" + pwd;
        JSONObject jsonObject = HttpClientUtils.doPost(url);
        return jsonObject;
    }

    @Override
    public String getNickName(String token) throws Exception {
        String url = BaseUrl.UC_URL + "/api/v2/account/authorize?token="+token;
        ResponseResult responseResult = HttpClientUtils.doGet(url);
        Object data = responseResult.getData();
        JSONObject user = JSON.parseObject(data.toString());
        String nickName = user.get("nickName").toString();
        return nickName;
    }

    @Override
    public List<OType> getOtypes(Set<Long> allOtIds) throws Exception {
        String httpUrl = BaseUrl.DATASTORE_URL + "/rest/v0.1.0/datastore/otype/query";
        Map<String, String> params = new HashMap<>();
        params.put("ids", allOtIds.toString().substring(1, allOtIds.toString().length() -1));
        params.put("loadModel", "true");
        ResponseResult responseResult = HttpClientUtils.doGet(httpUrl, params);
        if(responseResult==null||responseResult.getStatus()!=200){
            return new ArrayList<>();
        }
        Object data = responseResult.getData();
        if(data==null){
            return new ArrayList<>();
        }
        JSONObject jsonObject = JSONObject.parseObject(data.toString());
        PageInfo pageInfo = jsonObject.toJavaObject(PageInfo.class);
        List list = pageInfo.getList();
        ObjectMapper objectMapper = new ObjectMapper();
        List<OType> oTypes = objectMapper.readValue(objectMapper.writeValueAsString(pageInfo.getList()), new TypeReference<List<OType>>(){});
        return oTypes;
    }

    @Override
    public void downLoadDll(String srcPath, String downloadPath) throws Exception{
        String url = BaseUrl.HDFS_URL + "/rest/v0.1.0/datastore/slave/hdfs/download?srcPath=" + srcPath;
        HttpClientUtils.execDownlLoad(url,downloadPath,srcPath);
    }

    @Override
    public void downLoadModle(String modelID, String downloadPath){
        if (!GeneralUtils.isNotEmpty(modelID)) {
            return;
        }
        try {
            String url = BaseUrl.MODEL_URL + "/rest/v0.1.0/datastore/slave/model/file/download/" + modelID;
            byte[] result = HttpClientUtils.doPostIn(url);
            PbModelData.Pb3DModelResponseResult resData = PbModelData.Pb3DModelResponseResult.newBuilder().build().parseFrom(result);
            if (resData.getStatus() == 200) {
                PbModelData.Pb3DModelFile fileMode = resData.getFileModel();
                ByteString fileData = fileMode.getFileData();
                byte[] data = fileData.toByteArray();
                InputStream input = new ByteArrayInputStream(data);
                // 本例是储存到本地文件系统，fileRealName为你想存的文件名称
                String fileName = fileMode.getFileName().replaceAll("/", "_").replaceAll(":", "");
                String fname = ExecuteContainer.modelNamesMap.get(modelID);
                if (fname != null && !"".equals(fname)) {
                    fileName = fname;
                }
                File dest = new File(downloadPath + "/" + fileName + "." + fileMode.getFileExt());
                //获取父目录
                File fileParent = dest.getParentFile();
                //判断是否存在
                if (!fileParent.exists()) {
                    //创建父目录文件
                    fileParent.mkdirs();
                }
                OutputStream output = new FileOutputStream(dest);
                int len = 0;
                byte[] ch = new byte[1024];
                while ((len = input.read(ch)) != -1) {
                    output.write(ch, 0, len);
                }
                output.close();
                System.out.println(PathUtil.baseDirData+"\\"+ fname + "." + fileMode.getFileExt() + "-- 下载成功！");
            } else {
                System.out.println("下载模型文件：" + modelID + ",失败");
            }
        }catch (Exception e) {
            System.out.println("下载模型文件：" + modelID + ",异常 -- " + e.getMessage());
        }
    }

    @Override
    public SDomain getSDomain(Long sdomainId) throws Exception {
        String utl = BaseUrl.DATASTORE_URL + "/rest/v0.1.0/datastore/sdomain/query";
        HashMap<String,String> params = new HashMap<>();
        params.put("ids",sdomainId+"");
        ResponseResult responseResult = HttpClientUtils.doGet(utl, params);
        if(responseResult.getStatus()==200){
            Object data = responseResult.getData();
            if(data!=null){
                String msg = JSONObject.toJSONString(data);
                PageInfo pageInfo = JSONObject.parseObject(msg, PageInfo.class);
                List list = pageInfo.getList();
                if (list!=null&&list.size()>0){
                    SDomain sDomain = JSONObject.parseObject(list.get(0).toString(), SDomain.class);
                    return sDomain;
                }else {
                    System.out.println("不存在该时空域");
                    return null;
                }

            }else {
                return null;
            }
        }else {
            System.out.println(responseResult.getMessage());
            return null;
        }
    }

    @Override
    public List<EGraph> getRelationCatalog(Long sdomainId) throws Exception {
        List<EGraph> result = new ArrayList<>();
        String url = BaseUrl.DATASTORE_URL + "/rest/v0.1.0/datastore/getRelationCatalog?sdomain=" + sdomainId;
        ResponseResult responseResult = HttpClientUtils.doGet(url);
        List<SDomainRelationCatalog> sDomainRelationCatalogs = new ArrayList<>();
        if(responseResult.getStatus()==200){
            sDomainRelationCatalogs = JsonUtils.jsonToList(JsonUtils.objectToJson(responseResult.getData()), SDomainRelationCatalog.class);
        }
        if (sDomainRelationCatalogs == null || sDomainRelationCatalogs.isEmpty()) {
            return result;
        }

        SDomainRelationCatalog sDomainRelationCatalog = sDomainRelationCatalogs.get(0);
        if (sDomainRelationCatalog == null) {
            return result;
        }

        List<RelationCatalog> relationCatalogs = sDomainRelationCatalog.getRelations();
        if (relationCatalogs == null || relationCatalogs.isEmpty()) {
            return result;
        }
        for (RelationCatalog relationCatalog : relationCatalogs) {
            EGraph eGraph = new EGraph(relationCatalog);
            result.add(eGraph);
        }
        return result;
    }

    @Override
    public List<DObject> getDobjectByIds(List<Long> dobjectIds) throws Exception {
        /**注意不要让ids过大*/
        String requestUrl = BaseUrl.DATASTORE_URL +"/rest/v0.1.0/datastore/dobject/queryByFrom?fromIds=";
        String idsString = setToString(dobjectIds);
        requestUrl += idsString;
        ResponseResult responseResult = HttpClientUtils.doGet(requestUrl);
        if(responseResult.getStatus() != 200){
            System.out.println("==============>>>>>>>> DObject查询失败，失败原因：" + responseResult.getMessage());
            throw new BaseException(ResponseMessage.BAD_REQUEST, responseResult.getMessage());
        }
        Object data = responseResult.getData();
        PageInfo<DObject> resultdata = JsonUtils.jsonToPojo(JsonUtils.objectToJson(data), PageInfo.class);
        List<DObject> dObjects = JsonUtils.jsonToList(JsonUtils.objectToJson(resultdata.getList()), DObject.class);
        return dObjects;
    }

    @Override
    public List<ModelBlock> getModel(Long fid) throws Exception {
        String url = BaseUrl.MODEL_URL + "/rest/v0.1.0/datastore/slave/model/file/query?fids=" + fid;
        ResponseResult responseResult = HttpClientUtils.doGet(url);
        if(responseResult.getStatus()==200){
            PageInfo<ModelBlock> modelBlockPageInfo = JsonUtils.jsonToPojo(JsonUtils.objectToJson(responseResult.getData()), PageInfo.class);
            List<ModelBlock> modelBlocks = JsonUtils.jsonToList(JsonUtils.objectToJson(modelBlockPageInfo.getList()), ModelBlock.class);
            return modelBlocks;
        }
        return null;
    }

    @Override
    public DynamicDatas getDynamicDate(String oids) throws Exception {
        String tableName = "trackdata";
        String url = BaseUrl.GEOMESA_URL + "/rest/v0.1.0/datastore/slave/geomesa/query";
        String json = "{\"oids\":[" + oids + "],\"tableName\":\"" + tableName + "\"}";
        ResponseResult responseResult = HttpClientUtils.doPostWithJson(url, json);
        if(responseResult.getStatus()==200){
            Object data = responseResult.getData();
            ResultData resultdata = JsonUtils.jsonToPojo(JsonUtils.objectToJson(data), ResultData.class);
            Object resultdataData = resultdata.getData();
            /** 获取动态轨迹数据 */
            DynamicDatas dynamicDatas = JsonUtils.jsonToPojo(JsonUtils.objectToJson(resultdataData), DynamicDatas.class);
            return dynamicDatas;
        }
        return null;
    }

    @Override
    public ResponseResult saveSObject(String token, List<CustomerSObject> sObjects,Integer num) throws Exception{
        String url = BaseUrl.DATASTORE_URL + "/rest/v0.1.0/datastore/object/saveObject?token=" + token + "&idstrategy=true";
        ObjectMapper objectMapper = new ObjectMapper();
        for(CustomerSObject customerSObject:sObjects){
            Long id = customerSObject.getId();
            List<CustomerSObject> sObjectList = new ArrayList<>();
            sObjectList.add(customerSObject);
            String json = objectMapper.writeValueAsString(sObjectList);
            //替换ID
            Optional<List<ERNode>> erNodesOp = Optional.of(customerSObject).map(so -> so.getNetwork()).map(network -> network.getNodes());
            List<ERNode> erNodes = erNodesOp.orElse(null);

            String content = ReadSObject.changID(json, id + "",erNodes);
            ResponseResult responseResult = HttpClientUtils.doPostWithJson(url, content);
            if(responseResult.getStatus()!=200){
                System.out.println("导入错误："+responseResult.getMessage()+"--Name:"+customerSObject.getName()+"--num:"+num);
                System.out.println("url:"+url+"\r\n+json:"+content);
            }
        }
        ResponseResult responseResult = new ResponseResult(200);
        return responseResult;
    }

    @Override
    public Model uploadModel(File file) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(),
                ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
        String url = BaseUrl.MODEL_URL + "/rest/v0.1.0/datastore/slave/model/file/upload";
        Map<String, String> params = new HashMap<>();
        params.put("token", BaseUrl.token);
        params.put("name", file.getName().substring(0, file.getName().lastIndexOf(".")));
        Map<String, MultipartFile> multipartFileMap = new HashMap<>();
        multipartFileMap.put("file", multipartFile);
        String result = HttpClientUtils.doPostByte(url, params, multipartFileMap);
        onegis.result.response.ResponseResult responseResult = JsonUtils.jsonToPojo(result, onegis.result.response.ResponseResult.class);
        if (responseResult.getStatus() == 200) {
            Model model = JSON.parseObject(JSON.toJSONString(responseResult.getData()), Model.class);
            System.out.println("模型" +  model.getFname() +"保存成功，fid：" + model.getFid());
            return model;
        }else {
            System.out.println("模型" +  file.getName() +"保存失败" );
        }
        return null;
    }

    @Override
    public ResponseResult saveDynamicDatas(String dataJson) throws Exception{
        String url = BaseUrl.DATASTORE_URL + "/rest/v0.1.0/datastore/object/saveDynamicData?token=" + BaseUrl.token;
        ResponseResult responseResult = HttpClientUtils.doPostWithJson(url, dataJson);
        return responseResult;
    }

    @Override
    public Boolean isExistTag(String tags) throws Exception {
        String url = BaseUrl.DATASTORE_URL +"/rest/v0.1.0/datastore/otype/isExistTags?tags="+tags;
        ResponseResult responseResult = HttpClientUtils.doGet(url);
        if(responseResult.getStatus()==1){//status==1代表该标签是存在的
            return true;
        }
        return false;
    }

    @Override
    public OType queryOtype(String tags) throws Exception {
//        String url = BaseUrl.DATASTORE_URL +"/rest/v0.1.0/datastore/otype/query?tags=" +tags;
        String url = "/rest/v0.1.0/datastore/otype/query?tags=" +tags;
        ResponseResult responseResult = HttpClientUtils.doGet(url);
        if(responseResult.getStatus()==200){
            PageInfo<OType> pageInfo = JsonUtils.jsonToPojo(JsonUtils.objectToJson(responseResult.getData()), PageInfo.class);
            if(pageInfo.getTotal()>0){
                List<OType> oTypes = JsonUtils.jsonToList(JsonUtils.objectToJson(pageInfo.getList()), OType.class);
                return oTypes.get(0);
            }
        }
        return null;
    }

    private String setToString(List<Long> ids){
        if (ids == null||ids.size() == 0) {
            return "";
        }
        StringBuilder idsBuilder = new StringBuilder();
        for (Long id : ids) {
            idsBuilder.append(id + ",");
        }
        return idsBuilder.substring(0, idsBuilder.length() - 1);
    }

    public static void main(String[] args) {

    }
}
