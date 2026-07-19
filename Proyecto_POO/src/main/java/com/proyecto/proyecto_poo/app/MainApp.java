package com.proyecto.proyecto_poo.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                MainApp.class.getResource("/view/login.fxml")
        );

        Scene scene = new Scene(loader.load());

        stage.setTitle("Sistema de Gestión Educativa");
        stage.setScene(scene);

        stage.setResizable(true);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}