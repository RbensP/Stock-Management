package com.rb.odmg.Controller;

import com.rb.odmg.Model.Article;
import com.rb.odmg.utils.Validator;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

public class StockDialogController {
    @FXML
    private Dialog<ButtonType> stockDialog;

    @FXML
    private ComboBox<Article> articleField;

    @FXML
    private TextField initialQuantityField;

    @FXML
    public void setData(ObservableList<Article> articles, Dialog<ButtonType> dialog){
        articleField.setItems(articles);
        articleField.getSelectionModel().selectFirst();
        articleField.setConverter(new StringConverter<Article>() {
            @Override
            public String toString(Article o) {
                return o.getLibelle();
            }

            @Override
            public Article fromString(String s) {
                return null;
            }
        });

        this.stockDialog = dialog;
    }

    @FXML
    public StockDialogData processResults() {
        Article article = articleField.getSelectionModel().getSelectedItem();
        int quantity = Integer.parseInt(initialQuantityField.getText().trim());

        return new StockDialogData(article.getId(), quantity);
    }

    @FXML
    public void handleRegisterClick(){
        String initialQuantity = initialQuantityField.getText().trim();

        if(Validator.isInteger(initialQuantity)){
            ((Button) stockDialog.getDialogPane().lookupButton(ButtonType.OK)).fire();
        }
        else {
            AlertHandler.show("Quantité doit être une valeur entière!", Alert.AlertType.WARNING);
        }
    }
}
