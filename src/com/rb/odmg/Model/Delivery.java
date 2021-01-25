package com.rb.odmg.Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Delivery {
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty orderId;
    private SimpleIntegerProperty clientId;
    private SimpleStringProperty date;
    private SimpleStringProperty state;

    public Delivery() {
        this.id = new SimpleIntegerProperty();
        this.orderId = new SimpleIntegerProperty();
        this.date = new SimpleStringProperty();
        this.state = new SimpleStringProperty();
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getOrderId() {
        return orderId.get();
    }

    public SimpleIntegerProperty orderIdProperty() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId.set(orderId);
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getState() {
        return state.get();
    }

    public SimpleStringProperty stateProperty() {
        return state;
    }

    public void setState(String state) {
        this.state.set(state);
    }
}
