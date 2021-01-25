package com.rb.odmg.Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Order {
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty clientId;
    private SimpleStringProperty dateCreated;

    public Order() {
        this.id = new SimpleIntegerProperty();
        this.clientId = new SimpleIntegerProperty();
        this.dateCreated = new SimpleStringProperty();
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getClientId() {
        return clientId.get();
    }

    public void setClientId(int id) {
        this.clientId.set(id);
    }

    public String getDateCreated() {
        return this.dateCreated.get();
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated.set(dateCreated);
    }
}
