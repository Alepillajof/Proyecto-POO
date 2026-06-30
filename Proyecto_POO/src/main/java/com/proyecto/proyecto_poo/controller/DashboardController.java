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

    @FXML
    private Label lblUsuario;

    @FXML
    private Label lblRol;

    @FXML
    private Button btnProfesores;

    @FXML
    private Button btnEstudiantes;

    @FXML
    private Button btnReportes;

    @FXML
    private AnchorPane panelPrincipal;

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

    @FXML
    private void abrirProfesores(){

        cargarVista("/view/profesores.fxml");

    }

    @FXML
    private void abrirEstudiantes(){

        cargarVista("/view/estudiantes.fxml");

    }

    @FXML
    private void abrirReportes(){

        cargarVista("/view/reportes.fxml");

    }

    private void cargarVista(String ruta){

        try{

            Parent vista = FXMLLoader.load(
                    MainApp.class.getResource(ruta)
            );

            panelPrincipal.getChildren().clear();

            panelPrincipal.getChildren().add(vista);

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    @FXML
    private void cerrarSesion(ActionEvent e){

        try{

            FXMLLoader loader = new FXMLLoader(
                    MainApp.class.getResource("/view/login.fxml")
            );

            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) lblUsuario.getScene().getWindow();

            stage.setScene(scene);

            stage.show();

        }catch(Exception ex){

            ex.printStackTrace();

        }

    }

}