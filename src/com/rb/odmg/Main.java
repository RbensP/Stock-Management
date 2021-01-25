package com.rb.odmg;

import com.rb.odmg.Controller.Login;
import com.rb.odmg.Model.Datasource;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/rb/odmg/View/login.fxml"));
        Parent root = loader.load();
        Login controller = loader.getController();
        controller.setData(primaryStage);

        primaryStage.setTitle("Gestionnaire de Stock");
        primaryStage.setScene(new Scene(root, 1000, 650));
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
        if(!Datasource.getInstance().open()) {
            System.out.println("FATAL ERROR: Couldn't connect to database");
            Platform.exit();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Datasource.getInstance().close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
