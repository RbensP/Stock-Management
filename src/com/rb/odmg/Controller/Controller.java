package com.rb.odmg.Controller;

import com.rb.odmg.Model.*;
import com.rb.odmg.utils.Validator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

public class Controller {
    private Stage primaryStage;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private TableView<Article> articlesTable;

    @FXML
    private ComboBox<Commodity> commodities;

    @FXML
    private TableView<DisplayOrder> ordersTable;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private TextField searchField;

    @FXML
    private DatePicker dateField;

    public void setData(Stage stage){
        this.primaryStage = stage;
    }

    @FXML //Done
    public void listArticles() {
        System.out.println("in lists article req");
        Task<ObservableList<Article>> task = new GetAllArticlesTask();

        articlesTable.itemsProperty().bind(task.valueProperty());

        task.setOnSucceeded(e -> {
            articlesTable.itemsProperty().unbind();
            commodities.setPromptText("Selectionner famille");
        });
        task.setOnFailed(e -> articlesTable.itemsProperty().unbind());

        new Thread(task).start();
    }

    @FXML //Done
    public void listCommodities() {
        System.out.println("in lists coms req");
        Task<ObservableList<Commodity>> task = new GetAllCommoditiesTask();

        commodities.itemsProperty().bind(task.valueProperty());

        task.setOnSucceeded(e -> {
            commodities.setPromptText("Selectionner famille");
            commodities.setConverter(new StringConverter<Commodity>() {
                @Override
                public String toString(Commodity o) {
                    return o.getCommodity();
                }

                @Override
                public Commodity fromString(String s) {
                    return null;
                }
            });
            commodities.setButtonCell(new ListCell<>(){
                protected void updateItem(Commodity item, boolean empty){
                    super.updateItem(item, empty);
                    if (empty || item == null){
                        setText("Selectionner famille");
                    }
                    else {
                        setText(item.getCommodity());
                    }
                }
            });
            commodities.itemsProperty().unbind();
        });
        task.setOnFailed(e -> commodities.itemsProperty().unbind());

        new Thread(task).start();
    }

    @FXML
    public void articleFilter(Commodity commodity){
        if(commodity != null){
            try {
                ObservableList<Article> articles = FXCollections.observableArrayList
                        (Datasource.getInstance().queryArticlesByCommodity(commodity.getId(), Datasource.ORDER_BY_ASC));
                if(articles.size() != 0){
                    articlesTable.setItems(articles);
                }
                else {
                    articlesTable.getItems().clear();
                }
            } catch (SQLException e){
                AlertHandler.show("Une erreur s'est produite\n\nReessayez", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    public void OrderFilter(java.sql.Date date){
        if(date != null){
            try {
                ObservableList<DisplayOrder> displayOrders = FXCollections.observableArrayList
                        (Datasource.getInstance().queryOrdersWithClientsByDate(date));
                if(displayOrders.size() != 0){
                    ordersTable.setItems(displayOrders);
                }
                else {
                    ordersTable.getItems().clear();
                }
            } catch (SQLException e){
                AlertHandler.show("Une erreur s'est produite\n\nReessayez", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML //Done
    public void handleCommodityAction(){
        Commodity commodity = commodities.getSelectionModel().getSelectedItem();
        articleFilter(commodity);
    }

    @FXML //Done
    public void handleDatePickerAction(){
        LocalDate localDate = dateField.getValue();
        if(localDate != null){
            OrderFilter(java.sql.Date.valueOf(localDate));
        }
    }

    @FXML// Done
    public void listOrders() {
        System.out.println("in lists order req");
        Task<ObservableList<DisplayOrder>> task = new GetAllOrdersTask();

        ordersTable.itemsProperty().bind(task.valueProperty());
        progressBar.progressProperty().bind(task.progressProperty());
        progressBar.setVisible(true);

        task.setOnSucceeded(e -> {
            progressBar.setVisible(false);
            ordersTable.itemsProperty().unbind();
        });

        task.setOnFailed(e -> {
            progressBar.setVisible(false);
            ordersTable.itemsProperty().unbind();
        });

        new Thread(task).start();
    }

    @FXML
    public void handleDisplayAllOrdersClick(){
        listOrders();
        dateField.setValue(null);
        dateField.setPromptText("Selectionner date");
    }

    @FXML //Done
    public void handleDisplayAllArticleClick(){
        listArticles();
        commodities.getSelectionModel().select(null);
        commodities.setPromptText("Selectionner famille");
    }

    //Done
    public void insertArticle(ArticleDialogData data) {
        Service<Result> srv = new Service<>(){
            @Override
            protected Task<Result> createTask(){
                return new Task<>() {
                    @Override
                    protected Result call() throws Exception {
                        return Datasource.getInstance().insertArticle(data.getCommodityId(), data.getLibelle(), data.getPrice(), new java.sql.Date(new Date().getTime()));
                    }
                };
            }
        };

        srv.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Done article");
                Article article;
                if(srv.getValue().isSuccess()){
                    try {
                        article = Datasource.getInstance().queryArticleById(srv.getValue().getValue());
                        setItemToArticleTable(article);
                        if(commodities.getSelectionModel().getSelectedItem() != null){
                            articleFilter(commodities.getSelectionModel().getSelectedItem());
                        }
                    } catch (SQLException throwables) {
                        AlertHandler.show("Erreur lors de la recupération de l'article", Alert.AlertType.ERROR);
                        throwables.printStackTrace();
                    }
                    try {
                        Datasource.getInstance().insertStock(srv.getValue().getValue(), data.getInitStockQty());
                    } catch (SQLException throwables) {
                        AlertHandler.show("Erreur lors de l'enregistrement du stock! Reessayez", Alert.AlertType.ERROR);
                        throwables.printStackTrace();
                    }
                }
                else {
                    AlertHandler.show(srv.getValue().getErrMsg(), Alert.AlertType.ERROR);
                }
            }
        });
        srv.setOnFailed(e -> {
            AlertHandler.show("Erreur lors de l'enregistrement du stock! Reessayez", Alert.AlertType.ERROR);
        });

        srv.restart();
    }

    //Done
    public void insertCommodity(String commodity) {
        Service<Result> srv = new Service<Result>(){
            @Override
            protected Task<Result> createTask(){
                return new Task<Result>() {
                    @Override
                    protected Result call() throws Exception {
                        return Datasource.getInstance().insertCommodity(commodity);
                    }
                };
            }
        };

        srv.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if(srv.getValue().isSuccess()){
                    try {
                        Commodity commodity = Datasource.getInstance().queryCommodityById(srv.getValue().getValue());
                        Commodity commodity1 = commodities.getSelectionModel().getSelectedItem();
                        commodities.getItems().add(commodity);
                        commodities.getSelectionModel().select(commodity1);
                    } catch (SQLException throwables) {
                        AlertHandler.show("Erreur lors de la recupération de l'article", Alert.AlertType.ERROR);
                        throwables.printStackTrace();
                    }
                    listCommodities();
                }
                else {
                    AlertHandler.show(srv.getValue().getErrMsg(), Alert.AlertType.ERROR);
                }
            }
        });

        srv.restart();
    }

    /*
    //Done
    public void insertOrder(OrderDialogData data) {
        Service<Integer> srv = new Service<Integer>(){
            @Override
            protected Task<Integer> createTask(){
                return new Task<Integer>() {
                    @Override
                    protected Integer call() throws Exception {
                        return Datasource.getInstance().insertOrder(data.getArticleId(), new java.sql.Date(new Date().getTime()));
                    }
                };
            }
        };

        srv.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Done Insert order");
                try {
                    DisplayOrder displayOrder = Datasource.getInstance().queryOrderByIdWithArticle(srv.getValue());
                    setItemToOrderTable(displayOrder);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        srv.restart();
    }
    */

    //Done
    public void updateStock(int articleId, int quantity, int operation) {
        System.out.println(""+ articleId + ": "+ quantity);
        Service<Boolean> srv = new Service<Boolean>(){
            @Override
            protected Task<Boolean> createTask(){
                return new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {
                        return Datasource.getInstance().updateStock(articleId, quantity, operation);
                    }
                };
            }
        };
        srv.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if(srv.getValue()){
                    System.out.println("Updatestock done");
                    if(!srv.getValue()){
                        AlertHandler.show("Erreur lors de l'enregistrement! Reessayez", Alert.AlertType.ERROR);
                    }
                }
            }
        });
        srv.setOnFailed(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if(srv.getValue()){
                    System.out.println(" Update stock failed");
                }
                AlertHandler.show("Erreur lors de l'enregistrement! Reessayez", Alert.AlertType.ERROR);
            }
        });

        srv.restart();
    }

    @FXML // Done
    public void setItemToArticleTable(Article article){
        System.out.println("in setItem to table article");
        articlesTable.getItems().add(article);
    }

    @FXML // Done
    public void setItemToOrderTable(DisplayOrder displayOrder){
        System.out.println("in setItem to table order");
        ordersTable.getItems().add(displayOrder);
    }

    @FXML // Done
    public void showNewArticleDialog(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/rb/odmg/View/newArticleDialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch(IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        ArticleDialogController controller = fxmlLoader.getController();

        Task<ObservableList<Commodity>> commoditiesTask = new GetAllCommoditiesTask();
        commoditiesTask.setOnSucceeded(e -> {
            controller.setData(commoditiesTask.getValue(), dialog);
        });

        new Thread(commoditiesTask).start();

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Button btn = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        btn.setVisible(false);

        dialog.setTitle("Nouvel Article");
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("OK pressed");

            ArticleDialogData data = controller.processResults();
            this.insertArticle(data);
        } else {
            System.out.println("Cancel pressed");
        }
    }

    @FXML // Done
    public void showNewCommodityDialog(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/rb/odmg/View/newCommodityDialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch(IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        NewCommodityDialogController controller = fxmlLoader.getController();
        controller.setData("Entrer le nom de la nouvelle famille", "", dialog);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Button btn = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        btn.setVisible(false);

        dialog.setTitle("Nouvelle Famille");
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("OK pressed");

            String data = controller.processResults();

            this.insertCommodity(data);
        } else {
            System.out.println("Cancel pressed");
        }
    }

    @FXML // Done
    public void displayCommoditiesDialog(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/rb/odmg/View/commoditiesDialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch(IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        CommoditiesDialogController controller = fxmlLoader.getController();

        Task<ObservableList<Commodity>> task = new GetAllCommoditiesTask();
        task.setOnSucceeded(e -> {
            controller.setData(task.getValue());
        });
        new Thread(task).start();

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        dialog.setTitle("Familles d'Articles");
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("OK pressed");
        }
    }

    @FXML // Done
    public void showNewOrderDialog() throws SQLException {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/rb/odmg/View/newOrderDialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch(IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }
        OrderDialogController controller = fxmlLoader.getController();

        Task<ObservableList<Commodity>> commoditiesTask = new GetAllCommoditiesTask();
        commoditiesTask.setOnSucceeded(e -> {
            controller.setCommodities(commoditiesTask.getValue(), dialog);
        });

        new Thread(commoditiesTask).start();

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Button btn = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        btn.setVisible(false);

        dialog.setTitle("Nouveau Commande");
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("OK pressed");

            OrderDialogData data = controller.processResults();

            Dialog<ButtonType> clientDialog = new Dialog<>();
            clientDialog.initOwner(mainBorderPane.getScene().getWindow());
            FXMLLoader clientFxmlLoader = new FXMLLoader();
            clientFxmlLoader.setLocation(getClass().getResource("/com/rb/odmg/View/clientDialog.fxml"));

            try {
                clientDialog.getDialogPane().setContent(clientFxmlLoader.load());
            } catch(IOException e) {
                System.out.println("Couldn't load the dialog");
                e.printStackTrace();
                return;
            }

            ClientDialogController clientDialogController = clientFxmlLoader.getController();

            Task<ObservableList<Client>> clientsTask = new GetAllClientsTask();
            clientsTask.setOnSucceeded(e -> {
                clientDialogController.setData(clientsTask.getValue(), clientDialog);
            });
            new Thread(clientsTask).start();

            clientDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            clientDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
            Button okBtn = (Button) clientDialog.getDialogPane().lookupButton(ButtonType.OK);
            okBtn.setVisible(false);

            clientDialog.setTitle("Client");
            Optional<ButtonType> res = clientDialog.showAndWait();
            if(res.isPresent() && res.get() == ButtonType.OK) {
                System.out.println("OK pressed");
                progressBar.setVisible(true);

                ClientDialogData clientData = clientDialogController.processResults();

                try{
                    int clientId;
                    if(clientData.isNewClient()){
                        clientId = Datasource.getInstance().insertClient(clientData.getName(), clientData.getCity(), clientData.getAddress(), clientData.getPhone());
                    }
                    else {
                        clientId = clientData.getClient().getId();
                    }

                    try {
                        int orderId;
                        if(data.isValidated()){
                            orderId = Datasource.getInstance().insertOrder(clientId, new java.sql.Date(new Date().getTime()));
                        }
                        else {
                            orderId = Datasource.getInstance().insertNotValidatedOrder(clientId, new java.sql.Date(new Date().getTime()));
                        }

                        Service<Void> srv = new Service<>(){
                            @Override
                            protected Task<Void> createTask(){
                                return new Task<>() {
                                    @Override
                                    protected Void call() {
                                        data.getOrderedArticles().getItems().forEach(purchase -> {
                                            try {
                                                Datasource.getInstance().insertOrderedArticle(purchase.getArticleId(), orderId, purchase.getQuantity(), purchase.getTotal());
                                                if(data.isValidated()){
                                                    updateStock(purchase.getArticleId(), purchase.getQuantity(), Datasource.UPDATE_STOCK_TO_SUBTRACT);
                                                }
                                            } catch (SQLException exception) {
                                                exception.printStackTrace();
                                            }
                                        });
                                        return null;
                                    }
                                };
                            }
                        };
                        srv.setOnSucceeded(new EventHandler<>() {
                            @Override
                            public void handle(WorkerStateEvent event) {
                                System.out.println("Done Insert orderedArticles");
                                if(data.isValidated()){
                                    try {
                                        DisplayOrder displayOrder = Datasource.getInstance().queryOrderByIdWithClient(orderId);
                                        setItemToOrderTable(displayOrder);
                                    } catch (SQLException exception) {
                                        exception.printStackTrace();
                                    }
                                }
                                progressBar.setVisible(false);
                            }
                        });
                        srv.restart();

                    } catch (SQLException excep){
                        throw excep;
                    }
                } catch (SQLException exception){
                    throw exception;
                }
            } else {
                System.out.println("Cancel pressed");
            }
        } else {
            System.out.println("Cancel pressed");
        }
    }

    @FXML // Done
    public void showNewStockDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/rb/odmg/View/newStockDialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch(IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        StockDialogController controller = fxmlLoader.getController();

        Task<ObservableList<Article>> articlesTask = new GetAllArticlesTask();
        articlesTask.setOnSucceeded(e -> {
            controller.setData(articlesTask.getValue(), dialog);
        });

        new Thread(articlesTask).start();

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Button btn = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        btn.setVisible(false);

        dialog.setTitle("Nouveau Stock");
        Optional<ButtonType> result =  dialog.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("OK pressed");

            StockDialogData data = controller.processResults();
            this.updateStock(data.getArticleId(), data.getInitialQuantity(), Datasource.UPDATE_STOCK_TO_ADD);
        } else {
            System.out.println("Cancel pressed");
        }
    }

    @FXML // Done
    public void showEditArticleDialog(DisplayArticle displayArticle) {
        Dialog<ButtonType> editDialog = new Dialog<>();
        editDialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader editFxmlLoader = new FXMLLoader();
        editFxmlLoader.setLocation(getClass().getResource("/com/rb/odmg/View/editArticle.fxml"));

        try {
            editDialog.getDialogPane().setContent(editFxmlLoader.load());
        } catch(IOException err) {
            System.out.println("Couldn't load the dialog");
            err.printStackTrace();
            return;
        }

        EditArticleController editArticleController = editFxmlLoader.getController();
        editArticleController.setTextFields(displayArticle);

        Task<ObservableList<Commodity>> commoditiesTask = new Task (){
            @Override
            public ObservableList<Commodity> call()  {
                return FXCollections.observableArrayList
                        (Datasource.getInstance().queryCommodities(Datasource.ORDER_BY_NONE));
            }
        };
        commoditiesTask.setOnSucceeded(er -> {
            try {
                editArticleController.setData(commoditiesTask.getValue(), displayArticle, editDialog);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        new Thread(commoditiesTask).start();

        editDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        editDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Button btn = (Button) editDialog.getDialogPane().lookupButton(ButtonType.OK);
        btn.setVisible(false);

        editDialog.setTitle("Modifier Article");
        Optional<ButtonType> result = editDialog.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("Register pressed");

            ArticleDialogData data = editArticleController.processResults(displayArticle);
            try {
                Result res = Datasource.getInstance().updateArticle(displayArticle.getId(), data.getCommodityId(), data.getLibelle(), data.getPrice());
                if(res.isSuccess()){
                    Article updatedArticle = Datasource.getInstance().queryArticleById(displayArticle.getId());

                    for(int pos = 0; pos < articlesTable.getItems().size(); pos++){
                        Article articlePos = articlesTable.getItems().get(pos);
                        if(articlePos.getId() == updatedArticle.getId()){
                            articlesTable.getItems().set(pos, updatedArticle);
                            break;
                        }
                    }
                }
                else{
                    AlertHandler.show(res.getErrMsg(), Alert.AlertType.ERROR);
                }
            } catch (SQLException e){
                AlertHandler.show("Oupss! Une erreur s'est produite\nReessayez", Alert.AlertType.ERROR);
                System.out.println("" + e.getMessage());
            }
        } else {
            System.out.println("Cancel pressed");
        }
    }

    @FXML // Done
    public void handleClickArticle() throws SQLException {
        final Article article = articlesTable.getSelectionModel().getSelectedItem();

        if(article != null){
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(mainBorderPane.getScene().getWindow());
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/rb/odmg/View/articleInfoDialog.fxml"));

            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());
            } catch(IOException e) {
                System.out.println("Couldn't load the dialog");
                e.printStackTrace();
                return;
            }

            ArticleInfoController controller = fxmlLoader.<ArticleInfoController>getController();
            DisplayArticle displayArticle = Datasource.getInstance().queryArticleByIdWithStockAndCommodity(article.getId());

            controller.setData(displayArticle);

            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            ((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).setText("Modifier");

            dialog.setTitle("Information sur l'Article");
            Optional<ButtonType> result = dialog.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                System.out.println("Edit pressed");
                showEditArticleDialog(displayArticle);
            } else {
                System.out.println("Cancel pressed");
            }
        }
    }

    @FXML // Done
    public void handleClickOrder() {
        final DisplayOrder displayOrder = ordersTable.getSelectionModel().getSelectedItem();

        if(displayOrder != null){
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(mainBorderPane.getScene().getWindow());
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/rb/odmg/View/orderInfoDialog.fxml"));

            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());
            } catch(IOException e) {
                System.out.println("Couldn't load the dialog");
                e.printStackTrace();
                return;
            }

            OrderInfoController controller = fxmlLoader.<OrderInfoController>getController();
            controller.setDialog(mainBorderPane, dialog);

            Task<ObservableList<Purchase>> orderedArticlesTask = new GetAllOrderedArticlesByOrderIdTask(displayOrder.getId());
            orderedArticlesTask.setOnSucceeded(e -> {
                controller.setOrderedArticlesData(orderedArticlesTask.getValue());
            });
            new Thread(orderedArticlesTask).start();

            Service<DisplayOrder> srv = new Service<>(){
                @Override
                protected Task<DisplayOrder> createTask(){
                    return new Task<>() {
                        @Override
                        protected DisplayOrder call() throws Exception {
                            return Datasource.getInstance().queryOrderByIdWithClient(displayOrder.getId());
                        }
                    };
                }
            };
            srv.setOnSucceeded(new EventHandler<>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    System.out.println("Done query display order");
                    controller.setOrderAndClientInfo(srv.getValue());
                }
            });
            srv.restart();

            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
//        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            dialog.setTitle("Bon de Commande");
            Optional<ButtonType> result = dialog.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.CANCEL) {
                System.out.println("Cancel pressed");

            } else{
                System.out.println("OK pressed");

            }
        }
    }

    @FXML
    public void handleSearchClick(){
        String libelle = searchField.getText().trim();

        if(!Validator.isEmpty(libelle)){
            Task<ObservableList<Article>> task = new SearchArticlesTask(libelle);

            task.setOnSucceeded(e -> {
                articlesTable.setItems(task.getValue());
            });

            new Thread(task).start();
        }
    }

    @FXML
    public void handleLogOutClick() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/rb/odmg/View/login.fxml"));
        Parent root = loader.load();
        Login controller = loader.getController();
        controller.setData(primaryStage);

        primaryStage.setTitle("Gestionnaire de Commande");
        primaryStage.setScene(new Scene(root, 1000, 650));
        primaryStage.show();
    }

    @FXML
    public void handleProfomaListClick(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/rb/odmg/View/proformaList.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        ProformaListController controller = fxmlLoader.<ProformaListController>getController();
        controller.setData(dialog);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.setTitle("Liste de Proforma");

        dialog.setOnCloseRequest(e -> {
            LocalDate localDate = dateField.getValue();
            System.out.println(localDate);
            if(localDate != null){
                OrderFilter(java.sql.Date.valueOf(localDate));
            }
            else {
                listOrders();
            }
        });

        Optional<ButtonType> result = dialog.showAndWait();
    }

    @FXML
    public void handleDaySalesClick(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/rb/odmg/View/salesPerDay.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        SalesController controller = fxmlLoader.<SalesController>getController();
        controller.setData(dialog);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.setTitle("Ventes Par Jour");
        dialog.showAndWait();
    }
}

class GetAllArticlesTask extends Task {

    @Override
    public ObservableList<Article> call()  {
        return FXCollections.observableArrayList(Datasource.getInstance().queryArticles(Datasource.ORDER_BY_ASC));
    }
}

class SearchArticlesTask extends Task {
    private String libelle;

    public SearchArticlesTask(String libelle){
        this.libelle = libelle;
    }

    @Override
    public ObservableList<Article> call() throws SQLException {
        return FXCollections.observableArrayList(Datasource.getInstance().searchArticles(this.libelle));
    }
}

class GetAllCommoditiesTask extends Task {

    @Override
    public ObservableList<Commodity> call()  {
        return FXCollections.observableArrayList(Datasource.getInstance().queryCommodities(Datasource.ORDER_BY_NONE));
    }
}

class GetAllOrdersTask extends Task {

    @Override
    public ObservableList<DisplayOrder> call()  {
        return FXCollections.observableArrayList(Datasource.getInstance().queryOrdersWithClients(Datasource.ORDER_BY_ASC));
    }
}

class GetAllOrderedArticlesByOrderIdTask extends Task {
    private int orderId;

    public GetAllOrderedArticlesByOrderIdTask(int orderId){
        this.orderId = orderId;
    }

    @Override
    public ObservableList<Purchase> call() throws SQLException {
        System.out.println("calling OrderedArticles");
        return FXCollections.observableArrayList(Datasource.getInstance().queryOrderedArticlesByOrderId(this.orderId));
    }
}

class GetAllClientsTask extends Task {

    @Override
    public ObservableList<Client> call()  {
        return FXCollections.observableArrayList(Datasource.getInstance().queryClients(Datasource.ORDER_BY_ASC));
    }
}