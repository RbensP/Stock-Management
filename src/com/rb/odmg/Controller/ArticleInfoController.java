package com.rb.odmg.Controller;

import com.rb.odmg.Model.DisplayArticle;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class ArticleInfoController {
    @FXML
    private Text quantity;

    @FXML
    private Text libelle;

    @FXML
    private Text commodity;

    @FXML
    private Text price;

    @FXML
    private Text date;

    @FXML
    public void setData(DisplayArticle article){
        quantity.setText("" + article.getQuantity());
        commodity.setText("" + article.getCommodity());
        libelle.setText(article.getLibelle());
        price.setText("" + article.getPrice() + " HTG");
        date.setText("" + article.getDateCreated());
    }
}
