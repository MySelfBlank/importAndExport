package com.yzh.utilts;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yzh.api.MyApi;
import com.yzh.dao.SDomainOutPutModel;
import com.yzh.userInfo.UserInfo;
import onegis.psde.form.GeoBox;
import onegis.psde.psdm.SDomain;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/16 18:16
 */
public class SDomainUtil {
    public static SDomainOutPutModel getSDomain(SDomainOutPutModel sDomainOutPutModel, SDomain sDomain) {

        sDomainOutPutModel.setId(sDomain.getId());
        sDomainOutPutModel.setName(sDomain.getName());
        sDomainOutPutModel.setDesc(sDomain.getDes());
        sDomainOutPutModel.setSrs("epsg:4326");
        sDomainOutPutModel.setTrs("onegis:1001");
        if (sDomain.getsTime() != null) {
            sDomainOutPutModel.setStime(sDomain.getsTime().getTime());
        }
        if (sDomain.geteTime() != null) {
            sDomainOutPutModel.setEtime(sDomain.geteTime().getTime());
        }

//        List<OBase> parents = sDomain.getParents();
//        if (GeneralUtils.isNotEmpty(parents)) {
        sDomainOutPutModel.setParentId(null);
//        }
        GeoBox geoBox = sDomain.getGeoBox();
        if (geoBox != null) {
            sDomainOutPutModel.addGeobox(geoBox.getMinx(),
                    geoBox.getMiny(), geoBox.getMinz(),
                    geoBox.getMaxx(), geoBox.getMaxy(), geoBox.getMaxz());
        }
        return sDomainOutPutModel;
    }

    public static SDomainOutPutModel getSDomainById(SDomainOutPutModel sDomainOutPutModel, long sDomainId) {
        if (ObjectUtil.isNull(sDomainId) || ObjectUtil.isEmpty(sDomainId)) {
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("token", UserInfo.token);
        params.put("ids", sDomainId);
        String respose = HttpUtil.get(MyApi.getDomain1.getValue(), params);
        JSONObject jsonObject = (JSONObject) JSONUtil.parseObj(respose).get("data");
        JSONArray jsonArray = JSONUtil.parseArray(jsonObject.get("list"));
        List<SDomain> sDomains = jsonArray.toList(SDomain.class);

        sDomainOutPutModel.setId(sDomains.get(0).getId());
        sDomainOutPutModel.setName(sDomains.get(0).getName());
        sDomainOutPutModel.setDesc(sDomains.get(0).getDes());
        sDomainOutPutModel.setSrs("epsg:4326");
        sDomainOutPutModel.setTrs("onegis:1001");
        if (sDomains.get(0).getsTime() != null) {
            sDomainOutPutModel.setStime(sDomains.get(0).getsTime().getTime());
        }
        if (sDomains.get(0).geteTime() != null) {
            sDomainOutPutModel.setEtime(sDomains.get(0).geteTime().getTime());
        }

//        List<OBase> parents = sDomain.getParents();
//        if (GeneralUtils.isNotEmpty(parents)) {
        sDomainOutPutModel.setParentId(null);
//        }
        GeoBox geoBox = sDomains.get(0).getGeoBox();
        if (geoBox != null) {
            sDomainOutPutModel.addGeobox(geoBox.getMinx(),
                    geoBox.getMiny(), geoBox.getMinz(),
                    geoBox.getMaxx(), geoBox.getMaxy(), geoBox.getMaxz());
        }
        return sDomainOutPutModel;
    }
}

