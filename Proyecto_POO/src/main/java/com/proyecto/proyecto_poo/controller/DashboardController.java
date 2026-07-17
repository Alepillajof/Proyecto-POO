package com.proyecto.proyecto_poo.controller;

import com.proyecto.proyecto_poo.app.MainApp;
import com.proyecto.proyecto_poo.model.Usuario;
import com.proyecto.proyecto_poo.util.Sesion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class DashboardController {

    // ... (tus campos @FXML existentes) ...
    @FXML private Label lblUsuario;
    @FXML private Label lblRol;
    @FXML private Button btnProfesores;
    @FXML private Button btnEstudiantes;
    @FXML private Button btnReportes;
    @FXML private AnchorPane panelPrincipal;

    // AÑADE ESTAS CONSTANTES PARA LOS ESTILOS
    private final String ESTILO_ACTIVO = "-fx-background-color: #7c3aed; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-alignment: BASELINE_LEFT; -fx-padding: 0 0 0 20;";
    private final String ESTILO_INACTIVO = "-fx-background-color: transparent; -fx-text-fill: #c0c6d9; -fx-font-size: 14px; -fx-alignment: BASELINE_LEFT; -fx-padding: 0 0 0 20;";

    @FXML
    public void initialize() {
        Usuario usuario = Sesion.getUsuario();
        lblUsuario.setText("Bienvenido: " + usuario.getNombre());
        lblRol.setText("Rol: " + usuario.getRol());
        configurarPermisos(usuario.getRol());
    }

    private void configurarPermisos(String rol){

        switch (rol){

            case "ADMIN":
                // Si es admin, no hacemos nada (todo está habilitado)
                break;

            case "PROFESOR":
                btnProfesores.setDisable(true);
                break;

            case "ESTUDIANTE":
                btnProfesores.setDisable(true);
                btnReportes.setDisable(true);
                break;
        }
    }

    // AÑADE ESTE MÉTODO AUXILIAR
    private void activarBoton(Button botonActivo) {
        // Limpiamos estilos y quitamos clase activo
        btnProfesores.getStyleClass().remove("boton-menu-activo");
        btnEstudiantes.getStyleClass().remove("boton-menu-activo");
        btnReportes.getStyleClass().remove("boton-menu-activo");

        // Aplicamos clase activo al nuevo
        botonActivo.getStyleClass().add("boton-menu-activo");
    }

    // ACTUALIZA TUS MÉTODOS DE NAVEGACIÓN
    @FXML
    private void abrirProfesores(){
        cargarVista("/view/profesores.fxml");
        activarBoton(btnProfesores);
    }

    @FXML
    private void abrirEstudiantes(){
        cargarVista("/view/estudiantes.fxml");
        activarBoton(btnEstudiantes);
    }

    @FXML
    private void abrirReportes(){
        cargarVista("/view/reportes.fxml");
        activarBoton(btnReportes);
    }

    private void cargarVista(String ruta){
        try{
            Parent vista = FXMLLoader.load(MainApp.class.getResource(ruta));
            panelPrincipal.getChildren().clear();
            panelPrincipal.getChildren().add(vista);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void cerrarSesion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/view/login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) lblUsuario.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ... (tu método cerrarSesion sigue igual) ...
}