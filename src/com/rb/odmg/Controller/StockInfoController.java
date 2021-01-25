package com.rb.odmg.Controller;

import com.rb.odmg.Model.Stock;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class StockInfoController {
    @FXML
    private Text id;

    @FXML
    private Text article;

    @FXML
    private Text initialQty;

    @FXML
    public void setData(Stock stock){
        id.setText("" + stock.getId());
        article.setText(stock.getArticle());
        initialQty.setText("" + stock.getInitialQty());
    }
}
