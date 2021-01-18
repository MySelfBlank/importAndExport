package com.yzh.dao.exportModel;

/**
 * 行为
 */
public class EAction {
    private Long id;
    private EActionEvent operation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EActionEvent getOperation() {
        return operation;
    }

    public void setOperation(EActionEvent operation) {
        this.operation = operation;
    }
}
