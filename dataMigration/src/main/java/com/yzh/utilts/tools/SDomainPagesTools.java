package com.yzh.utilts.tools;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.yzh.Index;
import com.yzh.api.MyApi;
import com.yzh.userInfo.UserInfo;
import onegis.psde.psdm.SDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static com.yzh.utilts.tools.FileTools.formatData;

/**
 * @author Yzh
 * @create 2020-12-16 10:53
 */
public class SDomainPagesTools {
    private static final Logger logger = LoggerFactory.getLogger(SDomainPagesTools.class);
    private static Map<String, Object> params = new HashMap<>();

    /**
     * 分页请求方法
     *
     * @param pageNum
     * @param pageSize
     * @param sDomainName
     * @param input
     * @param sDomains
     */
    public static void getPages(int pageNum, int pageSize, String sDomainName, Scanner input, List<SDomain> sDomains) {
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        params.put("names", sDomainName);
        params.put("token", UserInfo.token);
        String getRespond = HttpUtil.get(MyApi.getDomain1.getValue(), params);
        logger.debug("获取时空域完毕");
        JSONObject jsonData = formatData(getRespond);
        Index.pages = jsonData.getInt("pages");
        sDomains.clear();
        if((jsonData.getStr("list").equalsIgnoreCase("[]"))){
            System.out.println("未找到相关时空域");
            return;
        }
        sDomains.addAll(JSONArray.parseArray(jsonData.getStr("list"), SDomain.class));
        System.out.println("共" + Index.pages + "页" + " 当前第" + pageNum + "页");
        for (int i = 0; i < sDomains.size(); i++) {
            System.out.println("[" + (i + 1) + "]" + sDomains.get(i).getName());
        }
    }
}
