package com.rb.odmg.Controller;


public class StockDialogData {
    private int articleId;
    private int initialQuantity;

    public StockDialogData(int articleId, int initialQuantity) {
        this.articleId = articleId;
        this.initialQuantity = initialQuantity;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getInitialQuantity() {
        return initialQuantity;
    }

    public void setInitialQuantity(int initialQuantity) {
        this.initialQuantity = initialQuantity;
    }
}
