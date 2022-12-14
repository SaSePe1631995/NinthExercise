module com.example.ninthexercise {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens com.example.ninthexercise to javafx.fxml;
    exports com.example.ninthexercise;
    exports com.example.ninthexercise.controllers;
    opens com.example.ninthexercise.controllers to javafx.fxml;
}