package com.rb.odmg.Controller;

import com.rb.odmg.Model.Client;
import com.rb.odmg.utils.Validator;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class ClientDialogController {
    @FXML
    private Dialog<ButtonType> dialog;

    @FXML
    private TableView<Client> clients;

    @FXML
    private TextField nameField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField cityField;

    @FXML
    private TextField phoneField;

    @FXML
    private GridPane clientForm;

    @FXML
    private Button toggleBtn;


    public void initialize(){
        clientForm.setDisable(true);
    }

    @FXML
    public ClientDialogData processResults(){
        if(clientForm.isDisabled() && clients.getSelectionModel().getSelectedItem() != null){
            return new ClientDialogData(clients.getSelectionModel().getSelectedItem());
        }
        else {
            String name = nameField.getText().trim();
            String city = cityField.getText().trim();
            String address = addressField.getText().trim();
            String phone = phoneField.getText().trim();

            return new ClientDialogData(name, city, address, phone);
        }
    }

    @FXML
    public void setData(ObservableList<Client> clients, Dialog<ButtonType> dialog){
        this.clients.setItems(clients);
        this.dialog = dialog;
    }

    @FXML
    public void handleRegisterClick(){
        if(clientForm.isDisabled() && clients.getSelectionModel().getSelectedItem() != null){
            ((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).fire();
        }
        else if (clientForm.isDisabled() && clients.getSelectionModel().getSelectedItem() == null){
            AlertHandler.show("Selectionner un client ou creer un nouveau", Alert.AlertType.WARNING);
        }
        else {
            String name = nameField.getText().trim();
            String city = cityField.getText().trim();
            String address = addressField.getText().trim();
            String phone = phoneField.getText().trim();

            if(!Validator.isEmpty(name) && !Validator.isEmpty(city) && !Validator.isEmpty(address) && Validator.isPhoneNumber(phone)){
                ((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).fire();
                System.out.println("OK");
            }
            else {
                AlertHandler.show("Les champs ne doivent pas être vide\n\nLe numero doit être valide!", Alert.AlertType.WARNING);
            }
        }
    }

    public void handleToggleClick(){
        if(clientForm.isDisabled()){
            clientForm.setDisable(false);
            clients.setDisable(true);
            toggleBtn.setText("Selectionner client");
        }
        else {
            clientForm.setDisable(true);
            clients.setDisable(false);
            toggleBtn.setText("Nouveau client");
        }
    }
}
