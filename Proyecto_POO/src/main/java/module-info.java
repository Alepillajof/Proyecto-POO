module proyecto_poo {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.proyecto.proyecto_poo.app to javafx.fxml;
    opens com.proyecto.proyecto_poo.controller to javafx.fxml;
    opens com.proyecto.proyecto_poo.model to javafx.base, javafx.fxml;

    exports com.proyecto.proyecto_poo.app;
    exports com.proyecto.proyecto_poo.controller;
    exports com.proyecto.proyecto_poo.model;

}