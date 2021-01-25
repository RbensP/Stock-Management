package com.rb.odmg.Controller;

import com.rb.odmg.Model.Purchase;
import javafx.scene.control.TableView;

public class OrderDialogData {
    private TableView<Purchase> orderedArticles;
    private boolean validated;

    public OrderDialogData(TableView<Purchase> orderedArticles, boolean validated) {
        this.orderedArticles = orderedArticles;
        this.validated = validated;
    }

    public TableView<Purchase> getOrderedArticles() {
        return orderedArticles;
    }

    public void setOrderedArticles(TableView<Purchase> orderedArticles) {
        this.orderedArticles = orderedArticles;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }
}
