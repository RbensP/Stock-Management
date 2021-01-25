package com.rb.odmg.Controller;

import com.rb.odmg.Model.Article;
import com.rb.odmg.Model.Datasource;
import com.rb.odmg.Model.DisplayOrder;
import com.rb.odmg.Model.Purchase;
import com.rb.odmg.utils.Validator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SalesController {
    @FXML
    private DialogPane salesDialog;

    @FXML
    private TableView<Purchase> salesTable;

    @FXML
    private DatePicker dateField;

    private Dialog<ButtonType> dialog;

    @FXML
    private Text total;

    @FXML
    private Text day;

    private double _total;

    public void initialize(){
        java.sql.Date date = new java.sql.Date(new Date().getTime());
        dateField.setValue(LocalDate.now());
        try {
            handleDatePickerAction();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void setData(Dialog<ButtonType> dialog){
        this.dialog = dialog;
    }

    public ObservableList<DisplayOrder> OrderFilter(java.sql.Date date){
        if(date != null){
            try {
                return FXCollections.observableArrayList(Datasource.getInstance().queryOrdersWithClientsByDate(date));

            } catch (SQLException e){
                AlertHandler.show("Une erreur s'est produite\n\nReessayez", Alert.AlertType.ERROR);
                return null;
            }
        }
        else {
            return null;
        }
    }

    @FXML
    public void setTotal(Purchase orderedArticle){
        this._total += orderedArticle.getTotal();
    }

    @FXML
    public void handleDatePickerAction() throws SQLException {
        LocalDate localDate = dateField.getValue();
        Locale locale = new Locale("fr", "Fr");
        DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        day.setText("Ventes pour le " + df.format(java.sql.Date.valueOf(localDate)));

        if(localDate != null){
            ObservableList<DisplayOrder> displayOrders = OrderFilter(java.sql.Date.valueOf(localDate));

            if(displayOrders != null){
                List<ObservableList<Purchase>> allPurchases= new ArrayList<>();

                for (int i = 0; i<displayOrders.size(); i++){
                    ObservableList<Purchase> purchases = FXCollections.observableArrayList(
                            Datasource.getInstance().queryOrderedArticlesByOrderId(displayOrders.get(i).getId()));

                    allPurchases.add(purchases);
                }

                ObservableList<Article> articles = FXCollections.observableArrayList( Datasource.getInstance().queryArticles(Datasource.ORDER_BY_NONE));

                List<Purchase> totalSales = new ArrayList<>();

                for(int i = 0; i<articles.size(); i++){
                    int qty = 0;
                    double sum = 0;

                    for (int j = 0; j<allPurchases.size(); j++){
                        ObservableList<Purchase> purchases = allPurchases.get(j);

                        for(int k = 0; k<purchases.size(); k++){
                            if(purchases.get(k).getArticleId() == articles.get(i).getId()){
                                qty += purchases.get(k).getQuantity();
                                sum += purchases.get(k).getTotal();
                            }
                        }
                    }

                    if(qty != 0){
                        Purchase purchase = new Purchase();

                        purchase.setArticleId(articles.get(i).getId());
                        purchase.setLibelle(articles.get(i).getLibelle());
                        purchase.setQuantity(qty);
                        purchase.setTotal(sum);

                        totalSales.add(purchase);
                    }
                }

                salesTable.setItems(FXCollections.observableArrayList(totalSales));
                this._total = 0;
                totalSales.forEach(o -> setTotal(o));
                total.setText("Total : " + Validator.truncateDouble(this._total) + " HTG");
            }
        }
    }
}
