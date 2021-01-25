package com.rb.odmg.Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Commodity {
    private SimpleIntegerProperty id;
    private SimpleStringProperty commodity;

    public Commodity() {
        this.id = new SimpleIntegerProperty();
        this.commodity = new SimpleStringProperty();
    }

    public int getId() {
        return id.get();
    }
    public void setId(int id) {
        this.id.set(id);
    }

    public String getCommodity() {
        return commodity.get();
    }

    public void setCommodity(String commodity) {
        this.commodity.set(commodity);
    }
}
