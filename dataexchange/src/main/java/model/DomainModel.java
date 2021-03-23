package model;

import javafx.beans.property.SimpleStringProperty;

public class DomainModel {
    private final SimpleStringProperty name;
    private final SimpleStringProperty id;
    private final SimpleStringProperty index;

    public DomainModel(String name, String id, String index) {
        this.name = new SimpleStringProperty(name);
        this.id = new SimpleStringProperty(id);
        this.index = new SimpleStringProperty(index);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getIndex() {
        return index.get();
    }

    public SimpleStringProperty indexProperty() {
        return index;
    }

    public void setIndex(String index) {
        this.index.set(index);
    }
}
