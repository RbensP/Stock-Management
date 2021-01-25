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
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class OrderInfoController {
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
    private DialogPane orderInfoDialog;

    @FXML
    private TableView<Purchase> orderedArticlesTable;

    @FXML
    private Dialog<ButtonType> dialog;

    @FXML
    private Text total;

    private BorderPane main;

    private double _total;

    private DisplayOrder order;
    private ObservableList<Purchase> orderedArticles;

    public void initialize() { }

    public void setDialog(BorderPane main, Dialog<ButtonType> dialog){
        this.dialog = dialog;
        this.main = main;
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
    public void handleReturn() {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.initOwner(orderInfoDialog.getScene().getWindow());
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
        returnArticlesController.setData(orderedArticles, dlg, true);

        dlg.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dlg.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Button btn = (Button) dlg.getDialogPane().lookupButton(ButtonType.OK);
        btn.setVisible(false);

        dlg.setTitle("Retourner Article");
        Optional<ButtonType> res = dlg.showAndWait();
        if(res.isPresent() && res.get() == ButtonType.OK) {
            Returned returned = returnArticlesController.processResults();

            try {
                boolean success;
                if(returned.getQuantity() <= returned.getPurchase().getQuantity()){
                    success = Datasource.getInstance().updateOrderedArticle(order.getId(),
                            returned.getPurchase().getArticleId(),
                            returned.getPurchase().getQuantity() - returned.getQuantity(),
                            returned.getPurchase().getTotal() - Validator.truncateDouble((returned.getPurchase().getTotal()/returned.getPurchase().getQuantity())*returned.getQuantity()));

                    if (success){
                        ObservableList<Purchase> purchases = FXCollections.observableArrayList(Datasource.getInstance().queryOrderedArticlesByOrderId(order.getId()));
                        setOrderedArticlesData(purchases);

                        boolean result = Datasource.getInstance().updateStock(returned.getPurchase().getArticleId(), returned.getQuantity(), Datasource.UPDATE_STOCK_TO_ADD);
                        if(!result){
                            AlertHandler.show("Erreur lors de la mise à jour du stock", Alert.AlertType.ERROR);
                        }
                    }
                }
                else{
                    AlertHandler.show("La quantité entrée est supérieure!", Alert.AlertType.ERROR);
                }
            } catch (SQLException e){
                AlertHandler.show("Oupss! Mise à jour échouée", Alert.AlertType.ERROR);
            }

        }
    }

    @FXML
    public void handleDeliveryInfoClick(){
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.initOwner(orderInfoDialog.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/rb/odmg/View/deliveryInfoDialog.fxml"));

        try {
            dlg.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        DeliveryInfoController controller = fxmlLoader.<DeliveryInfoController>getController();
        controller.setOrderedArticlesData(orderedArticlesTable.getItems());
        controller.setOrderAndClientInfo(order);

        dlg.getDialogPane().getButtonTypes().add(ButtonType.OK);

        dlg.setTitle("Bon de Livraison");
        Optional<ButtonType> res = dlg.showAndWait();
    }
}
