package com.yzh.utilts.action;

import com.yzh.dao.enums.EActionEnum;
import com.yzh.dao.exportModel.EAction;
import com.yzh.dao.exportModel.EActionEvent;
import com.yzh.dao.exportModel.EAttribute;
import com.yzh.dao.exportModel.EVersion;
import onegis.psde.attribute.Attribute;
import onegis.psde.attribute.Attributes;
import onegis.psde.psdm.SObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildAttributeAction {
    /**
     * 判断属性信息的变化
     * @param eVersion
     * @param lastSObject
     * @param thisSObject
     */
    public static void setAttributeAction(EVersion eVersion, SObject lastSObject, SObject thisSObject) {
        Attributes lastAttributes = lastSObject.getAttributes();
        Attributes thisAttributes = thisSObject.getAttributes();
        if (lastAttributes == null && thisAttributes == null) {
            return;
        }
        // 上一版本中没有属性，这个版本中有，表示全部属性为新增
        if ((lastAttributes == null || lastAttributes.getAttributeList().isEmpty()) && thisAttributes != null && !thisAttributes.getAttributeList().isEmpty()) {
            List<Attribute> attributeList = thisAttributes.getAttributeList();
            for (Attribute attribute : attributeList) {
                operationAttribute(eVersion, attribute, EActionEnum.ADDING);
            }
        }
        // 上一版本中有属性，这个版本中没有属性，表示全部为删除
        else if (lastAttributes != null && !lastAttributes.getAttributeList().isEmpty() && (thisAttributes == null || thisAttributes.getAttributeList().isEmpty())) {
            List<Attribute> attributeList = lastAttributes.getAttributeList();
            for (Attribute attribute : attributeList) {
                operationAttribute(eVersion, attribute, EActionEnum.DELETE);
            }
        }
        // 如果两个版本中都有属性，则计算新增、删除、修改的属性
        else {
            List<Attribute> lastAttributeList = lastAttributes.getAttributeList();
            Map<Long, Attribute> lastAttributeMap = new HashMap<>();
            lastAttributeList.forEach(attribute -> lastAttributeMap.put(attribute.getFid(), attribute));

            List<Attribute> thisAttributeList = thisAttributes.getAttributeList();
            Map<Long, Attribute> thisAttributeMap = new HashMap<>();
            thisAttributeList.forEach(attribute -> thisAttributeMap.put(attribute.getFid(), attribute));
            // 遍历上一个版本的属性，判断是否删除、修改
            for (Map.Entry<Long, Attribute> entry : lastAttributeMap.entrySet()) {
                Long fid = entry.getKey();
                Attribute lastAttribute = entry.getValue();
                // 如果当前版本中没有，则表明该属性被删除
                Attribute thisAttribute = thisAttributeMap.get(fid);
                if (thisAttribute == null) {
                    operationAttribute(eVersion, lastAttribute, EActionEnum.DELETE);
                }
                // 如果当前版本中有，则判断该属性是否被修改
                else if (lastAttribute != null && lastAttribute.getValue() != null && thisAttribute.getValue() != null && !lastAttribute.getValue().equals(thisAttribute.getValue())){
                    operationAttribute(eVersion, thisAttribute, EActionEnum.MODIFY);
                }
            }

            // 遍历当前版本的属性，判断是否有新增
            for (Map.Entry<Long, Attribute> entry : thisAttributeMap.entrySet()) {
                Long fid = entry.getKey();
                Attribute thisAttribute = entry.getValue();
                // 如果上一版本中没有改属性，则表示新增属性
                Attribute lastAttribute = lastAttributeMap.get(fid);
                if (lastAttribute == null) {
                    operationAttribute(eVersion, thisAttribute, EActionEnum.ADDING);
                }
            }
        }
    }

    /**
     * 记录对象属性的变化
     * @param eVersion
     * @param attribute
     * @param eActionEnum
     */
    private static void operationAttribute(EVersion eVersion, Attribute attribute, EActionEnum eActionEnum) {
        Long attributeId = attribute.getFid();
        EAttribute eAttribute = new EAttribute(attribute);
        EAction eAction = new EAction();
        eAction.setId(attributeId);
        eAction.setOperation(new EActionEvent(eActionEnum, EActionEnum.ATTRIBUTE));

        eVersion.addAction(eAction);
        eVersion.addAttr(eAttribute);
    }
}
