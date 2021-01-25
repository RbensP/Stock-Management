package com.rb.odmg.Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class DisplayOrder {
    private SimpleIntegerProperty id;
    private SimpleStringProperty dateCreated;
    private SimpleStringProperty deliveryState;
    private SimpleStringProperty deliveredDate;
    private SimpleStringProperty clientName;
    private SimpleStringProperty clientCity;
    private SimpleStringProperty clientAddress;
    private SimpleStringProperty clientPhone;

    public DisplayOrder() {
        this.id = new SimpleIntegerProperty();
        this.dateCreated = new SimpleStringProperty();
        this.deliveryState = new SimpleStringProperty();
        this.deliveredDate = new SimpleStringProperty();
        this.clientName = new SimpleStringProperty();
        this.clientCity = new SimpleStringProperty();
        this.clientAddress = new SimpleStringProperty();
        this.clientPhone = new SimpleStringProperty();
    }

    public String getDeliveryState() {
        return deliveryState.get();
    }

    public void setDeliveryState(String deliveryState) {
        this.deliveryState.set(deliveryState);
    }

    public String getDeliveredDate() {
        return deliveredDate.get();
    }

    public void setDeliveredDate(String deliveredDate) {
        this.deliveredDate.set(deliveredDate);
    }

    public String getClientPhone() {
        return clientPhone.get();
    }

    public SimpleStringProperty clientPhoneProperty() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone.set(clientPhone);
    }

    public String getClientCity() {
        return clientCity.get();
    }

    public SimpleStringProperty clientCityProperty() {
        return clientCity;
    }

    public void setClientCity(String clientCity) {
        this.clientCity.set(clientCity);
    }

    public int getId() {
        return id.get();
    }

    public String getClientAddress() {
        return clientAddress.get();
    }

    public SimpleStringProperty clientAddressProperty() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress.set(clientAddress);
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getDateCreated() {
        return this.dateCreated.get();
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated.set(dateCreated);
    }

    public String getClientName() {
        return this.clientName.get();
    }

    public void setClientName(String libelle) {
        this.clientName.set(libelle);
    }
}
