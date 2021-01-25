package com.rb.odmg.Controller;

import com.rb.odmg.Model.Datasource;
import com.rb.odmg.Model.Result;
import com.rb.odmg.utils.Validator;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class Login {
    private Stage primaryStage;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    public void handleConnect() {
        if(!Validator.isEmpty(username.getText()) && !Validator.isEmpty(password.getText())){
            Service<Result> srv = new Service<>(){
                @Override
                protected Task<Result> createTask(){
                    return new Task<>() {
                        @Override
                        protected Result call() throws Exception {
                            return Datasource.getInstance().authenticate(username.getText(), password.getText());
                        }
                    };
                }
            };

            srv.setOnSucceeded(new EventHandler<>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    if(srv.getValue().isSuccess()) {

                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/com/rb/odmg/mainWindow.fxml"));
                        Parent root = null;
                        try {
                            root = loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Controller controller = loader.getController();

                        controller.setData(primaryStage);
                        controller.listArticles();
                        controller.listCommodities();
                        controller.listOrders();

                        primaryStage.setTitle("Gestionnaire de Commande");
                        primaryStage.setScene(new Scene(root, 1000, 650));
                        progressBar.setVisible(false);

                        primaryStage.show();
                    }
                    else {
                        progressBar.setVisible(false);
                        AlertHandler.show(srv.getValue().getErrMsg(), Alert.AlertType.ERROR);
                    }
                }
            });
            srv.setOnFailed(e -> {
                AlertHandler.show(srv.getValue().getErrMsg(), Alert.AlertType.ERROR);
            });

            progressBar.progressProperty().bind(srv.progressProperty());
            progressBar.setVisible(true);

            srv.restart();
        }
    }

    public void setData(Stage stage){
        this.primaryStage = stage;
    }
}
