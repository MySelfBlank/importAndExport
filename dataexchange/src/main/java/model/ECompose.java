package model;

import java.util.List;

/**
 * 组成结构
 */
public class ECompose extends AbstractObject{

    /**
     * 描述
     */
    private String des;

    /**
     * 组成结构中的元素
     */
    private List<EComposeElement> elements;
    /**
     * 记录是否为强关联
     */
    private Boolean strong = false;

    public void addElement(EComposeElement element){
        this.elements.add(element);
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public List<EComposeElement> getElements() {
        return elements;
    }

    public void setElements(List<EComposeElement> elements) {
        this.elements = elements;
    }

    public Boolean getStrong() {
        return strong;
    }

    public void setStrong(Boolean strong) {
        this.strong = strong;
    }
}
