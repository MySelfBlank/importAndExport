package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import onegis.common.utils.IdMakerUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomerGeomSerialize extends JsonSerializer<CustomerGeom> {
    @Override
    public void serialize(CustomerGeom customerGeom, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        if (customerGeom == null) {
            jsonGenerator.writeObject(null);
        }
        CustomerGeom.CustomerData customerData = customerGeom.getData();

        if (customerData != null) {
            String type = customerData.getGeotype();
            GeomSerialize geomSerialize = new GeomSerialize();
            geomSerialize.setId(Long.valueOf(customerGeom.getId()));
            // 点
            if ("point".equals(type)) {
                geomSerialize.setType("node");
                geomSerialize.setFlag(1);
                List<Double> coordinates = objectMapper.readValue(customerData.getCoordinates().toString(), new TypeReference<List<Double>>(){});
                geomSerialize.setX(coordinates.get(0));
                geomSerialize.setY(coordinates.get(1));
                geomSerialize.setZ(0.00);
                geomSerialize.setNodes(null);
                geomSerialize.setType1("Node");
            } else{
                geomSerialize.setType("way");
                geomSerialize.setFlag(1);
                geomSerialize.setX(null);
                geomSerialize.setY(null);
                geomSerialize.setZ(null);
                geomSerialize.setType1("Way");
                List<List<Double>> coordinates= new ArrayList<>();
                try {
                    coordinates = objectMapper.readValue(customerData.getCoordinates().toString(), new TypeReference<List<List<Double>>>(){});
                } catch (Exception e) {
                    try {
                        String json = customerData.getCoordinates().toString().substring(1, customerData.getCoordinates().toString().length() -1);
                        json = json.replace("[[[", "[[").replace("]]]", "]]");
                        coordinates = objectMapper.readValue(json, new TypeReference<List<List<Double>>>(){});
                    } catch (Exception ex) {
                        System.out.println("form转换失败");
                    }
                }
                List<Node> nodes = new ArrayList<>();
                Long firstId = 0L;
                for (int i=0; i<coordinates.size(); i++) {
                    List<Double> list = coordinates.get(i);
                    if (list == null || list.size() < 2) {
                        continue;
                    }
                    Node node = new Node();
                    node.setFlag(1);
                    Long id = new IdMakerUtils().nextId();
                    node.setId(id);
                    if (i == 0) {
                        firstId = id;
                    }

                    if ("polygon".equals(type) && i == coordinates.size() -1) {
                        node.setId(firstId);
                    }

                    node.setX(list.get(0));
                    node.setY(list.get(1));
                    node.setZ(0.00);
                    if (list.size() >= 3) {
                        node.setZ(list.get(2));
                    }

                    node.setType("node");
                    node.setType1("Node");
                    if (list.size() > 2) {
                        node.setZ(list.get(3));
                    }
                    nodes.add(node);
                }

                geomSerialize.setNodes(nodes);
            }
            jsonGenerator.writeObject(geomSerialize);
        } else {
            jsonGenerator.writeObject(null);
        }
    }

    public static class GeomSerialize{
        private Long id;
        private String type;
        private Integer flag;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Double x;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Double y;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Double z;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<Node> nodes;
        @JsonProperty("@type")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String type1;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Integer getFlag() {
            return flag;
        }

        public void setFlag(Integer flag) {
            this.flag = flag;
        }

        public Double getX() {
            return x;
        }

        public void setX(Double x) {
            this.x = x;
        }

        public Double getY() {
            return y;
        }

        public void setY(Double y) {
            this.y = y;
        }

        public Double getZ() {
            return z;
        }

        public void setZ(Double z) {
            this.z = z;
        }

        public List<Node> getNodes() {
            return nodes;
        }

        public void setNodes(List<Node> nodes) {
            this.nodes = nodes;
        }

        public String getType1() {
            return type1;
        }

        public void setType1(String type1) {
            this.type1 = type1;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    public static class Node{
        private Long id;
        private Integer flag;
        private String type;
        private Double x;
        private Double y;
        private Double z;
        @JsonProperty("@type")
        private String type1;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Integer getFlag() {
            return flag;
        }

        public void setFlag(Integer flag) {
            this.flag = flag;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Double getX() {
            return x;
        }

        public void setX(Double x) {
            this.x = x;
        }

        public Double getY() {
            return y;
        }

        public void setY(Double y) {
            this.y = y;
        }

        public Double getZ() {
            return z;
        }

        public void setZ(Double z) {
            this.z = z;
        }

        public String getType1() {
            return type1;
        }

        public void setType1(String type1) {
            this.type1 = type1;
        }
    }

}
