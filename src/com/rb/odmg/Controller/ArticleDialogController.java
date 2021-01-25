package com.rb.odmg.Controller;

import com.rb.odmg.Model.Commodity;
import com.rb.odmg.Model.Stock;
import com.rb.odmg.utils.Validator;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

public class ArticleDialogController {
    @FXML
    private Dialog<ButtonType> articleDialog;

    @FXML
    private TextField libelleField;

    @FXML
    private ComboBox<Stock> stockField;

    @FXML
    private ComboBox<Commodity> commodityField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField initQtyField;

    @FXML
    public void setData(ObservableList<Commodity> commodities, Dialog<ButtonType> dialog){
        commodityField.setItems(commodities);
        commodityField.getSelectionModel().selectFirst();
        commodityField.setConverter(new StringConverter<Commodity>() {
            @Override
            public String toString(Commodity o) {
                return o.getCommodity();
            }

            @Override
            public Commodity fromString(String s) {
                return null;
            }
        });

        this.articleDialog = dialog;
    }

    @FXML
    public ArticleDialogData processResults() {
        String libelle = libelleField.getText().trim();
        Commodity commodity = commodityField.getSelectionModel().getSelectedItem();
        float price = Float.parseFloat(priceField.getText().trim());
        int initQty = Integer.parseInt(initQtyField.getText().trim());

        return new ArticleDialogData(commodity.getId(), libelle, Validator.truncateFloat(price), initQty);
    }

    @FXML
    public void handleRegisterClick(){
        String libelle = libelleField.getText().trim();
        String initialQuantity = initQtyField.getText().trim();
        String price = priceField.getText().trim();

        if(Validator.isInteger(initialQuantity) && Validator.isDecimal(price) && !Validator.isEmpty(libelle) && Float.parseFloat(price)>0){
            ((Button) articleDialog.getDialogPane().lookupButton(ButtonType.OK)).fire();
            System.out.println("Ok");
        }
        else {
            AlertHandler.show("Libelle doit être non vide\n\nQuantité doit être une valeur entière\n\nPrix doit être un décimal non nul",
                        Alert.AlertType.WARNING);
        }
    }
}
