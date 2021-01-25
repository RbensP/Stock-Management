package com.rb.odmg.Controller;

import com.rb.odmg.Model.Datasource;
import com.rb.odmg.Model.DisplayOrder;
import com.rb.odmg.Model.Purchase;
import com.rb.odmg.utils.Validator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class ProformaController {
    @FXML
    private Text client_name;

    @FXML
    private Text client_city;

    @FXML
    private Text client_address;

    @FXML
    private Text client_phone;

    @FXML
    private Text date;

    @FXML
    private DialogPane proformaInfoDialog;

    @FXML
    private TableView<Purchase> orderedArticlesTable;

    @FXML
    private Dialog<ButtonType> dialog;

    @FXML
    private Text total;

    private double _total;

    private DisplayOrder order;
    private ObservableList<Purchase> orderedArticles;

    public void initialize() { }

    public void setDialog(Dialog<ButtonType> dialog){
        this.dialog = dialog;
    }

    @FXML
    public void setOrderAndClientInfo(DisplayOrder displayOrder){
        date.setText(displayOrder.getDateCreated());
        client_name.setText(displayOrder.getClientName());
        client_city.setText(displayOrder.getClientCity());
        client_address.setText(displayOrder.getClientAddress());
        client_phone.setText(displayOrder.getClientPhone());

        this.order = displayOrder;
    }

    @FXML
    public void setOrderedArticlesData(ObservableList<Purchase> orderedArticles){
        System.out.println("adding OrderedArticles");

        this.orderedArticles = orderedArticles;
        orderedArticlesTable.setItems(orderedArticles);

        this._total = 0;
        orderedArticles.forEach(o -> setTotal(o));
        total.setText("" + Validator.truncateDouble(this._total) + " HTG");
    }

    @FXML
    public void setTotal(Purchase orderedArticle){
        this._total += orderedArticle.getTotal();
    }

    @FXML
    public void handleEditClick() {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.initOwner(proformaInfoDialog.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/rb/odmg/View/returnArticles.fxml"));

        try {
            dlg.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        ReturnArticlesController returnArticlesController = fxmlLoader.<ReturnArticlesController>getController();
        returnArticlesController.setData(orderedArticles, dlg, false);

        dlg.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dlg.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Button btn = (Button) dlg.getDialogPane().lookupButton(ButtonType.OK);
        btn.setVisible(false);

        dlg.setTitle("Modifier Proforma");
        Optional<ButtonType> res = dlg.showAndWait();
        if(res.isPresent() && res.get() == ButtonType.OK) {
            Returned returned = returnArticlesController.processResults();

            try {
                boolean success = Datasource.getInstance().updateOrderedArticle(order.getId(), returned.getPurchase().getArticleId(), returned.getQuantity(),
                        Validator.truncateDouble((returned.getPurchase().getTotal()/returned.getPurchase().getQuantity())*returned.getQuantity()));

                if (success){
                    ObservableList<Purchase> purchases = FXCollections.observableArrayList(Datasource.getInstance().queryOrderedArticlesByOrderId(order.getId()));
                    setOrderedArticlesData(purchases);
                }
            } catch (SQLException e){
                AlertHandler.show("Oupss! Mise à jour échouée", Alert.AlertType.ERROR);
            }

        }
    }

    @FXML
    public void handleRegisterOrderClick(){
        try{
            boolean success = Datasource.getInstance().validateOrder(order.getId());
            if (success){
                orderedArticles.forEach(purchase -> {
                    try {
                        Datasource.getInstance().updateStock(purchase.getArticleId(),purchase.getQuantity(), Datasource.UPDATE_STOCK_TO_SUBTRACT);
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                });
                ((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).fire();
            }
            else {
                AlertHandler.show("Erreur lors de la mise à jour\n\nReessayez", Alert.AlertType.ERROR);
            }
        } catch (SQLException e){
            AlertHandler.show("Erreur lors de la mise à jour\n\nReessayez", Alert.AlertType.ERROR);
        }
    }
}
