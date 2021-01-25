package com.rb.odmg.Model;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class DisplayArticle {

    private SimpleIntegerProperty id;
    private SimpleIntegerProperty quantity;
    private SimpleStringProperty commodity;
    private SimpleStringProperty libelle;
    private SimpleFloatProperty price;
    private SimpleStringProperty dateCreated;

    public DisplayArticle() {
        this.id = new SimpleIntegerProperty();
        this.quantity = new SimpleIntegerProperty();
        this.libelle = new SimpleStringProperty();
        this.price = new SimpleFloatProperty();
        this.dateCreated = new SimpleStringProperty();
        this.commodity = new SimpleStringProperty();
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int qty) {
        this.quantity.set(qty);
    }

    public float getPrice() {
        return price.get();
    }

    public void setPrice(float price) {
        this.price.set(price);
    }

    public String getLibelle() {
        return libelle.get();
    }

    public void setLibelle(String name) {
        this.libelle.set(name);
    }

    public String getCommodity() {
        return commodity.get();
    }

    public void setCommodity(String commodity) {
        this.commodity.set(commodity);
    }

    public String getDateCreated() {
        return dateCreated.get();
    }

    public void setDateCreated(String stringDate){
        this.dateCreated.set(stringDate);
    }
}