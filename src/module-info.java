module OrderMng {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;

    opens com.rb.odmg;
    opens com.rb.odmg.Model;
    opens com.rb.odmg.Controller;
}