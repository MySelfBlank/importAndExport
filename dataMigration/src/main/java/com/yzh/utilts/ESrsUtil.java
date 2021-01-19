package com.yzh.utilts;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.yzh.dao.ESpatialReferenceSystem;
import com.yzh.userInfo.PathUtil;
import onegis.psde.psdm.OType;
import onegis.psde.reference.SpatialReferenceSystem;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.stream.Collectors;

import static cn.hutool.core.util.ObjectUtil.isEmpty;
import static cn.hutool.core.util.ObjectUtil.isNull;
import static com.yzh.Index.sDomain;


/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/23 11:12
 */
public class ESrsUtil {

    /**
     * 获取空间参照的对象
     * @param oTypeList
     * @throws ConcurrentModificationException
     */
    public static void getSrs(List<OType> oTypeList) throws ConcurrentModificationException {
        if (oTypeList == null || oTypeList.size() == 0) {
            return;
        }
        List<SpatialReferenceSystem> srsList = new ArrayList<>();
        for (OType oType : oTypeList) {
            if (!isNull(oType) && !isEmpty(oType)) {
                srsList.add(oType.getSrs());
            }
        }
        JSONArray jsonArray = JSONUtil.parseArray(srsList);
        List<ESpatialReferenceSystem> eSpatialReferenceSystems = jsonArray.toList(ESpatialReferenceSystem.class);
        List<ESpatialReferenceSystem> collect = eSpatialReferenceSystems.stream().distinct().collect(Collectors.toList());
        JSON json = JSONUtil.parse(collect);
        String path = PathUtil.baseInfoDir + "\\test.srs";
        FileTools.exportFile(json, path,"Srs");
    }
}
