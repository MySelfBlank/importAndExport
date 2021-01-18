package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import onegis.psde.util.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 节点
 */
public class ERNode extends ERObject{
    private Long id;

    /**
     * 引用的对象 【即关系网络中的端点】
     */
    private EObase refObject;

    /**
     * 关系边
     */
    private EREdge edge;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Object> nodeProperties;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Properties> properties = new ArrayList<>();

    public class Properties{
        private String key;
        private Object value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

    public ERNode() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EObase getRefObject() {
        return refObject;
    }

    public void setRefObject(EObase refObject) {
        this.refObject = refObject;
    }

    public EREdge getEdge() {
        return edge;
    }

    public void setEdge(EREdge edge) {
        this.edge = edge;
    }

    public Map<String, Object> getNodeProperties() {
        return nodeProperties;
    }

    public void setNodeProperties(Map<String, Object> nodeProperties) {
        this.nodeProperties = nodeProperties;
    }

    public List<Properties> getProperties() {
        return properties;
    }

    public void setProperties(List<Properties> properties) {
        this.properties = properties;
    }

}
