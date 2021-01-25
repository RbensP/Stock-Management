package com.rb.odmg.Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Client {
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty city;
    private SimpleStringProperty address;
    private SimpleStringProperty phone;

    public Client() {
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.city = new SimpleStringProperty();
        this.address = new SimpleStringProperty();
        this.phone = new SimpleStringProperty();
    }

    public String getCity() {
        return city.get();
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }
}
