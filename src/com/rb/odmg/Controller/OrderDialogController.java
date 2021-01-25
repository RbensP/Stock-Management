package com.rb.odmg.Controller;

import com.rb.odmg.Model.*;
import com.rb.odmg.utils.Validator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.io.IOException;
import java.sql.SQLException;

public class OrderDialogController {
    @FXML
    private Dialog<ButtonType> dialog;

    @FXML
    private ComboBox<Commodity> commodityField;

    @FXML
    private ComboBox<Article> articleField;

    @FXML
    private TextField quantityField;

    @FXML
    private Text totalField;

    @FXML
    private TableView<Purchase> purchaseTable;

    @FXML
    private DialogPane orderDialog;

    private boolean validated;

    @FXML
    public void setArticles(ObservableList<Article> articles){
        articleField.setItems(articles);
        articleField.getSelectionModel().selectFirst();
        articleField.setConverter(new StringConverter<>() {
            @Override
            public String toString(Article o) {
                if(o != null){
                    return o.getLibelle() + " (" + o.getPrice() + " HTG Unité)";
                }
                else {
                    return  "";
                }
            }

            @Override
            public Article fromString(String s) {
                return null;
            }
        });
    }

    @FXML
    public void setCommodities(ObservableList<Commodity> commodities, Dialog<ButtonType> dialog){
        commodityField.setItems(commodities);
        commodityField.getSelectionModel().selectFirst();
        commodityField.setConverter(new StringConverter<>() {
            @Override
            public String toString(Commodity o) {
                return o.getCommodity();
            }

            @Override
            public Commodity fromString(String s) {
                return null;
            }
        });

        this.dialog = dialog;
    }

    @FXML
    public void handleCommodityAction(){
        Commodity commodity = commodityField.getSelectionModel().getSelectedItem();
        articleFilter(commodity);
    }

    @FXML
    public void articleFilter(Commodity commodity){
        if(commodity != null){
            try {
                ObservableList<Article> articles = FXCollections.observableArrayList
                        (Datasource.getInstance().queryArticlesByCommodity(commodity.getId(), Datasource.ORDER_BY_NONE));
                if(articles.size() != 0){
                    setArticles(articles);
                }
                else {
                    articleField.getItems().clear();
                }
            } catch (SQLException e){
                AlertHandler.show("Une erreur s'est produite\n\nReessayez", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    public void handleAddPurchase() throws SQLException {
        String quantityValue = quantityField.getText().trim();

        if(Validator.isInteger(quantityValue) && Integer.parseInt(quantityField.getText())>0){
            Article article = articleField.getSelectionModel().getSelectedItem();
            int quantity = Integer.parseInt(quantityField.getText().trim());
            double total = Validator.truncateDouble(article.getPrice()*quantity);


            System.out.println(quantity);

            Stock stock = Datasource.getInstance().queryStockByArticleId(article.getId());

            System.out.println(stock.getInitialQty());
            System.out.println((stock.getInitialQty() - quantity));

            if((stock.getInitialQty() - quantity) < 0){
                AlertHandler.show("Oupss! Stock insuffisant.\n\nSeulement " + stock.getInitialQty() + " disponibles!", Alert.AlertType.WARNING);
            }
            else{
                purchaseTable.getItems().add(new Purchase(article.getId(), article.getLibelle(), quantity, total));
                double totalCost = 0;
                for(int pos = 0; pos < purchaseTable.getItems().size(); pos++){
                    Purchase purchasePos = purchaseTable.getItems().get(pos);
                    totalCost += purchasePos.getTotal();
                }
                totalField.setText("" + Validator.truncateDouble(totalCost) + " HTG");
            }
        }
        else {
            AlertHandler.show("Le champ quantité doit contenir\nune valeur entière non nulle!", Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void handlePurchaseClick() {
        Purchase purchase = purchaseTable.getSelectionModel().getSelectedItem();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(orderDialog.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/rb/odmg/View/purchase.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        PurchaseController controller = fxmlLoader.getController();
        controller.setData(purchase, purchaseTable, dialog, totalField);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        dialog.setTitle("Achat");
        dialog.showAndWait();
    }

    @FXML
    public OrderDialogData processResults() {
        return new OrderDialogData(purchaseTable, validated);
    }

    @FXML
    public void handleRegisterClick(){
        if(purchaseTable.getItems().size() != 0){
            this.validated = true;
            ((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).fire();
        }
        else {
            AlertHandler.show("Vous devez ajouter au moins un article au tableau!", Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void handleProformaClick(){
        if(purchaseTable.getItems().size() != 0){
            this.validated = false;
            System.out.println(validated);
            ((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).fire();
        }
        else {
            AlertHandler.show("Vous devez ajouter au moins un article au tableau!", Alert.AlertType.WARNING);
        }
    }
}
