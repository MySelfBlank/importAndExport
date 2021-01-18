package com.yzh.utilts;

import com.yzh.dao.SDomainOutPutModel;
import onegis.psde.form.GeoBox;
import onegis.psde.psdm.SDomain;

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
}

