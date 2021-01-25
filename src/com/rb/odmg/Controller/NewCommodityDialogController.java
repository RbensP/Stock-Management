package com.rb.odmg.Controller;

import com.rb.odmg.utils.Validator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

public class NewCommodityDialogController {
    @FXML
    private Dialog<ButtonType> commodityDialog;

    @FXML
    private TextField commodityField;

    @FXML
    private Text title;

    public String processResults(){
        return commodityField.getText().trim();
    }

    public void setData(String title, String value, Dialog<ButtonType> dialog){
        this.title.setText(title);
        this.commodityField.setText(value);

        this.commodityDialog = dialog;
    }

    @FXML
    public void handleRegisterClick(){
        String commod = commodityField.getText().trim();
//System.out.println(commod.toLowerCase());

        if(Validator.isEmpty(commod)){
            AlertHandler.show("Veuillez enter le nom de la famille", Alert.AlertType.WARNING);
        }
        else {
            ((Button) commodityDialog.getDialogPane().lookupButton(ButtonType.OK)).fire();
        }
    }
}
