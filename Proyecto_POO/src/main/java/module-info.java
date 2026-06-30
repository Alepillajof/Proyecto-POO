module com.proyecto.proyecto_poo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.proyecto.proyecto_poo to javafx.fxml;
    exports com.proyecto.proyecto_poo;
}