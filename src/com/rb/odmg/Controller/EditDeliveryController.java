package com.rb.odmg.Controller;

import com.rb.odmg.Model.DisplayOrder;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditDeliveryController {
    @FXML
    private ComboBox<String> deliveryStates;

    @FXML
    private DatePicker date;

    @FXML
    private Button btn;

    private Dialog<ButtonType> dialog;
    private DisplayOrder order;

    public void initialize() {
        List<String> states = new ArrayList<>();
        states.add("EN COURS");
        states.add("LIVRÉ");
        states.add("ANNULÉ");

        deliveryStates.setItems(FXCollections.observableList(states));
    }

    @FXML
    public void setData(DisplayOrder order, Dialog<ButtonType> dialog){
        this.dialog = dialog;
        this.order = order;
        deliveryStates.getSelectionModel().select(order.getDeliveryState());
        handleStateAction();
    }

    public void handleRegisterClick(){
        LocalDate localDate = date.getValue();
        Date date;
        if(localDate != null){
            date = java.sql.Date.valueOf(localDate);
            Date now = new Date();
            if (date.before(now) || date.equals(now)){
                System.out.println("OK");
                ((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).fire();
            }
            else {
                AlertHandler.show("Vous devez choisir une date avant\nou égale à la date courante",
                        Alert.AlertType.WARNING);
            }
        }
        else {
            AlertHandler.show("Veuillez choisir une date", Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void handleStateAction(){
        if(deliveryStates.getSelectionModel().getSelectedItem().equals("LIVRÉ")){
            date.setDisable(false);
            btn.setDisable(false);
        }
        else {
            date.setDisable(true);
            btn.setDisable(true);
        }
    }

    @FXML
    public EditDelStateData processResults(){
        String state = deliveryStates.getSelectionModel().getSelectedItem();
        LocalDate localDate = date.getValue();
        java.sql.Date date = java.sql.Date.valueOf(localDate);

        return new EditDelStateData(state, date);
    }
}
