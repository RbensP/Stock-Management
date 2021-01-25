package com.rb.odmg.Model;

public class Purchase {
    private int articleId;
    private String libelle;
    private int quantity;
    private double total;

    public Purchase(int articleId, String libelle, int quantity, double total) {
        this.articleId = articleId;
        this.libelle = libelle;
        this.quantity = quantity;
        this.total = total;
    }

    public Purchase() {

    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
