package com.rb.odmg.Controller;

import com.rb.odmg.Model.Client;

public class ClientDialogData {
    private String name;
    private String city;
    private String address;
    private String phone;
    private boolean newClient;
    private Client client;

    public ClientDialogData(String name, String city, String address, String phone) {
        this.name = name;
        this.city = city;
        this.address = address;
        this.phone = phone;
        this.newClient = true;
        this.client = null;
    }

    public ClientDialogData(Client client) {
        this.newClient = false;
        this.client = client;
    }

    public boolean isNewClient() {
        return newClient;
    }

    public void setNewClient(boolean newClient) {
        this.newClient = newClient;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
