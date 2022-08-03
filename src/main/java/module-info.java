module com.konon.farm {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.konon.farm to javafx.fxml;
    opens com.konon.farm.app to javafx.fxml;
    exports com.konon.farm.app;
    opens com.konon.farm.controllers to javafx.fxml;
    exports com.konon.farm.controllers;
    opens com.konon.farm.product to javafx.fxml;
    exports com.konon.farm.product;
    opens com.konon.farm.employee to javafx.fxml;
    exports com.konon.farm.employee;
}