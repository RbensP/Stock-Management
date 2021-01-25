package com.rb.odmg.Controller;

public class ArticleDialogData {
    private String libelle;
    private int commodityId;
    private int initStockQty;
    private float price;

    public ArticleDialogData(int commodityId, String libelle, float price, int initStockQty){
        this.libelle = libelle;
        this.commodityId = commodityId;
        this.price = price;
        this.initStockQty = initStockQty;
    }

    public int getInitStockQty() {
        return initStockQty;
    }

    public void setInitStockQty(int initStockQty) {
        this.initStockQty = initStockQty;
    }

    public int getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(int commodityId) {
        this.commodityId = commodityId;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
