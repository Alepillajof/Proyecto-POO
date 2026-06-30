package com.proyecto.proyecto_poo.controller;

import com.proyecto.proyecto_poo.dao.UsuarioDAO;
import com.proyecto.proyecto_poo.model.Usuario;
import com.proyecto.proyecto_poo.util.Sesion;
import com.proyecto.proyecto_poo.app.MainApp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private void iniciarSesion(ActionEvent event) {

        UsuarioDAO dao = new UsuarioDAO();

        Usuario usuario = dao.iniciarSesion(
                txtUsuario.getText(),
                txtContrasena.getText()
        );

        if(usuario != null){

            Sesion.setUsuario(usuario);

            abrirDashboard();

        }else{

            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Error");

            alert.setHeaderText(null);

            alert.setContentText("Usuario o contraseña incorrectos.");

            alert.showAndWait();

        }

    }

    private void abrirDashboard(){

        try{

            FXMLLoader loader = new FXMLLoader(
                    MainApp.class.getResource("/view/dashboard.fxml")
            );

            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) txtUsuario.getScene().getWindow();

            stage.setScene(scene);

            stage.setTitle("Dashboard");

            stage.show();

        }catch(Exception e){

            e.printStackTrace();

        }

    }

}