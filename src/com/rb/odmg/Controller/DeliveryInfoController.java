package com.rb.odmg.Controller;

import com.rb.odmg.Model.Datasource;
import com.rb.odmg.Model.DisplayOrder;
import com.rb.odmg.Model.Purchase;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class DeliveryInfoController {
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
    private Text state;

    @FXML
    private DialogPane deliveryInfoDialog;

    @FXML
    private TableView<Purchase> orderedArticlesTable;

    @FXML
    private Text total;

    private int _total;

    private DisplayOrder order;

    private ObservableList<Purchase> orderedArticles;

    @FXML
    public void setOrderAndClientInfo(DisplayOrder order){
        try {
            DisplayOrder displayOrder = Datasource.getInstance().queryOrderByIdWithClient(order.getId());

            if(displayOrder.getDeliveryState() != null){
                state.setText(displayOrder.getDeliveryState());
            }
            else {
                state.setText("---");
            }
            if(displayOrder.getDeliveredDate() != null){
                date.setText(displayOrder.getDeliveredDate());
            }
            else {
                date.setText("---");
            }
            client_name.setText(displayOrder.getClientName());
            client_city.setText(displayOrder.getClientCity());
            client_address.setText(displayOrder.getClientAddress());
            client_phone.setText(displayOrder.getClientPhone());

            this.order = order;
        } catch (SQLException e){
            AlertHandler.show("Oupss! Une erreur s'est produite\n\nReessayez", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void setOrderedArticlesData(ObservableList<Purchase> orderedArticles){
        System.out.println("adding OrderedArticles");

        this.orderedArticles = orderedArticles;
        orderedArticlesTable.setItems(orderedArticles);

        this._total = 0;
        orderedArticles.forEach(o -> setTotal(o));
        total.setText("Livraison de " + this._total + " Articles");
    }

    @FXML
    public void setTotal(Purchase orderedArticle){
        this._total += orderedArticle.getQuantity();
    }

    @FXML
    public void handleEditState() {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.initOwner(deliveryInfoDialog.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/rb/odmg/View/editDeliveryState.fxml"));

        try {
            dlg.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        EditDeliveryController controller = fxmlLoader.<EditDeliveryController>getController();
        controller.setData(order, dlg);

        dlg.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dlg.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Button btn = (Button) dlg.getDialogPane().lookupButton(ButtonType.OK);
        btn.setVisible(false);

        dlg.setTitle("Modifier Etat livraison");
        Optional<ButtonType> res = dlg.showAndWait();

        if(res.isPresent() && res.get() == ButtonType.OK) {
            EditDelStateData data = controller.processResults();

            try {
                boolean success = Datasource.getInstance().updateOrderDeliveryState(order.getId(), data.getState(), data.getDate());

                if (success){
                    DisplayOrder displayOrder = Datasource.getInstance().queryOrderByIdWithClient(order.getId());
                    setOrderAndClientInfo(displayOrder);
                    this.order = displayOrder;
                }
                else {
                    AlertHandler.show("Erreur lors de la mise à jour\n\nReessayez!", Alert.AlertType.ERROR);
                }
            } catch (SQLException e){
                AlertHandler.show("Oupss! Mise à jour échouée", Alert.AlertType.ERROR);
            }
        }
    }
}
