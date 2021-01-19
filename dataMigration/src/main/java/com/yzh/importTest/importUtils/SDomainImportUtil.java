package com.yzh.importTest.importUtils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yzh.api.MyApi;
import com.yzh.userInfo.UserInfo;
import com.yzh.utilts.FileTools;
import onegis.psde.form.GeoBox;
import onegis.psde.psdm.Action;
import onegis.psde.psdm.SDomain;
import onegis.psde.reference.SpatialReferenceSystem;
import onegis.psde.reference.TimeReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.yzh.utilts.FileTools.login;

/**
 * @author Yzh
 * @create 2021-01-12 11:25
 * @details
 */
public class SDomainImportUtil {
    //日志工厂
    private static final Logger logger = LoggerFactory.getLogger(FieldImportUtil.class);

    public static void importSDomain(String sDomainPath)throws Exception{
        //读取文件
        logger.debug("时空域开始导入===========》读取文件");
        String SDomainStr = FileTools.readFile(sDomainPath);
        //构建SDomain
        SDomain sDomain = JSONUtil.toBean(SDomainStr, SDomain.class);
        ArrayList<Action> actions = new ArrayList<>();
        Action action = new Action();
        action.setId(0L);action.setOperation(33);
        actions.add(action);
        sDomain.setActions(actions);
        JSONObject object = JSONUtil.parseObj(SDomainStr);
        JSONArray geoBoxJSONArray = object.getJSONArray("geoBox");
        List<Double> geoBoxlist = geoBoxJSONArray.toList(Double.class);
        GeoBox geoBox = new GeoBox();
        geoBox.setMinx(geoBoxlist.get(0));
        geoBox.setMiny(geoBoxlist.get(1));
        geoBox.setMinz(geoBoxlist.get(2));
        geoBox.setMaxx(geoBoxlist.get(3));
        geoBox.setMaxy(geoBoxlist.get(4));
        geoBox.setMaxz(geoBoxlist.get(5));
        sDomain.setGeoBox(geoBox);
        SpatialReferenceSystem srs = new SpatialReferenceSystem();
        srs.setId("4326");
        sDomain.setSrs(srs);
        TimeReferenceSystem trs = new TimeReferenceSystem();
        trs.setId("2231");
        sDomain.setTrs(trs);
        List<SDomain> sDomains = new ArrayList<>();
        sDomains.add(sDomain);
        JSONUtil.parseArray(sDomains);
        //请求
        String response  = HttpUtil.post(MyApi.insertSDomain.getValue() + "?token=" + UserInfo.token, JSONUtil.parseArray(sDomains).toString());
        JSONObject responseJSONObj = JSONUtil.parseObj(response);
        if(!responseJSONObj.getStr("status").equals("200")){
            logger.error("导入失败="+responseJSONObj.getStr("message"));
            return;
        }

    }


}
