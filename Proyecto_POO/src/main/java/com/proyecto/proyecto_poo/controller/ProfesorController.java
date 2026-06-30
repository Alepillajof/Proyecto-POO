package com.proyecto.proyecto_poo.controller;

import com.proyecto.proyecto_poo.dao.ProfesorDAO;
import com.proyecto.proyecto_poo.model.Profesor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ProfesorController {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtApellido;

    @FXML
    private TextField txtCedula;

    @FXML
    private TextField txtEspecialidad;

    @FXML
    private TextField txtCorreo;

    @FXML
    private TableView<Profesor> tablaProfesores;

    @FXML
    private TableColumn<Profesor,Integer> colId;

    @FXML
    private TableColumn<Profesor,String> colNombre;

    @FXML
    private TableColumn<Profesor,String> colApellido;

    @FXML
    private TableColumn<Profesor,String> colCedula;

    @FXML
    private TableColumn<Profesor,String> colEspecialidad;

    private final ProfesorDAO dao = new ProfesorDAO();

    private final ObservableList<Profesor> lista =
            FXCollections.observableArrayList();

    @FXML
    public void initialize(){

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));

        cargarTabla();

        tablaProfesores.getSelectionModel().selectedItemProperty().addListener(
                (observable, anterior, profesor) -> {

                    if(profesor != null){

                        txtNombre.setText(profesor.getNombre());
                        txtApellido.setText(profesor.getApellido());
                        txtCedula.setText(profesor.getCedula());
                        txtEspecialidad.setText(profesor.getEspecialidad());
                        txtCorreo.setText(profesor.getCorreo());

                    }

                });

    }

    private void cargarTabla(){

        lista.clear();

        lista.addAll(dao.listar());

        tablaProfesores.setItems(lista);

    }

    @FXML
    private void guardar(){

        Profesor profesor = new Profesor();

        profesor.setNombre(txtNombre.getText());
        profesor.setApellido(txtApellido.getText());
        profesor.setCedula(txtCedula.getText());
        profesor.setEspecialidad(txtEspecialidad.getText());
        profesor.setCorreo(txtCorreo.getText());

        if(dao.guardar(profesor)){

            limpiar();

            cargarTabla();

        }

    }

    @FXML
    private void actualizar(){

        Profesor profesor = tablaProfesores.getSelectionModel().getSelectedItem();

        if(profesor == null){

            return;

        }

        profesor.setNombre(txtNombre.getText());
        profesor.setApellido(txtApellido.getText());
        profesor.setCedula(txtCedula.getText());
        profesor.setEspecialidad(txtEspecialidad.getText());
        profesor.setCorreo(txtCorreo.getText());

        dao.actualizar(profesor);

        limpiar();

        cargarTabla();

    }

    @FXML
    private void eliminar(){

        Profesor profesor = tablaProfesores.getSelectionModel().getSelectedItem();

        if(profesor == null){

            return;

        }

        dao.eliminar(profesor.getId());

        limpiar();

        cargarTabla();

    }

    private void limpiar(){

        txtNombre.clear();
        txtApellido.clear();
        txtCedula.clear();
        txtEspecialidad.clear();
        txtCorreo.clear();

        tablaProfesores.getSelectionModel().clearSelection();

    }

}