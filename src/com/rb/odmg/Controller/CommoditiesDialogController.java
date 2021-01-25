package com.rb.odmg.Controller;

import com.rb.odmg.Model.Commodity;
import com.rb.odmg.Model.Datasource;
import com.rb.odmg.Model.Result;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class CommoditiesDialogController {
    @FXML
    private TableView<Commodity> commoditiesTable;

    @FXML
    private DialogPane commoditiesDialog;

    public void setData(ObservableList<Commodity> commodities){
        this.commoditiesTable.setItems(commodities);
    }

    public void handleCommodityClick(){
        Commodity commodity = commoditiesTable.getSelectionModel().getSelectedItem();

        if(commodity != null){
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(commoditiesDialog.getScene().getWindow());
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/rb/odmg/View/commodity.fxml"));

            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());
            } catch(IOException e) {
                System.out.println("Couldn't load the dialog");
                e.printStackTrace();
                return;
            }

            CommodityController controller = fxmlLoader.getController();
            controller.setData(commodity);

            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            ((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).setText("Modifier");

            dialog.setTitle("Famille");
            Optional<ButtonType> result = dialog.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                System.out.println("Modif pressed");
                dialog.close();

                Dialog<ButtonType> dlg = new Dialog<>();
                dlg.initOwner(commoditiesDialog.getScene().getWindow());
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/com/rb/odmg/View/newCommodityDialog.fxml"));

                try {
                    dlg.getDialogPane().setContent(loader.load());

                } catch(IOException e) {
                    System.out.println("Couldn't load the dialog");
                    e.printStackTrace();
                    return;
                }

                NewCommodityDialogController newCommodityDialogController = loader.getController();
                newCommodityDialogController.setData("Modifier famille", commodity.getCommodity(), dlg);

                dlg.getDialogPane().getButtonTypes().add(ButtonType.OK);
                dlg.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
                Button btn = (Button) dlg.getDialogPane().lookupButton(ButtonType.OK);
                btn.setVisible(false);

                dlg.setTitle("Modifer Famille");
                Optional<ButtonType> res = dlg.showAndWait();
                if(res.isPresent() && res.get() == ButtonType.OK) {
                    System.out.println("OK pressed");
                    String data = newCommodityDialogController.processResults();

                    try {
                        Result updateRes = Datasource.getInstance().updateCommodity(commodity.getId(), data);
                        if(updateRes.isSuccess()){
                            Commodity updatedCommodity = Datasource.getInstance().queryCommodityById(commodity.getId());

                            for(int pos = 0; pos < commoditiesTable.getItems().size(); pos++){
                                Commodity commodityPos = commoditiesTable.getItems().get(pos);
                                if(commodityPos.getId() == updatedCommodity.getId()){
                                    commoditiesTable.getItems().set(pos, updatedCommodity);
                                    break;
                                }
                            }
                        }
                        else {
                            AlertHandler.show(updateRes.getErrMsg(), Alert.AlertType.ERROR);
                        }
                    } catch (SQLException e){
                        AlertHandler.show("Oupss! Une erreur s'est produite! Reessayez", Alert.AlertType.ERROR);
                    }

                } else {
                    System.out.println("Cancel pressed");
                }
            } else {
                System.out.println(" pressed");
            }
        }
    }
}
