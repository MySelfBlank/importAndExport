package utils;

import model.EField;
import onegis.common.utils.JsonUtils;
import onegis.exception.BaseException;
import onegis.psde.attribute.Field;
import onegis.psde.attribute.Fields;

import java.util.*;

/**
 * 字段输出为交换格式
 */
public class FieldUtils {

    /**
     * 字段到交换格式
     *
     * @param fields
     * @throws Exception
     */
    public static List<EField> dsFields2DataFile(Fields fields) throws Exception {
        if (fields == null) {
            throw new BaseException("字段为空");
        }
        List<EField> eFields = new ArrayList<>();
        List<Field> fieldList = fields.getFields();
        for (Field field : fieldList) {
            EField eField = dsField2EField(field);
            eFields.add(eField);
        }

        return eFields;
    }

    private static EField dsField2EField(Field field) throws Exception {
        EField eField = new EField();
        eField.setId(field.getId());
        eField.setCaption(field.getCaption());
        if ("date".equals(field.getType().getName())) {
            eField.setType("datetime");
        } else {
            eField.setType(field.getType().getName());
        }
        eField.setDesc(field.getDes());
        Map<String, Object> domain = new HashMap<>();
        if (field.getDomain() != null && !field.getDomain().equals("")) {
            Map<String, Object> objectMap = JsonUtils.parseMap(field.getDomain());
            for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
                if (entry.getKey().equals("Range")) {
                    domain.put("type", "list");
                } else {
                    domain.put("type", "range");
                }
                if (entry.getValue() instanceof List) {
                    List value = (List) entry.getValue();
                    value.removeAll(Collections.singleton(null));
                    if (value.size() == 0) {
                        domain.remove("type");
                        continue;
                    }
                    domain.put("value", value);
                } else {
                    domain.put("value", entry.getValue());
                }
            }
        }
        if (domain.size() > 0) {
            eField.setDomain(domain);
        }
        eField.setName(field.getName());
        if (field.getDefaultValue() != null) {
            eField.setDefaultValue(field.getDefaultValue().toString());
        }
        return eField;
    }
}
