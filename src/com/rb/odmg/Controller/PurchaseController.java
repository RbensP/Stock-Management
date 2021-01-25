package com.rb.odmg.Controller;

import com.rb.odmg.Model.Purchase;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

public class PurchaseController {
    @FXML
    private Text articleId;

    @FXML
    private Text quantity;

    @FXML
    private Text libelle;

    @FXML
    private Text total;

    @FXML
    private Button deleteBtn;

    public void setData(Purchase purchase, TableView<Purchase> purchaseTable, Dialog<ButtonType> dialog, Text totalField){
        articleId.setText("" + purchase.getArticleId());
        libelle.setText(purchase.getLibelle());
        quantity.setText("" + purchase.getQuantity());
        total.setText("" + purchase.getTotal());

        deleteBtn.setOnMouseClicked(e -> {
            dialog.close();
            purchaseTable.getItems().remove(purchase);

            double totalCost = 0;
            for(int pos = 0; pos < purchaseTable.getItems().size(); pos++){
                Purchase purchasePos = purchaseTable.getItems().get(pos);
                totalCost += purchasePos.getTotal();
            }
            totalField.setText("" + totalCost + " HTG");
        });
    }
}
