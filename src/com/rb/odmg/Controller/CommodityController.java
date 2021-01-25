package com.rb.odmg.Controller;

import com.rb.odmg.Model.Commodity;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class CommodityController {
    @FXML
    private Text commodity;

//    private Dialog<ButtonType> dialog;
//
//    private DialogPane commoditiesDialog;

    public void setData(Commodity com){
        commodity.setText(com.getCommodity());
    }

//    public void handleEditClick(){
//        dialog.close();
//
//        Dialog<ButtonType> dlg = new Dialog<>();
//        dlg.initOwner(commoditiesDialog.getScene().getWindow());
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource("/com/rb/odmg/View/newCommodityDialog.fxml"));
//
//        try {
//            dlg.getDialogPane().setContent(loader.load());
//
//        } catch(IOException e) {
//            System.out.println("Couldn't load the dialog");
//            e.printStackTrace();
//            return;
//        }
//
//        NewCommodityDialogController newCommodityDialogController = loader.getController();
//        newCommodityDialogController.setData("Modifier famille", commodity.getText(), dlg);
//
//        dlg.getDialogPane().getButtonTypes().add(ButtonType.OK);
//        dlg.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
//        Button btn = (Button) dlg.getDialogPane().lookupButton(ButtonType.OK);
//        btn.setVisible(false);
//
//        dlg.setTitle("Modifer Famille");
//        Optional<ButtonType> res = dlg.showAndWait();
//    }
}
