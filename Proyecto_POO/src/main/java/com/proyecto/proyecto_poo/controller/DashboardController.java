package com.proyecto.proyecto_poo.controller;

import com.proyecto.proyecto_poo.model.Usuario;
import com.proyecto.proyecto_poo.util.Sesion;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML
    private Label lblBienvenida;

    @FXML
    public void initialize(){

        Usuario usuario = Sesion.getUsuario();

        if(usuario != null){

            System.out.println("Bienvenido " + usuario.getNombre());

            System.out.println("Rol: " + usuario.getRol());

        }

    }

}