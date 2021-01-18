package com.yzh.dao.exportModel;


import com.yzh.dao.enums.EActionEnum;

/**
 * 具体行为事件
 */
public class EActionEvent {

    /**
     * 操作方式【增删改】
     */
    private String actionOperationType;

    /**
     * 对象属性标记【八个方面】
     */
    private String objectOperationType;

    public EActionEvent() {

    }

    public EActionEvent(EActionEnum operationType, EActionEnum objectType) {
        this.actionOperationType = operationType.getName();
        this.objectOperationType = objectType.getName();
    }

    public void createActionOperationType(Integer value){
        this.actionOperationType = EActionEnum.getEnum(value).getName();
    }

    public void createObjectOperationType(Integer value){
        this.objectOperationType = EActionEnum.getEnum(value).getName();
    }

    public String getActionOperationType() {
        return actionOperationType;
    }

    public void setActionOperationType(String actionOperationType) {
        this.actionOperationType = actionOperationType;
    }

    public String getObjectOperationType() {
        return objectOperationType;
    }

    public void setObjectOperationType(String objectOperationType) {
        this.objectOperationType = objectOperationType;
    }
}
