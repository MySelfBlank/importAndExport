package com.yzh.utilts;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.yzh.dao.ETrs;
import com.yzh.userInfo.PathUtil;
import onegis.psde.psdm.OType;
import onegis.psde.reference.TimeReferenceSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.hutool.core.util.ObjectUtil.isEmpty;
import static cn.hutool.core.util.ObjectUtil.isNull;
import static com.yzh.Index.sDomain;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/23 14:37
 */
public class ETrsUtil {

    /**
     * 获取时间参照
     * @param oTypeList
     */
    public static void getTrs(List<OType> oTypeList){
        if (oTypeList==null||oTypeList.size()==0){
            return ;
        }
       List<TimeReferenceSystem> trsList = new ArrayList<>();
        for (OType oType : oTypeList) {
            if (!isNull(oType)&&!isEmpty(oType)){
                trsList.add(oType.getTrs());
            }
        }
        JSONArray objects = JSONUtil.parseArray(trsList);
        List<ETrs> eTrsUtils = objects.toList(ETrs.class);
        List<ETrs> collect = eTrsUtils.stream().distinct().collect(Collectors.toList());

        JSON parse = JSONUtil.parse(collect);
        String path= PathUtil.baseInfoDir + "\\test.trs";
        FileTools.exportFile(parse,path,"Trs");
    }
}
