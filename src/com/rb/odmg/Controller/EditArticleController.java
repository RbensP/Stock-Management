package com.rb.odmg.Controller;

import com.rb.odmg.Model.Commodity;
import com.rb.odmg.Model.Datasource;
import com.rb.odmg.Model.DisplayArticle;
import com.rb.odmg.utils.Validator;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.sql.SQLException;

public class EditArticleController {
    @FXML
    private Dialog<ButtonType> dialog;

    @FXML
    private TextField libelleField;

    @FXML
    private ComboBox<Commodity> commodityField;

    @FXML
    private TextField priceField;

    @FXML
    public void setTextFields(DisplayArticle article) {
        libelleField.setText(article.getLibelle());
        priceField.setText("" + article.getPrice());
    }

    @FXML
    public void setData(ObservableList<Commodity> commodities, DisplayArticle article, Dialog<ButtonType> editDialog) throws SQLException {
        commodityField.setItems(commodities);
        commodityField.setConverter(new StringConverter<>() {
            @Override
            public String toString(Commodity o) {
                return o.getCommodity();
            }

            @Override
            public Commodity fromString(String s) {
                return null;
            }
        });
        commodityField.getSelectionModel().select(Datasource.getInstance().queryCommodity(article.getCommodity()));

        this.dialog = editDialog;
    }

    @FXML
    public ArticleDialogData processResults(DisplayArticle displayArticle) {
        String libelle = libelleField.getText().trim();
        Commodity commodity = commodityField.getSelectionModel().getSelectedItem();
        float price = Float.parseFloat(priceField.getText().trim());

        return new ArticleDialogData(commodity.getId(), libelle, Validator.truncateFloat(price), displayArticle.getQuantity());
    }

    @FXML
    public void handleRegisterClick(){
        String libelle = libelleField.getText().trim();
        String price = priceField.getText().trim();

        if(Validator.isDecimal(price) && !Validator.isEmpty(libelle) && Float.parseFloat(priceField.getText())>0){
            ((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).fire();
            System.out.println("Ok");
        }
        else {
            AlertHandler.show("Libelle doit être non vide\n\nPrix doit être un décimal non nul",
                    Alert.AlertType.WARNING);
        }
    }
}
