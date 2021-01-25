package com.rb.odmg.Model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class OrderedArticle {
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty articleId;
    private SimpleIntegerProperty quantitty;
    private SimpleDoubleProperty total;

    public OrderedArticle(){
        this.id = new SimpleIntegerProperty();
        this.articleId = new SimpleIntegerProperty();
        this.quantitty = new SimpleIntegerProperty();
        this.total = new SimpleDoubleProperty();
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

    public int getArticleId() {
        return articleId.get();
    }

    public SimpleIntegerProperty articleIdProperty() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId.set(articleId);
    }

    public int getQuantitty() {
        return quantitty.get();
    }

    public SimpleIntegerProperty quantittyProperty() {
        return quantitty;
    }

    public void setQuantitty(int quantitty) {
        this.quantitty.set(quantitty);
    }

    public double getTotal() {
        return total.get();
    }

    public SimpleDoubleProperty totalProperty() {
        return total;
    }

    public void setTotal(double total) {
        this.total.set(total);
    }
}
