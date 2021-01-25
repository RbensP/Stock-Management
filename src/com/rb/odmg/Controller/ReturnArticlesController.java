package com.rb.odmg.Controller;

import com.rb.odmg.Model.Purchase;
import com.rb.odmg.utils.Validator;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

public class ReturnArticlesController {
    @FXML
    private ComboBox<Purchase> purchasesComb;

    @FXML
    private TextField quantity;

    private Dialog<ButtonType> dialog;

    private boolean isValidated;

    @FXML
    public void setData(ObservableList<Purchase> purchases, Dialog<ButtonType> dialog, boolean validated){
        purchasesComb.setItems(purchases);
        purchasesComb.getSelectionModel().selectFirst();
        purchasesComb.setConverter(new StringConverter<Purchase>() {
            @Override
            public String toString(Purchase purchase) {
                return purchase.getLibelle();
            }

            @Override
            public Purchase fromString(String s) {
                return null;
            }
        });

        this.dialog = dialog;
        this.isValidated = validated;
    }

    @FXML
    public Returned processResults(){
        Purchase purchase = purchasesComb.getSelectionModel().getSelectedItem();
        int qty = Integer.parseInt(quantity.getText().trim());

        return new Returned(purchase, qty);
    }

    @FXML
    public void handleRegisterClick(){
        if(Validator.isInteger(quantity.getText().trim())) {
            if(this.isValidated){
                if(Integer.parseInt(quantity.getText().trim()) <= purchasesComb.getSelectionModel().getSelectedItem().getQuantity()){
                    ((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).fire();
                    System.out.println("Ok");
                }
                else {
                    AlertHandler.show("Quantité doit être inférieure ou égale à la valeur initiale", Alert.AlertType.WARNING);
                }
            }
            else{
                ((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).fire();
                System.out.println("Ok");
            }
        }
        else {
            AlertHandler.show("Quantité doit être une valeur entiere", Alert.AlertType.WARNING);
        }
    }
}
