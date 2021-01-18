package services;

import com.alibaba.fastjson.JSONObject;
import model.*;
import onegis.common.paging.PageInfo;
import onegis.psde.dynamicdata.DynamicDatas;
import onegis.psde.form.ModelBlock;
import onegis.psde.psdm.DObject;
import onegis.psde.psdm.OType;
import onegis.psde.psdm.SDomain;
import onegis.psde.psdm.SObject;

import java.io.File;
import java.util.List;
import java.util.Set;

public interface RequestServices {
    /**
     * 根据时空域ID获取该时空域下对象的ID
     * @param sdomainId
     * @param pageNum
     * @param pageSize
     * @return
     * @throws Exception
     */
    PageInfo<String> queryObjectIds(String sdomainId, String pageNum, String pageSize) throws Exception;

    /**
     * 根据id查询对象
     * @param sdomainId
     * @param ids
     * @return
     */
    List<SObject> querySObject(String sdomainId, List<String> ids);
    /**
     * 根据关键字查询时空域列表
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     * @throws Exception
     */
    ResponseResult queryDomain(int pageNum, int pageSize, String name) throws Exception;

    /**
     * 验证用户信息
     * @param userName
     * @param pwd
     * @return
     * @throws Exception
     */
    JSONObject queryUser(String userName,String pwd) throws Exception;

    /**
     * 根据token获取用户名
     * @param token
     * @return
     * @throws Exception
     */
    String getNickName(String token) throws Exception;

    /**
     * 根据OtId查询Otype
     * @param allOtIds
     * @return
     * @throws Exception
     */
    List<OType> getOtypes(Set<Long> allOtIds) throws Exception;

    /**
     * 下载dll文件
     * @param srcPath
     * @param downloadPath
     */
    void downLoadDll(String srcPath, String downloadPath) throws Exception;

    /**
     * 下载模型
     * @param modelId 模型ID
     * @param downloadPath
     * @throws Exception
     */
    void downLoadModle(String modelId, String downloadPath);
    /**
     * 根据时空域ID查询时空域
     * @param sdomainId
     * @return
     * @throws Exception
     */
    SDomain getSDomain(Long sdomainId) throws Exception;

    /**
     * 查询时空域下的关联关系网
     * @param sdomainId 时空域ID
     * @return
     * @throws Exception
     */
    List<EGraph> getRelationCatalog(Long sdomainId) throws Exception;

    /**
     * 根据id获取DObject
     * @param dobjectIds
     * @return
     * @throws Exception
     */
    List<DObject> getDobjectByIds(List<Long> dobjectIds) throws Exception;

    /**
     * 根据ID获取模型信息
     * @param fid
     * @return
     * @throws Exception
     */
    List<ModelBlock> getModel(Long fid) throws Exception;

    /**
     * 根据id列表获取动态数据
     * @param oids
     * @return
     * @throws Exception
     */
    DynamicDatas getDynamicDate(String oids) throws Exception;

    /**(
     * 保存对象
     * @param token
     * @param sObjects
     * @return
     */
    ResponseResult saveSObject(String token, List<CustomerSObject> sObjects,Integer num) throws Exception;

    /**
     * 上传模型文件
     * @param file 文件
     * @return 已上传的ID
     * @throws Exception
     */
    Model uploadModel(File file) throws Exception;

    /**
     * 保存动态数据
     * @param dataJson 动态数据（json）
     * @return
     */
    ResponseResult saveDynamicDatas(String dataJson) throws Exception;

    /**
     * 判断Otype中tags标签是否存在
     * @param tags
     * @return
     * @throws Exception
     */
    Boolean isExistTag(String tags) throws Exception;

    /**
     * 根据tags查询Otype
     * @param tags
     * @return
     * @throws Exception
     */
    OType queryOtype(String tags) throws Exception;
}
