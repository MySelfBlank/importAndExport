package com.yzh.services.export;

import com.yzh.dao.exportModel.EDObject;
import onegis.psde.psdm.DObject;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2021/1/11 10:10
 */
public class ExportDobject {


    private EDObject sDObjectToEDObject(DObject dObject){
        EDObject edObject = new EDObject();
        edObject.setId(dObject.getId());
        edObject.setName(dObject.getName());
        if (dObject.getOtype()!=null){
            edObject.setdType(dObject.getOtype().getName());
        }
        return null;
    }
}
