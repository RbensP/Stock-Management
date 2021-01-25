package com.rb.odmg.Controller;

import javafx.scene.control.Alert;

public class AlertHandler {
    public AlertHandler(){ }

    public static void show(String msg, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);

        switch (alertType){
            case ERROR: alert.setTitle("Error Dialog");
                break;

            case INFORMATION: alert.setTitle("Information Dialog");
                break;

            case WARNING: alert.setTitle("Warning Dialog");
                break;
        }
        alert.setContentText(msg);

        alert.showAndWait();
    }
}
