package enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 形态字典
 */
public enum EFormEnum {
    /**
     * 位置形态
     */
    NULL("null",-1),
    /**
     * 几何形态
     */
    GEOMETRY("geometry", 20),
    /**
     * 点形态
     */
    POINT("point", 21),
    /**
     * 线形态
     */
    LINESTRING("linestring", 22),
    /**
     * 简单多边形
     */
    POLYGON("polygon", 23),

    /**
     * 地形
     */
    GEOGRAPHY("geography", 30),
    /**
     * 等高线
     */
    ISOHYPSE("isohypse", 31),
    /**
     * dem
     */
    DEM("dem", 32),
    /**
     * tin
     */
    TIN("tin", 33),
    /**
     * bim
     */
    BIM("bim", 40),
    /**
     * 模型
     */
    MODEL("model", 50),
    /**
     * 球体
     */
    SHAPEBLOCK("shapeblock",60),
    /**
     * 椭球
     */
    ELLIPSOID("ellipsoid", 61),
    /**
     * 三角面片
     */
    TRIANGULARMESH("triangularmesh", 62);

    private final int value;
    private final String name;

    EFormEnum(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static List<Map<String, Object>> enumList() {
        List<Map<String, Object>> list = new ArrayList<>();
        for(EFormEnum formEnum : values()){
            Map<String, Object> map = new HashMap<>();
            map.put("name", formEnum.getName());
            map.put("value", formEnum.getValue());
            list.add(map);
        }
        return list;
    }

    public static List<Map<String, Object>> enumList(int startNum, int endNum){
        List<Map<String, Object>> list = new ArrayList<>();
        for(EFormEnum formEnum : values()){
            if(formEnum.getValue() >= startNum && formEnum.getValue() < endNum) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", formEnum.getName());
                map.put("value", formEnum.getValue());
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 根据value获取枚举对象
     * @param value
     * @return
     */
    //@JsonCreator
//    public static EFormEnum getEnumByValue(int value){
//        for(EFormEnum formEnum : EFormEnum.values()){
//            if(formEnum.getValue() == value){
//                return formEnum;
//            }
//        }
//        return null;
//    }
    /**
     * 根据name获取枚举对象
     * @param name
     * @return
     */
    @JsonCreator
    public static EFormEnum getEnum(Object name){
        for(EFormEnum formEnum : EFormEnum.values()){
            if(formEnum.getName().equals(name) ){
                return formEnum;
            }
            if(name.toString().equals(formEnum.getValue()+"")){
                return formEnum;
            }
        }
        return null;
    }
//    @JsonCreator
//    public static EFormEnum getEnumByName(String name){
//        for(EFormEnum formEnum : EFormEnum.values()){
//            if(formEnum.getName().equals(name) ){
//                return formEnum;
//            }
//        }
//        return null;
//    }

    @JsonValue
    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

}

