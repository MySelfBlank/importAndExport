package services.export;

import model.ESDomain;
import onegis.common.utils.FileUtils;
import onegis.common.utils.GeneralUtils;
import onegis.common.utils.JsonUtils;
import onegis.psde.form.GeoBox;
import onegis.psde.psdm.OBase;
import onegis.psde.psdm.SDomain;
import onegis.psde.psdm.SObject;
import services.RequestServices;
import services.impl.RequestServicesImpl;

import java.util.List;

/**
 * 导出时空域
 */
public class ExportSDomain {

    private RequestServices requestServices = new RequestServicesImpl();

    /**
     * 导出时空域信息到本地
     * @param sObjects
     * @param outputDir 保存路径
     * @param fileName 文件名
     * @throws Exception
     */
    public void writeSDomain(List<SObject> sObjects, String outputDir, String fileName){
        if(sObjects!=null&&sObjects.size()>0){
            Long sDomainID = sObjects.get(0).getSdomain();
            if (!GeneralUtils.isNotEmpty(sDomainID)) {
                return;
            }
            try{
                SDomain sDomain = requestServices.getSDomain(sDomainID);
                ESDomain esDomain = new ESDomain();
                esDomain.setId(sDomain.getId());
                esDomain.setName(sDomain.getName());
                esDomain.setDesc(sDomain.getDes());
                esDomain.setSrs("epsg:4326");
                esDomain.setTrs("onegis:1001");
                if (sDomain.getsTime() != null) {
                    esDomain.setsTime(sDomain.getsTime().getTime());
                }
                if (sDomain.geteTime() != null) {
                    esDomain.seteTime(sDomain.geteTime().getTime());
                }
                List<OBase> parents = sDomain.getParents();
                if (GeneralUtils.isNotEmpty(parents)) {
                    esDomain.setParentId(parents.get(0).getId());
                }
                GeoBox geoBox = sDomain.getGeoBox();
                if (geoBox != null) {
                    esDomain.addGeobox(geoBox.getMinx(), geoBox.getMiny(), geoBox.getMinz(), geoBox.getMaxx(), geoBox.getMaxy(), geoBox.getMaxz());
                }
                FileUtils.writeContent(JsonUtils.objectToJson(esDomain), outputDir, fileName, false);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
