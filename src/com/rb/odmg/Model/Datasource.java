package com.rb.odmg.Model;

import com.rb.odmg.utils.Validator;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Datasource {
    public static final String DB_NAME = "orderMng";
    public static final String USER = "root";
    public static final String PASSWORD = "Mysqlp@ssw0rd";

    public static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/" + DB_NAME +
            "?user=" + USER + "&password=" + PASSWORD + "&useSSL=false";

    public static final String TABLE_ARTICLES = "articles";
    public static final String COLUMN_ARTICLE_ID = "id";
    public static final String COLUMN_ARTICLE_COMMODITYID = "commodity_id";
    public static final String COLUMN_ARTICLE_LIBELLE = "libelle";
    public static final String COLUMN_ARTICLE_PRICE = "price";
    public static final String COLUMN_ARTICLE_DATECREATED = "dateCreated";
    public static final int INDEX_ARTICLE_ID = 1;
    public static final int INDEX_ARTICLE_COMMODITYID = 2;
    public static final int INDEX_ARTICLE_LIBELLE = 3;
    public static final int INDEX_ARTICLE_PRICE = 4;
    public static final int INDEX_ARTICLE_DATECREATED = 5;

    public static final String TABLE_CLIENTS = "accounts";
    public static final String COLUMN_CLIENT_ID = "id";
    public static final String COLUMN_CLIENT_NAME = "name";
    public static final String COLUMN_CLIENT_CITY= "city";
    public static final String COLUMN_CLIENT_ADDRESS = "address";
    public static final String COLUMN_CLIENT_PHONE = "phone";
    public static final int INDEX_CLIENT_ID = 1;
    public static final int INDEX_CLIENT_NAME = 2;
    public static final int INDEX_CLIENT_CITY = 3;
    public static final int INDEX_CLIENT_ADDRESS = 4;
    public static final int INDEX_CLIENT_PHONE = 5;

    public static final String TABLE_STOCKS = "stocks";
    public static final String COLUMN_STOCK_ID = "id";
    public static final String COLUMN_STOCK_ARTICLEID = "article_id";
    public static final String COLUMN_STOCK_INITIALQUANTITY = "initialQuantity";
    public static final int INDEX_STOCK_ID = 1;
    public static final int INDEX_STOCK_ARTICLEID = 2;
    public static final int INDEX_STOCK_INITIALQTY = 3;

    public static final String TABLE_ORDERS = "orders";
    public static final String COLUMN_ORDER_ID = "id";
    public static final String COLUMN_ORDER_CLIENTID = "account_id";
    public static final String COLUMN_ORDER_DATECREATED = "date";
    public static final String COLUMN_ORDER_DELIVERYSTATE = "deliveryState";
    public static final String COLUMN_ORDER_DELIVEREDDATE = "deliveredDate";
    public static final String COLUMN_ORDER_ISVALIDATED = "isValidated";
    public static final int INDEX_ORDER_ID = 1;
    public static final int INDEX_ORDER_CLIENTID = 2;
    public static final int INDEX_ORDER_DATECREATED = 3;
    public static final int INDEX_ORDER_DELIVERYSTATE = 4;
    public static final int INDEX_ORDER_DELIVEREDDATE = 5;
    public static final int INDEX_ORDER_ISVALIDATED = 5;

    public static final String TABLE_ORDEREDARTICLES = "orderedArticles";
    public static final String COLUMN_ORDEREDARTICLES_ID = "id";
    public static final String COLUMN_ORDEREDARTICLES_ARTICLEID = "article_id";
    public static final String COLUMN_ORDEREDARTICLES_ORDERID = "order_id";
    public static final String COLUMN_ORDEREDARTICLES_QUANTITY = "quantity";
    public static final String COLUMN_ORDEREDARTICLES_TOTAL = "total";
    public static final int INDEX_ORDEREDARTICLES_ID = 1;
    public static final int INDEX_ORDEREDARTICLES_ARTICLEID = 2;
    public static final int INDEX_ORDEREDARTICLES_ORDERID = 3;
    public static final int INDEX_ORDEREDARTICLES_QUANTITY = 4;
    public static final int INDEX_ORDEREDARTICLES_TOTAL = 5;

    public static final String TABLE_COMMODITIES = "commodities";
    public static final String COLUMN_COMMODITY_ID = "id";
    public static final String COLUMN_COMMODITY_COMMODITY = "commodity";
    public static final int INDEX_COMMODITY_ID = 1;
    public static final int INDEX_COMMODITY_COMMODITY = 2;

    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASC = 2;
    public static final int ORDER_BY_DESC = 3;
    public static final int ORDER_BY_PRICE = 4;

    public static final int UPDATE_STOCK_TO_ADD = 1;
    public static final int UPDATE_STOCK_TO_SUBTRACT = 2;


    // ARTICLES DB_REQUESTS
    public static final String QUERY_ARTICLES = "SELECT * FROM " + TABLE_ARTICLES;

    public static final String QUERY_ARTICLES_WITH_COMMODITIES = "SELECT * FROM " + TABLE_ARTICLES +
            ", " + TABLE_COMMODITIES +" WHERE " + TABLE_ARTICLES + "." + COLUMN_ARTICLE_COMMODITYID + " = " + TABLE_COMMODITIES + "." + COLUMN_COMMODITY_ID;

    public static final String QUERY_ARTICLE_BY_ID_WITH_COMMODITY_AND_STOCK = "SELECT * FROM " + TABLE_ARTICLES + ", " + TABLE_COMMODITIES + ", " + TABLE_STOCKS +
            " WHERE " + TABLE_ARTICLES + "." + COLUMN_ARTICLE_COMMODITYID + " = " + TABLE_COMMODITIES + "." + COLUMN_COMMODITY_ID +
            " AND " +  TABLE_ARTICLES + "." + COLUMN_ARTICLE_ID + " = " + TABLE_STOCKS + "." + COLUMN_STOCK_ARTICLEID +
            " AND " + TABLE_ARTICLES + "." + COLUMN_ARTICLE_ID + " = ?";

    public static final String QUERY_ARTICLE_BY_ID_WITH_COMMODITY = "SELECT * FROM " + TABLE_ARTICLES +  ", " + TABLE_COMMODITIES +
            " WHERE " + TABLE_ARTICLES + "." + COLUMN_ARTICLE_COMMODITYID + "=" + TABLE_COMMODITIES + "." + COLUMN_COMMODITY_ID +
            " AND " + TABLE_ARTICLES + "." + COLUMN_ARTICLE_ID + " = ?";

    public static final String QUERY_ARTICLES_BY_COMMODITY = "SELECT * FROM " + TABLE_ARTICLES +
            " WHERE " + COLUMN_ARTICLE_COMMODITYID + " = ?";

    public static final String SEARCH_ARTICLE_BY_LIBELLE = "SELECT * FROM " +
            TABLE_ARTICLES + " WHERE " + COLUMN_ARTICLE_LIBELLE + " LIKE CONCAT('%', ?, '%')";

    public static final String QUERY_ARTICLE_BY_LIBELLE = "SELECT * FROM " +
            TABLE_ARTICLES + " WHERE " + COLUMN_ARTICLE_LIBELLE + " = ?";

    public static final String QUERY_ARTICLE_BY_ID = "SELECT * FROM " +
            TABLE_ARTICLES + " WHERE " + COLUMN_ARTICLE_ID + " = ?";

    public static final String INSERT_ARTICLE = "INSERT INTO " + TABLE_ARTICLES +
            '(' + COLUMN_ARTICLE_COMMODITYID + ", " + COLUMN_ARTICLE_LIBELLE + ", " + COLUMN_ARTICLE_PRICE + ", " +
            COLUMN_ARTICLE_DATECREATED + ") VALUES(?, ?, ?, ?)";

    public static final String UPDATE_ARTICLE = "UPDATE " + TABLE_ARTICLES +
            " SET " + COLUMN_ARTICLE_COMMODITYID + " = ?, " + COLUMN_ARTICLE_LIBELLE + " = ?, " +
            COLUMN_ARTICLE_PRICE + " = ? " + " WHERE " + COLUMN_ARTICLE_ID + " = ?";


    // STOCKS DB_REQUESTS
    public static final String QUERY_STOCKS = "SELECT * FROM " + TABLE_STOCKS;

    public static final String QUERY_STOCKS_WITH_ARTICLES = "SELECT * FROM " + TABLE_STOCKS + ", " + TABLE_ARTICLES +
            " WHERE " + TABLE_STOCKS + "." + COLUMN_STOCK_ARTICLEID + "=" + TABLE_ARTICLES + "." + COLUMN_ARTICLE_ID ;

    public static final String QUERY_STOCK_BY_ID_WITH_ARTICLE = "SELECT * FROM " + TABLE_STOCKS + ", " + TABLE_ARTICLES +
            " WHERE " + TABLE_STOCKS + "." + COLUMN_STOCK_ARTICLEID + "=" + TABLE_ARTICLES + "." + COLUMN_ARTICLE_ID +
            " AND ";

    public static final String INSERT_STOCK = "INSERT INTO " + TABLE_STOCKS +
            '(' + COLUMN_STOCK_ARTICLEID + ", " + COLUMN_STOCK_INITIALQUANTITY + ") VALUES(?, ?)";

    public static final String UPDATE_STOCK = "UPDATE " + TABLE_STOCKS +
            " SET " + COLUMN_STOCK_INITIALQUANTITY + " = ? WHERE " +
            COLUMN_STOCK_ARTICLEID + " = ?";

    public static final String QUERY_STOCK_BY_ID = "SELECT * FROM " +
            TABLE_STOCKS + " WHERE " + COLUMN_STOCK_ID + " = ?";

    public static final String QUERY_STOCK_BY_ARTICLEID = "SELECT * FROM " +
            TABLE_STOCKS + " WHERE " + COLUMN_STOCK_ARTICLEID + " = ?";


    // ORDERS DB_REQUESTS
    public static final String QUERY_ORDERS = "SELECT * FROM " + TABLE_ORDERS;

    public static final String QUERY_ORDERS_WITH_ARTICLES_AND_CLIENTS = "SELECT " + TABLE_ORDEREDARTICLES + "." + COLUMN_ORDEREDARTICLES_ID + ", " +
            TABLE_CLIENTS + "." + COLUMN_CLIENT_NAME + ", " + TABLE_ORDERS + "." + COLUMN_ORDER_DATECREATED +
            " FROM " + TABLE_ORDEREDARTICLES + ", " + TABLE_CLIENTS + ", " + TABLE_ORDERS +
            " WHERE " + TABLE_ORDEREDARTICLES + "." + COLUMN_ORDEREDARTICLES_ORDERID + " = " + TABLE_ORDERS + "." + COLUMN_ORDER_ID +
            " AND " + TABLE_ORDERS + "." + COLUMN_ORDER_CLIENTID + " = " + TABLE_CLIENTS + "." + COLUMN_CLIENT_ID;

    public static final String QUERY_ORDERS_WITH_CLIENTS = "SELECT " + TABLE_ORDERS + "." + COLUMN_ORDER_ID + ", " +
            TABLE_ORDERS + "." + COLUMN_ORDER_DATECREATED + ", " + TABLE_CLIENTS + "." + COLUMN_CLIENT_NAME +
            " FROM " + TABLE_ORDERS + ", " + TABLE_CLIENTS +
            " WHERE " + TABLE_ORDERS + "." + COLUMN_ORDER_CLIENTID + " = " + TABLE_CLIENTS + "." + COLUMN_CLIENT_ID +
            " AND " + TABLE_ORDERS + "." + COLUMN_ORDER_ISVALIDATED + " = TRUE";

    public static final String QUERY_NOT_VALIDATED_ORDERS_WITH_CLIENTS = "SELECT " + TABLE_ORDERS + "." + COLUMN_ORDER_ID + ", " +
            TABLE_ORDERS + "." + COLUMN_ORDER_DATECREATED + ", " + TABLE_CLIENTS + "." + COLUMN_CLIENT_NAME +
            " FROM " + TABLE_ORDERS + ", " + TABLE_CLIENTS +
            " WHERE " + TABLE_ORDERS + "." + COLUMN_ORDER_CLIENTID + " = " + TABLE_CLIENTS + "." + COLUMN_CLIENT_ID +
            " AND " + TABLE_ORDERS + "." + COLUMN_ORDER_ISVALIDATED + " = FALSE";

    public static final String QUERY_ORDERS_WITH_CLIENTS_BY_DATE = "SELECT " + TABLE_ORDERS + "." + COLUMN_ORDER_ID + ", " +
            TABLE_ORDERS + "." + COLUMN_ORDER_DATECREATED + ", " + TABLE_CLIENTS + "." + COLUMN_CLIENT_NAME +
            " FROM " + TABLE_ORDERS + ", " + TABLE_CLIENTS +
            " WHERE " + TABLE_ORDERS + "." + COLUMN_ORDER_CLIENTID + " = " + TABLE_CLIENTS + "." + COLUMN_CLIENT_ID +
            " AND " + TABLE_ORDERS + "." + COLUMN_ORDER_DATECREATED + " = ? AND " + TABLE_ORDERS + "." + COLUMN_ORDER_ISVALIDATED + " = TRUE" +
            " ORDER BY " + TABLE_CLIENTS + "." + COLUMN_CLIENT_NAME;

    public static final String QUERY_ORDER_BY_ID_WITH_CLIENT = "SELECT * FROM " + TABLE_ORDERS + ", " + TABLE_CLIENTS +
            " WHERE " + TABLE_ORDERS + "." + COLUMN_ORDER_CLIENTID + " = " + TABLE_CLIENTS + "." + COLUMN_CLIENT_ID +
            " AND " + TABLE_ORDERS + "." + COLUMN_ORDER_ID + " = ?";

    public static final String QUERY_ORDEREDARTICLES_BY_ID = "SELECT " + TABLE_ARTICLES + "." + COLUMN_ARTICLE_LIBELLE +
            TABLE_ORDEREDARTICLES + "." + COLUMN_ORDEREDARTICLES_QUANTITY + " SUM(" + TABLE_ORDEREDARTICLES + "." + COLUMN_ORDEREDARTICLES_QUANTITY + ") " +
            " FROM " + TABLE_ORDEREDARTICLES + ", " + TABLE_ORDERS + ", " + TABLE_CLIENTS +
            " WHERE " + TABLE_ORDEREDARTICLES + "." + COLUMN_ORDEREDARTICLES_ORDERID + " = " + TABLE_ORDERS + "." + COLUMN_ORDER_ID +
            " AND " + TABLE_ORDERS + "." + COLUMN_ORDER_CLIENTID + " = " + TABLE_CLIENTS + "." + COLUMN_CLIENT_ID + " AND " +
            TABLE_ORDEREDARTICLES + "." + COLUMN_ORDEREDARTICLES_ORDERID + " = ?";

    public static final String QUERY_ORDEREDARTICLES_BY_ORDERID = "SELECT " + TABLE_ARTICLES + "." + COLUMN_ARTICLE_ID + ", " +
            TABLE_ARTICLES + "." + COLUMN_ARTICLE_LIBELLE + ", " + TABLE_ORDEREDARTICLES + "." + COLUMN_ORDEREDARTICLES_QUANTITY + ", " +
            TABLE_ORDEREDARTICLES + "." + COLUMN_ORDEREDARTICLES_TOTAL +
            " FROM " + TABLE_ORDEREDARTICLES + ", " + TABLE_ARTICLES +
            " WHERE " + TABLE_ORDEREDARTICLES + "." + COLUMN_ORDEREDARTICLES_ARTICLEID + " = " + TABLE_ARTICLES + "." + COLUMN_ARTICLE_ID +
            " AND " + TABLE_ORDEREDARTICLES + "." + COLUMN_ORDEREDARTICLES_ORDERID + " = ?";

    public static final String QUERY_ORDER_BY_ID = "SELECT * FROM " +
            TABLE_ORDERS + " WHERE " + COLUMN_ORDER_ID + " = ?";

    public static final String INSERT_ORDER = "INSERT INTO " + TABLE_ORDERS +
            '(' + COLUMN_ORDER_CLIENTID + ", " + COLUMN_ORDER_DATECREATED + ") " +
            "VALUES(?, ?)";

    public static final String INSERT_NOT_VALIDATED_ORDER = "INSERT INTO " + TABLE_ORDERS +
            '(' + COLUMN_ORDER_CLIENTID + ", " + COLUMN_ORDER_DATECREATED +  ", " +
            COLUMN_ORDER_DELIVERYSTATE +  ", " + COLUMN_ORDER_DELIVEREDDATE + ", " + COLUMN_ORDER_ISVALIDATED + ") " +
            "VALUES(?, ?, NULL, NULL, FALSE)";

    public static final String UPDATE_ORDER_DELIVERY_STATE = "UPDATE " + TABLE_ORDERS +
            " SET " + COLUMN_ORDER_DELIVERYSTATE + " = ?, " + COLUMN_ORDER_DELIVEREDDATE + " = ?" +
            " WHERE " + COLUMN_ORDER_ID + " = ?";

    public static final String VALIDATE_ORDER = "UPDATE " + TABLE_ORDERS +
            " SET " + COLUMN_ORDER_DELIVERYSTATE + " = 'EN COURS', "  +  COLUMN_ORDER_ISVALIDATED + " = TRUE " +
            " WHERE " + COLUMN_ORDER_ID + " = ?";

    public static final String UPDATE_ORDEREDARTICLE = "UPDATE " + TABLE_ORDEREDARTICLES +
            " SET " + COLUMN_ORDEREDARTICLES_QUANTITY + " = ?, " + COLUMN_ORDEREDARTICLES_TOTAL + "= ? " +
            "WHERE " + COLUMN_ORDEREDARTICLES_ORDERID + " = ?" + " AND " + COLUMN_ORDEREDARTICLES_ARTICLEID + " = ?";

    public static final String DELETE_ORDEREDARTICLE = "DELETE FROM " + TABLE_ORDEREDARTICLES +
            " WHERE " + COLUMN_ORDEREDARTICLES_ARTICLEID + " = ?";


    // COMMODITIES DB_REQUESTS
    public static final String INSERT_COMMODITY = "INSERT INTO " + TABLE_COMMODITIES +
            '(' + COLUMN_COMMODITY_COMMODITY + ") VALUES(?)";

    public static final String UPDATE_COMMODITY = "UPDATE " + TABLE_COMMODITIES +
            " SET " + COLUMN_COMMODITY_COMMODITY + " = ? WHERE " + COLUMN_COMMODITY_ID + " = ?";

    public static final String QUERY_COMMODITIES = "SELECT * FROM " + TABLE_COMMODITIES;

    public static final String QUERY_COMMODITY = "SELECT * FROM " + TABLE_COMMODITIES +
            " WHERE " + COLUMN_COMMODITY_COMMODITY + " = ?";

    public static final String QUERY_COMMODITY_BY_ID = "SELECT * FROM " + TABLE_COMMODITIES +
            " WHERE " + COLUMN_COMMODITY_ID + " = ?";


    //CLIENT DB_REQUESTS
    public static final String INSERT_CLIENT = "INSERT INTO " + TABLE_CLIENTS +
            '(' + COLUMN_CLIENT_NAME + ", " + COLUMN_CLIENT_CITY + ", " + COLUMN_CLIENT_ADDRESS + ", " + COLUMN_CLIENT_PHONE + ") " +
            "VALUES(?, ?, ?, ?)";

    //CLIENT DB_REQUESTS
    public static final String QUERY_CLIENTS = "SELECT * FROM " + TABLE_CLIENTS;


    //ORDERED_ARTICLES DB_REQUESTS
    public static final String INSERT_ORDERED_ARTICLE = "INSERT INTO " + TABLE_ORDEREDARTICLES +
            '(' + COLUMN_ORDEREDARTICLES_ARTICLEID + ", " + COLUMN_ORDEREDARTICLES_ORDERID + ", " + COLUMN_ORDEREDARTICLES_QUANTITY + ", " + COLUMN_ORDEREDARTICLES_TOTAL + ") " +
            "VALUES(?, ?, ?, ?)";

    private Connection conn;

    private PreparedStatement insertIntoArticles;
    private PreparedStatement insertIntoStocks;
    private PreparedStatement insertIntoClients;
    private PreparedStatement insertIntoOrders;
    private PreparedStatement insertNotValidatedOrder;
    private PreparedStatement insertIntoOrderedArticles;
    private PreparedStatement updateStock;
    private PreparedStatement updateCommodity;
    private PreparedStatement updateArticle;
    private PreparedStatement updateOrderedArticle;
    private PreparedStatement updateOrderDeliveryState;

    private PreparedStatement deleteOrderedArticle;
    private PreparedStatement queryArticleByLibelle;
    private PreparedStatement searchArticleByLibelle;
    private PreparedStatement queryArticleById;
    private PreparedStatement queryArticlesByCommodity;
    private PreparedStatement queryArticleByIdWithCommodity;
    private PreparedStatement queryStockById;
    private PreparedStatement queryCommodity;
    private PreparedStatement queryCommodityById;
    private PreparedStatement queryStockByArticleId;
    private PreparedStatement queryOrderById;
    private PreparedStatement validateOrder;
    private PreparedStatement queryOrderedArticlesByOrderId;
    private PreparedStatement queryOrderByIdWithArticle;
    private PreparedStatement queryOrderByIdWithClient;
    private PreparedStatement queryOrdersByDateWithClients;
    private PreparedStatement insertIntoCommodities;
    private PreparedStatement queryArticleByIdWithStockAndCommodity;

    private static Datasource instance = new Datasource();

    private Datasource() { }

    public static Datasource getInstance() {
        return instance;
    }

    public boolean open() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(CONNECTION_STRING);

            insertIntoArticles = conn.prepareStatement(INSERT_ARTICLE, Statement.RETURN_GENERATED_KEYS);
            queryArticleByLibelle = conn.prepareStatement(QUERY_ARTICLE_BY_LIBELLE);
            searchArticleByLibelle = conn.prepareStatement(SEARCH_ARTICLE_BY_LIBELLE);
            queryArticleByIdWithStockAndCommodity = conn.prepareStatement(QUERY_ARTICLE_BY_ID_WITH_COMMODITY_AND_STOCK);
            queryArticleByIdWithCommodity = conn.prepareStatement(QUERY_ARTICLE_BY_ID_WITH_COMMODITY);
            queryArticleById = conn.prepareStatement(QUERY_ARTICLE_BY_ID);
            queryArticlesByCommodity = conn.prepareStatement(QUERY_ARTICLES_BY_COMMODITY);
            updateArticle = conn.prepareStatement(UPDATE_ARTICLE);

            insertIntoStocks = conn.prepareStatement(INSERT_STOCK, Statement.RETURN_GENERATED_KEYS);
            queryStockById = conn.prepareStatement(QUERY_STOCK_BY_ID);
            queryStockByArticleId = conn.prepareStatement(QUERY_STOCK_BY_ARTICLEID);
            updateStock = conn.prepareStatement(UPDATE_STOCK);

            insertIntoOrders = conn.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS);
            insertNotValidatedOrder = conn.prepareStatement(INSERT_NOT_VALIDATED_ORDER, Statement.RETURN_GENERATED_KEYS);
            queryOrderById = conn.prepareStatement(QUERY_ORDER_BY_ID);
            validateOrder = conn.prepareStatement(VALIDATE_ORDER);
            queryOrdersByDateWithClients = conn.prepareStatement(QUERY_ORDERS_WITH_CLIENTS_BY_DATE);
            updateOrderedArticle = conn.prepareStatement(UPDATE_ORDEREDARTICLE);
            updateOrderDeliveryState = conn.prepareStatement(UPDATE_ORDER_DELIVERY_STATE);
            queryOrderedArticlesByOrderId = conn.prepareStatement(QUERY_ORDEREDARTICLES_BY_ORDERID);

            insertIntoCommodities = conn.prepareStatement(INSERT_COMMODITY, Statement.RETURN_GENERATED_KEYS);
            queryCommodity = conn.prepareStatement(QUERY_COMMODITY);
            queryCommodityById = conn.prepareStatement(QUERY_COMMODITY_BY_ID);
            updateCommodity = conn.prepareStatement(UPDATE_COMMODITY);

            insertIntoClients = conn.prepareStatement(INSERT_CLIENT, Statement.RETURN_GENERATED_KEYS);
//            queryClient = conn.prepareStatement(QUERY_CLIENT);

            insertIntoOrderedArticles = conn.prepareStatement(INSERT_ORDERED_ARTICLE, Statement.RETURN_GENERATED_KEYS);
            queryOrderByIdWithClient = conn.prepareStatement(QUERY_ORDER_BY_ID_WITH_CLIENT);
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if(insertIntoArticles !=  null) {
                insertIntoArticles.close();
            }
            if(queryArticleByLibelle != null) {
                queryArticleByLibelle.close();
            }
            if(queryArticleByIdWithCommodity != null) {
                queryArticleByIdWithCommodity.close();
            }
            if(queryArticlesByCommodity != null) {
                queryArticlesByCommodity.close();
            }

            if(insertIntoStocks !=  null) {
                insertIntoStocks.close();
            }
            if(queryStockById != null) {
                queryStockById.close();
            }

            if(insertIntoOrders !=  null) {
                insertIntoOrders.close();
            }
            if(insertNotValidatedOrder !=  null) {
                insertNotValidatedOrder.close();
            }
            if(queryOrderById != null) {
                queryOrderById.close();
            }
            if(queryOrderByIdWithArticle != null) {
                queryOrderById.close();
            }
            if(queryOrderByIdWithClient != null) {
                queryOrderByIdWithClient.close();
            }
            if(queryOrdersByDateWithClients != null){
                queryOrdersByDateWithClients.close();
            }
            if(updateOrderedArticle != null) {
                updateOrderedArticle.close();
            }
            if (validateOrder != null){
                validateOrder.close();
            }

            if(insertIntoCommodities != null) {
                insertIntoCommodities.close();
            }
            if(updateCommodity != null) {
                updateCommodity.close();
            }

            if(insertIntoClients !=  null) {
                insertIntoClients.close();
            }

            if(conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    //Done
    public List<Article> queryArticles(int sortOrder) {
        StringBuilder sb = new StringBuilder(QUERY_ARTICLES_WITH_COMMODITIES);

        if(sortOrder != ORDER_BY_NONE) {
            sb.append(" ORDER BY ");
            sb.append(COLUMN_ARTICLE_LIBELLE);
            if (sortOrder == ORDER_BY_DESC) {
                sb.append(" DESC");
            } else {
                sb.append(" ASC");
            }
        }

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<Article> articles = new ArrayList<>();
            while (results.next()) {
                try {
                    Thread.sleep(20);
                } catch(InterruptedException e) {
                    System.out.println("Interuppted: " + e.getMessage());
                }

                Article article = new Article();
                Locale locale = new Locale("fr", "Fr");
                DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);

                article.setId(results.getInt(INDEX_ARTICLE_ID));
                article.setCommodityId(results.getInt(INDEX_ARTICLE_COMMODITYID));
                article.setLibelle(results.getString(INDEX_ARTICLE_LIBELLE));
                article.setPrice(results.getFloat(INDEX_ARTICLE_PRICE));
                article.setDateCreated(df.format(new Date(results.getDate(INDEX_ARTICLE_DATECREATED).getTime())));

                articles.add(article);
            }

            return articles;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    //Done
    public List<Client> queryClients(int sortOrder) {
        StringBuilder sb = new StringBuilder(QUERY_CLIENTS);

        if(sortOrder != ORDER_BY_NONE) {
            sb.append(" ORDER BY ");
            sb.append(COLUMN_CLIENT_NAME);
            if (sortOrder == ORDER_BY_DESC) {
                sb.append(" DESC");
            } else {
                sb.append(" ASC");
            }
        }

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {
            List<Client> clients = new ArrayList<>();

            while (results.next()) {
                try {
                    Thread.sleep(20);
                } catch(InterruptedException e) {
                    System.out.println("Interuppted: " + e.getMessage());
                }

                Client client = new Client();

                client.setId(results.getInt(INDEX_CLIENT_ID));
                client.setName(results.getString(INDEX_CLIENT_NAME));
                client.setCity(results.getString(INDEX_CLIENT_CITY));
                client.setAddress(results.getString(INDEX_CLIENT_ADDRESS));
                client.setPhone(results.getString(INDEX_CLIENT_PHONE));

                clients.add(client);
            }

            return clients;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    //Done
    public List<Article> queryArticlesByCommodity(int commodityId, int sortOrder) throws SQLException {
        queryArticlesByCommodity.setInt(1, commodityId);

        try (ResultSet results = queryArticlesByCommodity.executeQuery()) {

            List<Article> articles = new ArrayList<>();
            while (results.next()) {
                try {
                    Thread.sleep(20);
                } catch(InterruptedException e) {
                    System.out.println("Interuppted: " + e.getMessage());
                }

                Article article = new Article();
                Locale locale = new Locale("fr", "Fr");
                DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);

                article.setId(results.getInt(INDEX_ARTICLE_ID));
                article.setCommodityId(results.getInt(INDEX_ARTICLE_COMMODITYID));
                article.setLibelle(results.getString(INDEX_ARTICLE_LIBELLE));
                article.setPrice(results.getFloat(INDEX_ARTICLE_PRICE));
                article.setDateCreated(df.format(new Date(results.getDate(INDEX_ARTICLE_DATECREATED).getTime())));

                articles.add(article);
            }

            return articles;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    //Done
    public List<Article> searchArticles(String libelle) throws SQLException {
        searchArticleByLibelle.setString(1, libelle);

        try (ResultSet results = searchArticleByLibelle.executeQuery()) {

            List<Article> articles = new ArrayList<>();
            while (results.next()) {
                try {
                    Thread.sleep(20);
                } catch(InterruptedException e) {
                    System.out.println("Interuppted: " + e.getMessage());
                }

                Article article = new Article();
                Locale locale = new Locale("fr", "Fr");
                DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);

                article.setId(results.getInt(INDEX_ARTICLE_ID));
                article.setCommodityId(results.getInt(INDEX_ARTICLE_COMMODITYID));
                article.setLibelle(results.getString(INDEX_ARTICLE_LIBELLE));
                article.setPrice(results.getFloat(INDEX_ARTICLE_PRICE));
                article.setDateCreated(df.format(new Date(results.getDate(INDEX_ARTICLE_DATECREATED).getTime())));

                articles.add(article);
            }

            return articles;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    //Done
    public Article queryArticleById(int id) throws SQLException {
        queryArticleById.setInt(1, id);

        try (ResultSet result = queryArticleById.executeQuery()) {
            Article article = new Article();

            if(result.next()) {
                Locale locale = new Locale("fr", "Fr");
                DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);

                article.setId(result.getInt(INDEX_ARTICLE_ID));
                article.setCommodityId(result.getInt(INDEX_ARTICLE_COMMODITYID));
                article.setLibelle(result.getString(INDEX_ARTICLE_LIBELLE));
                article.setPrice(result.getFloat(INDEX_ARTICLE_PRICE));
                article.setDateCreated(df.format(new Date(result.getDate(INDEX_ARTICLE_DATECREATED).getTime())));
            }

            return article;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    //Done
    public Article queryArticleByLibelle(String libelle) throws SQLException {
        queryArticleByLibelle.setString(1, libelle);

        try (ResultSet result = queryArticleByLibelle.executeQuery()) {
            Article article = new Article();

            if(result.next()) {
                Locale locale = new Locale("fr", "Fr");
                DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);

                article.setId(result.getInt(INDEX_ARTICLE_ID));
                article.setCommodityId(result.getInt(INDEX_ARTICLE_COMMODITYID));
                article.setLibelle(result.getString(INDEX_ARTICLE_LIBELLE));
                article.setPrice(result.getFloat(INDEX_ARTICLE_PRICE));
                article.setDateCreated(df.format(new Date(result.getDate(INDEX_ARTICLE_DATECREATED).getTime())));
            }

            return article;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    //Done
    public DisplayArticle queryArticleByIdWithStockAndCommodity(int id) throws SQLException {
        queryArticleByIdWithStockAndCommodity.setInt(1, id);

        try (ResultSet result = queryArticleByIdWithStockAndCommodity.executeQuery()) {

            DisplayArticle article = new DisplayArticle();

            if(result.next()) {
                Locale locale = new Locale("fr", "Fr");
                DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);

                article.setId(result.getInt(INDEX_ARTICLE_ID));
                article.setCommodity(result.getString(7));
                article.setLibelle(result.getString(INDEX_ARTICLE_LIBELLE));
                article.setPrice(result.getFloat(INDEX_ARTICLE_PRICE));
                article.setQuantity(result.getInt(10));
                article.setDateCreated(df.format(new Date(result.getDate(INDEX_ARTICLE_DATECREATED).getTime())));
            }

            return article;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    //Done
    public List<Stock> queryStocks(int sortOrder) {
        StringBuilder sb = new StringBuilder(QUERY_STOCKS);

        if(sortOrder != ORDER_BY_NONE) {
            sb.append(" ORDER BY ");
            sb.append(COLUMN_STOCK_ID);
            if (sortOrder == ORDER_BY_DESC) {
                sb.append(" DESC");
            } else {
                sb.append(" ASC");
            }
        }

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<Stock> stocks = new ArrayList<>();
            while (results.next()) {
                try {
                    Thread.sleep(20);
                } catch(InterruptedException e) {
                    System.out.println("Interuppted: " + e.getMessage());
                }

                Stock stock = new Stock();

                stock.setId(results.getInt(INDEX_ARTICLE_ID));
//                stock.setArticle(results.getInt(INDEX_STOCK_ARTICLEID));
                stock.setInitialQty(results.getInt(INDEX_STOCK_INITIALQTY));

                stocks.add(stock);
            }

            return stocks;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    //Done
    public List<Commodity> queryCommodities(int sortOrder) {
        StringBuilder sb = new StringBuilder(QUERY_COMMODITIES);

        if(sortOrder != ORDER_BY_NONE) {
            sb.append(" ORDER BY ");
            sb.append(COLUMN_COMMODITY_ID);
            if (sortOrder == ORDER_BY_DESC) {
                sb.append(" DESC");
            } else {
                sb.append(" ASC");
            }
        }

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<Commodity> commodities = new ArrayList<>();
            while (results.next()) {
                try {
                    Thread.sleep(20);
                } catch(InterruptedException e) {
                    System.out.println("Interuppted: " + e.getMessage());
                }

                Commodity commodity = new Commodity();

                commodity.setId(results.getInt(INDEX_COMMODITY_ID));
                commodity.setCommodity(results.getString(INDEX_COMMODITY_COMMODITY));

                commodities.add(commodity);
            }

            return commodities;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    //Done
    public Commodity queryCommodity(String commodity) throws SQLException {
        queryCommodity.setString(1, commodity);

        try (ResultSet result = queryCommodity.executeQuery()) {
            Commodity commodity1 = new Commodity();

            if(result.next()) {
                commodity1.setId(result.getInt(INDEX_COMMODITY_ID));
                commodity1.setCommodity(result.getString(INDEX_COMMODITY_COMMODITY));
            }

            return commodity1;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    //Done
    public Commodity queryCommodityById(int commodityId) throws SQLException {
        queryCommodityById.setInt(1, commodityId);

        try (ResultSet result = queryCommodityById.executeQuery()) {
            Commodity commodity1 = new Commodity();

            if(result.next()) {
                commodity1.setId(result.getInt(INDEX_COMMODITY_ID));
                commodity1.setCommodity(result.getString(INDEX_COMMODITY_COMMODITY));
            }

            return commodity1;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    //Done
    public Stock queryStockById(int id) throws SQLException {
        queryStockById.setInt(1, id);

        try (ResultSet result = queryStockById.executeQuery()) {
            Stock stock = new Stock();

            if(result.next()) {
                stock.setId(result.getInt(INDEX_STOCK_ID));
                stock.setArticle(result.getString(INDEX_STOCK_ARTICLEID));
                stock.setInitialQty(result.getInt(INDEX_STOCK_INITIALQTY));
            }

            return stock;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public Stock queryStockByArticleId(int articleId) throws SQLException {
        queryStockByArticleId.setInt(1, articleId);

        try (ResultSet result = queryStockByArticleId.executeQuery()) {
            Stock stock = new Stock();

            if(result.next()) {
                stock.setId(result.getInt(INDEX_STOCK_ID));
                stock.setArticle(result.getString(INDEX_STOCK_ARTICLEID));
                stock.setInitialQty(result.getInt(INDEX_STOCK_INITIALQTY));
            }

            return stock;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    //Done?clickedA
    public List<DisplayOrder> queryOrdersWithArticles(int sortOrder) {
        StringBuilder sb = new StringBuilder(QUERY_ORDERS_WITH_ARTICLES_AND_CLIENTS);

        if(sortOrder != ORDER_BY_NONE) {
            sb.append(" ORDER BY ");
            sb.append(COLUMN_ORDER_ID);
            if (sortOrder == ORDER_BY_DESC) {
                sb.append(" DESC");
            } else {
                sb.append(" ASC");
            }
        }

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<DisplayOrder> displayOrders = new ArrayList<>();
            while (results.next()) {
                try {
                    Thread.sleep(20);
                } catch(InterruptedException e) {
                    System.out.println("Interuppted: " + e.getMessage());
                }

                Locale locale = new Locale("fr", "Fr");
                DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);

                DisplayOrder displayOrder = new DisplayOrder();
                displayOrder.setId(results.getInt(INDEX_ORDER_ID));
//                displayOrder.setQuantity(results.getInt(INDEX_ORDER_QUANTITY));
//                displayOrder.setArticleId(results.getInt(INDEX_ORDER_ARTICLEID));
//                displayOrder.setArticleLibelle(results.getString(7));
                displayOrder.setDateCreated(df.format(new Date(results.getDate(INDEX_ORDER_DATECREATED).getTime())));

                displayOrders.add(displayOrder);
            }

            return displayOrders;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    //Done
    public List<DisplayOrder> queryOrdersWithClients(int sortOrder) {
        StringBuilder sb = new StringBuilder(QUERY_ORDERS_WITH_CLIENTS);

        if(sortOrder != ORDER_BY_NONE) {
            sb.append(" ORDER BY ");
            sb.append(TABLE_CLIENTS+"."+COLUMN_CLIENT_NAME);
            if (sortOrder == ORDER_BY_DESC) {
                sb.append(" DESC");
            } else {
                sb.append(" ASC");
            }
        }

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<DisplayOrder> displayOrders = new ArrayList<>();
            while (results.next()) {
                try {
                    Thread.sleep(20);
                } catch(InterruptedException e) {
                    System.out.println("Interuppted: " + e.getMessage());
                }
                DisplayOrder displayOrder = new DisplayOrder();
                Locale locale = new Locale("fr", "Fr");
                DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);

                displayOrder.setId(results.getInt(1));
                displayOrder.setClientName(results.getString(3));
                displayOrder.setDateCreated(df.format(new Date(results.getDate(2).getTime())));

                displayOrders.add(displayOrder);
            }

            return displayOrders;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    //Done
    public List<DisplayOrder> queryNotValidatedOrdersWithClients(int sortOrder) {
        StringBuilder sb = new StringBuilder(QUERY_NOT_VALIDATED_ORDERS_WITH_CLIENTS);

        if(sortOrder != ORDER_BY_NONE) {
            sb.append(" ORDER BY ");
            sb.append(TABLE_CLIENTS+"."+COLUMN_CLIENT_NAME);
            if (sortOrder == ORDER_BY_DESC) {
                sb.append(" DESC");
            } else {
                sb.append(" ASC");
            }
        }

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<DisplayOrder> displayOrders = new ArrayList<>();
            while (results.next()) {
                try {
                    Thread.sleep(20);
                } catch(InterruptedException e) {
                    System.out.println("Interuppted: " + e.getMessage());
                }
                DisplayOrder displayOrder = new DisplayOrder();
                Locale locale = new Locale("fr", "Fr");
                DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);

                displayOrder.setId(results.getInt(1));
                displayOrder.setClientName(results.getString(3));
                displayOrder.setDateCreated(df.format(new Date(results.getDate(2).getTime())));

                displayOrders.add(displayOrder);
            }

            return displayOrders;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    //Done
    public List<DisplayOrder> queryOrdersWithClientsByDate(java.sql.Date date) throws SQLException {
        queryOrdersByDateWithClients.setDate(1, date);
        try (ResultSet results = queryOrdersByDateWithClients.executeQuery()) {

            List<DisplayOrder> displayOrders = new ArrayList<>();
            while (results.next()) {
                try {
                    Thread.sleep(20);
                } catch(InterruptedException e) {
                    System.out.println("Interuppted: " + e.getMessage());
                }
                DisplayOrder displayOrder = new DisplayOrder();
                Locale locale = new Locale("fr", "Fr");
                DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);

                displayOrder.setId(results.getInt(1));
                displayOrder.setClientName(results.getString(3));
                displayOrder.setDateCreated(df.format(new Date(results.getDate(2).getTime())));

                displayOrders.add(displayOrder);
            }

            return displayOrders;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    //Done?clickedA
    public DisplayOrder queryOrderById(int id) throws SQLException {
        queryOrderById.setInt(1, id);

        try (ResultSet result = queryOrderById.executeQuery()) {

            DisplayOrder displayOrder = new DisplayOrder();
            if(result.next()) {
//                order.setId(result.getInt(INDEX_ARTICLE_ID));
//                order.setLibelle(result.getString(INDEX_ARTICLE_LIBELLE));
//                article.setPrice(result.getFloat(INDEX_ARTICLE_PRICE));
//                article.setDateCreated(new Date(result.getDate(INDEX_ARTICLE_DATECREATED).getTime()).toString());
            }

            return displayOrder;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    //Done?
    public DisplayOrder queryOrderByIdWithArticle(int id) throws SQLException {
        queryOrderByIdWithArticle.setInt(1, id);

        try (ResultSet result = queryOrderByIdWithArticle.executeQuery()) {

            DisplayOrder displayOrder = new DisplayOrder();
            if(result.next()) {
                Locale locale = new Locale("fr", "Fr");
                DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);

                displayOrder.setId(result.getInt(INDEX_ORDER_ID));
//                displayOrder.setArticleId(result.getInt(INDEX_ORDER_ARTICLEID));
//                displayOrder.setArticleLibelle(result.getString(7));
//                displayOrder.setQuantity(result.getInt(INDEX_ORDER_QUANTITY));
                displayOrder.setDateCreated(df.format(new Date(result.getDate(INDEX_ORDER_DATECREATED).getTime())));
            }

            return displayOrder;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    //Done
    public Result insertArticle(int commodityId, String libelle, float price, java.sql.Date date) throws SQLException {
        String normalizedLib = Validator.normalize(libelle);

        queryArticleByLibelle.setString(1, normalizedLib);

        try (ResultSet result = queryArticleByLibelle.executeQuery()){
            if(!result.next()){
                insertIntoArticles.setInt(1, commodityId);
                insertIntoArticles.setString(2, normalizedLib);
                insertIntoArticles.setFloat(3, price);
                insertIntoArticles.setDate(4, date);

                int affectedRows = insertIntoArticles.executeUpdate();
                if(affectedRows != 1){
                    return new Result(false, "Une erreur s'est produite.\n\nReessayez!", 0);
                }

                ResultSet generatedKeys = insertIntoArticles.getGeneratedKeys();
                if(generatedKeys.next()) {
                    System.out.println("in gen next article");
                    System.out.println(generatedKeys.getInt(INDEX_ARTICLE_ID));
                    return new Result(true, "", generatedKeys.getInt(INDEX_ARTICLE_ID));
                } else {
                    throw new SQLException("Couldn't get generated keys");
                }
            }
            else {
                return new Result(false, "Ce Libellé existe déja\n\nAllez dans 'Stock' si voulez ajouter\nsur le stock", 0);
            }
        } catch (SQLException e){
            return new Result(false, "Une erreur s'est produite.\n\nReessayez!", 0);
        }
    }

    //Done
    public int insertStock(int articleId, int initialQuantity) throws SQLException {
        insertIntoStocks.setInt(1, articleId);
        insertIntoStocks.setInt(2, initialQuantity);

        int affectedRows = insertIntoStocks.executeUpdate();
        if(affectedRows != 1){
            throw new SQLException("Insert failed!");
        };

        ResultSet generatedKeys = insertIntoStocks.getGeneratedKeys();
        if(generatedKeys.next()) {
            System.out.println("in gen next stock");
            System.out.println(generatedKeys.getInt(INDEX_STOCK_ID));

            return generatedKeys.getInt(INDEX_STOCK_ID);
        } else {
            throw new SQLException("Couldn't get generated keys");
        }
    }

    //Done
    public boolean updateStock(int articleId, int quantity, int operation) throws SQLException {
        queryStockByArticleId.setInt(1, articleId);

        try (ResultSet result = queryStockByArticleId.executeQuery()) {
            if(result.next()) {
                int newQty;

                System.out.println("stock found");
                if(operation == UPDATE_STOCK_TO_ADD){
                    newQty = result.getInt(INDEX_STOCK_INITIALQTY) + quantity;
                }
                else{
                    newQty = result.getInt(INDEX_STOCK_INITIALQTY) - quantity;
                }

                updateStock.setInt(1, newQty);
                updateStock.setInt(2, articleId);

                int affectedRows = updateStock.executeUpdate();
                return affectedRows == 1;
            }
            else {
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return false;
        }
    }

    //Done
    public boolean updateOrderDeliveryState(int orderId, String state, java.sql.Date date) throws SQLException {
        try {
            updateOrderDeliveryState.setString(1, state);
            updateOrderDeliveryState.setDate(2, date);
            updateOrderDeliveryState.setInt(3, orderId);

            int affectedRows = updateOrderDeliveryState.executeUpdate();
            return affectedRows == 1;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return false;
        }
    }

    //Done
    public Result updateArticle(int id, int commodityId, String libelle, float price) throws SQLException {
        String normalizedLib = Validator.normalize(libelle);

        queryArticleByLibelle.setString(1, normalizedLib);

        try (ResultSet result = queryArticleByLibelle.executeQuery()){
            boolean resSuccess = result.next();

            if(resSuccess && result.getInt(INDEX_ARTICLE_ID) != id){
                return new Result(false, "Ce Libellé existe déja", 0);
            }
            else {
                updateArticle.setInt(1, commodityId);
                updateArticle.setString(2, normalizedLib);
                updateArticle.setFloat(3, price);
                updateArticle.setInt(4, id);

                int affectedRows = updateArticle.executeUpdate();
                if(affectedRows == 1){
                    return new Result(true, "", affectedRows);
                }
                else {
                    return new Result(false, "Oupss! Une erreur s'est produite.\n\nReessayez!", 0);
                }
            }
        } catch (SQLException e){
            return new Result(false, "Oupss! Une erreur s'est produite.\n\nReessayez!", 0);
        }
    }

    //Done
    public boolean updateOrderedArticle(int orderId, int articleId, int quantity, double total) throws SQLException {
        updateOrderedArticle.setInt(1, quantity);
        updateOrderedArticle.setDouble(2, total);
        updateOrderedArticle.setInt(3, orderId);
        updateOrderedArticle.setInt(4, articleId);

        int affectedRows = updateOrderedArticle.executeUpdate();
        return affectedRows == 1;
    }

    //Done
    public boolean validateOrder(int orderId) throws SQLException {
        validateOrder.setInt(1, orderId);

        int affectedRows = validateOrder.executeUpdate();
        return affectedRows == 1;
    }

    //Done
    public Result updateCommodity(int id, String commodity) throws SQLException {
        String normalizedLib = Validator.normalize(commodity);

        queryCommodity.setString(1, normalizedLib);

        try (ResultSet result = queryCommodity.executeQuery()){
            boolean resSuccess = result.next();

            if(resSuccess && result.getInt(INDEX_COMMODITY_ID) == id){
                return new Result(true, "", 1);
            }
            else if(resSuccess && result.getInt(INDEX_COMMODITY_ID) != id){
                return new Result(false, "Cette famille existe déja", 0);
            }
            else {
                updateCommodity.setString(1, commodity);
                updateCommodity.setInt(2, id);

                int affectedRows = updateCommodity.executeUpdate();

                if(affectedRows == 1){
                    return new Result(true, "", affectedRows);
                }
                else {
                    return new Result(false, "Oupss! Une erreur s'est produite.\n\nReessayez!", 0);
                }
            }
        } catch (SQLException e){
            return new Result(false, "Oupss! Une erreur s'est produite.\n\nReessayez!", 0);
        }
    }

    //Done
    public Result insertCommodity(String commodity) throws SQLException {
        String normalizedCom = Validator.normalize(commodity);

        queryCommodity.setString(1, normalizedCom);

        try (ResultSet result = queryCommodity.executeQuery()){
            if(!result.next()){
                insertIntoCommodities.setString(1, commodity);

                int affectedRows = insertIntoCommodities.executeUpdate();

                if(affectedRows != 1){
                    return new Result(false, "Une erreur s'est produite.\n\nReessayez!", 0);
                }
                else {
                    ResultSet generatedKeys = insertIntoCommodities.getGeneratedKeys();
                    if(generatedKeys.next()) {
                        System.out.println("in gen next com");
                        System.out.println(generatedKeys.getInt(INDEX_COMMODITY_ID));
                        return new Result(true, "", generatedKeys.getInt(INDEX_COMMODITY_ID));
                    } else {
                        throw new SQLException("Couldn't get generated keys");
                    }
                }
            }
            else {
                return new Result(false, "Cette famille existe déja\n", 0);
            }
        } catch (SQLException e){
            return new Result(false, "Une erreur s'est produite.\n\nReessayez!", 0);
        }
    }

    //Done
    public int insertClient(String name, String city, String address, String phone) throws SQLException {
        insertIntoClients.setString(1, name);
        insertIntoClients.setString(2, city);
        insertIntoClients.setString(3, address);
        insertIntoClients.setString(4, phone);

        int affectedRows = insertIntoClients.executeUpdate();
        if(affectedRows != 1){
            throw new SQLException("Insert failed!");
        }

        ResultSet generatedKeys = insertIntoClients.getGeneratedKeys();
        if(generatedKeys.next()) {
            System.out.println("in gen next stock");
            System.out.println(generatedKeys.getInt(INDEX_CLIENT_ID));

            return generatedKeys.getInt(INDEX_CLIENT_ID);
        } else {
            throw new SQLException("Couldn't get generated keys");
        }
    }

    //Done
    public int insertOrder(int clientId, java.sql.Date date) throws SQLException {
        insertIntoOrders.setInt(1, clientId);
        insertIntoOrders.setDate(2, date);

        int affectedRows = insertIntoOrders.executeUpdate();
        if(affectedRows != 1){
            throw new SQLException("Insert failed!");
        }

        ResultSet generatedKeys = insertIntoOrders.getGeneratedKeys();
        if(generatedKeys.next()) {
            System.out.println("in gen next order");
            System.out.println(generatedKeys.getInt(INDEX_ORDER_ID));

            return generatedKeys.getInt(INDEX_ORDER_ID);
        } else {
            throw new SQLException("Couldn't get generated keys");
        }
    }

    //Done
    public int insertNotValidatedOrder(int clientId, java.sql.Date date) throws SQLException {
        insertNotValidatedOrder.setInt(1, clientId);
        insertNotValidatedOrder.setDate(2, date);

        int affectedRows = insertNotValidatedOrder.executeUpdate();
        if(affectedRows != 1){
            throw new SQLException("Insert failed!");
        }

        ResultSet generatedKeys = insertNotValidatedOrder.getGeneratedKeys();
        if(generatedKeys.next()) {
            System.out.println("in gen next order");
            System.out.println(generatedKeys.getInt(INDEX_ORDER_ID));

            return generatedKeys.getInt(INDEX_ORDER_ID);
        } else {
            throw new SQLException("Couldn't get generated keys");
        }
    }

    //Done
    public boolean insertOrderedArticle(int articleId, int orderId, int quantity, double total) throws SQLException {
        insertIntoOrderedArticles.setInt(1, articleId);
        insertIntoOrderedArticles.setInt(2, orderId);
        insertIntoOrderedArticles.setInt(3, quantity);
        insertIntoOrderedArticles.setDouble(4, total);

        int affectedRows = insertIntoOrderedArticles.executeUpdate();
        return affectedRows == 1;
    }

    //Done
    public DisplayOrder queryOrderByIdWithClient(int id) throws SQLException {
        queryOrderByIdWithClient.setInt(1, id);

        try (ResultSet result = queryOrderByIdWithClient.executeQuery()) {
            DisplayOrder displayOrder = new DisplayOrder();

            if(result.next()) {
                Locale locale = new Locale("fr", "Fr");
                DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);

                displayOrder.setId(result.getInt(1));
                displayOrder.setDateCreated(df.format(new Date(result.getDate(3).getTime())));
                if(result.getString(4) != null){
                    displayOrder.setDeliveryState(result.getString(4));
                }
                else {
                    displayOrder.setDeliveryState(null);
                }
                if(result.getDate(5) != null){
                    displayOrder.setDeliveredDate(df.format(new Date(result.getDate(5).getTime())));
                }
                else {
                    displayOrder.setDeliveredDate(null);
                }
                displayOrder.setClientName(result.getString(8));
                displayOrder.setClientCity(result.getString(9));
                displayOrder.setClientAddress(result.getString(10));
                displayOrder.setClientPhone(result.getString(11));
            }

            return displayOrder;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    //Done
    public List<Purchase> queryOrderedArticlesByOrderId(int id) throws SQLException {
        queryOrderedArticlesByOrderId.setInt(1, id);

        try (ResultSet results = queryOrderedArticlesByOrderId.executeQuery()) {
            List<Purchase> orderedArticles = new ArrayList<>();

            while (results.next()) {
                try {
                    Thread.sleep(20);
                } catch(InterruptedException e) {
                    System.out.println("Interuppted: " + e.getMessage());
                }

                Purchase purchase = new Purchase();

                purchase.setArticleId(results.getInt(1));
                purchase.setLibelle(results.getString(2));
                purchase.setQuantity(results.getInt(3));
                purchase.setTotal(results.getFloat(4));

                orderedArticles.add(purchase);
            }

            return orderedArticles;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }


                    //AUTH
    public Result authenticate(String username, String password) throws SQLException {
        PreparedStatement authRequest = conn.prepareStatement("SELECT * FROM users WHERE username=?");
        authRequest.setString(1, username);

        try {
            Thread.sleep(1500);
        } catch(InterruptedException e) {
            System.out.println("Interuppted: " + e.getMessage());
        }

        try (ResultSet result = authRequest.executeQuery()){
            if(result.next()){
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
                String hexHash = bytesToHex(hash);

                if (hexHash.equals(result.getString(4))){
                    return new Result(true, "", result.getInt(1));
                }
                else {
                    return new Result(false, "Username ou Password incorret", 0);
                }
            }
            else {
                return new Result(false, "Username ou Password incorrect", 0);
            }
        } catch (SQLException | NoSuchAlgorithmException e){
            return new Result(false, "Erreur ! Reessayez.", 0);
        }
//        ea71c25a7a602246b4c39824b855678894a96f43bb9b71319c39700a1e045222
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
