package model;

public class CustomerGeom {

    private String id;

    private CustomerData data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CustomerData getData() {
        return data;
    }

    public void setData(CustomerData data) {
        this.data = data;
    }

    public static class CustomerData {
        private String geotype;
        private Object coordinates;

        public CustomerData() {

        }

        public String getGeotype() {
            return geotype;
        }

        public void setGeotype(String geotype) {
            this.geotype = geotype;
        }

        public Object getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(Object coordinates) {
            this.coordinates = coordinates;
        }
    }
}
