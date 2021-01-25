package com.rb.odmg.Model;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Article {

    private SimpleIntegerProperty id;
    private SimpleIntegerProperty commodityId;
    private SimpleStringProperty libelle;
    private SimpleFloatProperty price;
    private SimpleStringProperty dateCreated;

    public Article() {
        this.id = new SimpleIntegerProperty();
        this.commodityId = new SimpleIntegerProperty();
        this.libelle = new SimpleStringProperty();
        this.price = new SimpleFloatProperty();
        this.dateCreated = new SimpleStringProperty();
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getCommodityId() {
        return commodityId.get();
    }

    public void setCommodityId(int id) {
        this.commodityId.set(id);
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

    public String getDateCreated() {
        return dateCreated.get();
    }

    public void setDateCreated(String stringDate){
        this.dateCreated.set(stringDate);
    }
}