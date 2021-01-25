package com.rb.odmg.Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Stock {
    private SimpleIntegerProperty id;
    private SimpleStringProperty article;
    private SimpleIntegerProperty initialQty;

    public Stock() {
        this.id = new SimpleIntegerProperty();
        this.article = new SimpleStringProperty();
        this.initialQty = new SimpleIntegerProperty();
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getArticle() {
        return article.get();
    }

    public void setArticle(String article) {
        this.article.set(article);
    }

    public long getInitialQty() {
        return initialQty.get();
    }

    public void setInitialQty(int qty) {
        this.initialQty.set(qty);
    }
}
