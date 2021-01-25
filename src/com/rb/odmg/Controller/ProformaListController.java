package com.rb.odmg.Controller;

import com.rb.odmg.Model.Datasource;
import com.rb.odmg.Model.DisplayOrder;
import com.rb.odmg.Model.Purchase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class ProformaListController {
    @FXML
    private DialogPane proformaListDialog;

    @FXML
    private TableView<DisplayOrder> ordersTable;

    private Dialog<ButtonType> dialog;

    public void initialize(){
        listOrders();
    }

    public void listOrders(){
        Task<ObservableList<DisplayOrder>> proformaListTask = new GetAllNotValidatedOrdersTask();
        ordersTable.itemsProperty().bind(proformaListTask.valueProperty());

        new Thread(proformaListTask).start();
    }

    public void setData(Dialog<ButtonType> dialog){
//        ordersTable.setItems(list);
        this.dialog = dialog;
    }

    @FXML
    public void handleClick(){
        final DisplayOrder displayOrder = ordersTable.getSelectionModel().getSelectedItem();

        if(displayOrder != null){
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(proformaListDialog.getScene().getWindow());
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/rb/odmg/View/proformaInfo.fxml"));

            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());
            } catch(IOException e) {
                System.out.println("Couldn't load the dialog");
                e.printStackTrace();
                return;
            }

            ProformaController controller = fxmlLoader.<ProformaController>getController();
            controller.setDialog(dialog);

            Task<ObservableList<Purchase>> orderedArticlesTask = new AllOrderedArticlesByOrderIdTask(displayOrder.getId());
            orderedArticlesTask.setOnSucceeded(e -> {
                controller.setOrderedArticlesData(orderedArticlesTask.getValue());
            });
            new Thread(orderedArticlesTask).start();

            Service<DisplayOrder> srv = new Service<>(){
                @Override
                protected Task<DisplayOrder> createTask(){
                    return new Task<>() {
                        @Override
                        protected DisplayOrder call() throws Exception {
                            return Datasource.getInstance().queryOrderByIdWithClient(displayOrder.getId());
                        }
                    };
                }
            };
            srv.setOnSucceeded(new EventHandler<>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    System.out.println("Done query display order");
                    controller.setOrderAndClientInfo(srv.getValue());
                }
            });
            srv.restart();

            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

            dialog.setTitle("Proforma");
            Optional<ButtonType> result = dialog.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                System.out.println("OK pressed");
                listOrders();
//                AlertHandler.show("Commande validée", Alert.AlertType.INFORMATION);
            } else{
                System.out.println("Cancel pressed");

            }
        }
    }
}

class AllOrderedArticlesByOrderIdTask extends Task {
    private int orderId;

    public AllOrderedArticlesByOrderIdTask(int orderId){
        this.orderId = orderId;
    }

    @Override
    public ObservableList<Purchase> call() throws SQLException {
        System.out.println("calling OrderedArticles");
        return FXCollections.observableArrayList(Datasource.getInstance().queryOrderedArticlesByOrderId(this.orderId));
    }
}

class GetAllNotValidatedOrdersTask extends Task {

    @Override
    public ObservableList<DisplayOrder> call()  {
        return FXCollections.observableArrayList(Datasource.getInstance().queryNotValidatedOrdersWithClients(Datasource.ORDER_BY_ASC));
    }
}