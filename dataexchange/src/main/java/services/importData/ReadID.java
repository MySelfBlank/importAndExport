package services.importData;

import enums.ConstantDict;
import model.IDReset;
import onegis.common.utils.IdMakerUtils;
import onegis.common.utils.JsonUtils;
import utils.FileUtils;
import utils.PathUtil;

import java.util.*;

public class ReadID {
    public static Map<String,Map<String,String>> idMaps = new HashMap<>();

    /**
     * 读取本地存储的新的ID
     * @param path
     */
    public static void readId(String path){
        idMaps.clear();
        String content = FileUtils.readFile(path + "\\" + ConstantDict.ID_INFO_NAME.getName());
        List<IDReset> idResets = JsonUtils.jsonToList(content, IDReset.class);
        for(IDReset idReset:idResets){
            idMaps.put(idReset.getId(),idReset.getIdMpas());
        }
    }

    /**
     * 重置已保存的ID
     * @param path
     */
    public static void resetId(String path){
        String content = FileUtils.readFile(path + "\\" + ConstantDict.ID_INFO_NAME.getName());
        List<IDReset> idResets = JsonUtils.jsonToList(content, IDReset.class);
        List<IDReset> newIDList = new ArrayList<>();
        for(IDReset idReset:idResets){
            Map<String, String> idMpas = idReset.getIdMpas();
            for(String id:idMpas.keySet()){
                long newId = new IdMakerUtils().nextId();
                idMpas.put(id,newId+"");
            }
            idReset.setIdMpas(idMpas);
            newIDList.add(idReset);
        }
        onegis.common.utils.FileUtils.delFile(path + "\\" + ConstantDict.ID_INFO_NAME.getName());
        onegis.common.utils.FileUtils.writeContent(JsonUtils.objectToJson(newIDList),
                path, ConstantDict.ID_INFO_NAME.getName(), false);
    }
}
