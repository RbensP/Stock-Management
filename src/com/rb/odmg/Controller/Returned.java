package com.rb.odmg.Controller;

import com.rb.odmg.Model.Purchase;

public class Returned {
    private Purchase purchase;
    private int quantity;

    public Returned(Purchase purchase, int quantity) {
        this.purchase = purchase;
        this.quantity = quantity;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
