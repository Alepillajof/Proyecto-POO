package com.proyecto.proyecto_poo.controller;

import com.proyecto.proyecto_poo.dao.EstudianteDAO;
import com.proyecto.proyecto_poo.model.Estudiante;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class EstudianteController {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtApellido;

    @FXML
    private TextField txtCedula;

    @FXML
    private TextField txtCarrera;

    @FXML
    private TextField txtNivel;

    @FXML
    private TextField txtCorreo;

    @FXML
    private TableView<Estudiante> tablaEstudiantes;

    @FXML
    private TableColumn<Estudiante, Integer> colId;

    @FXML
    private TableColumn<Estudiante, String> colNombre;

    @FXML
    private TableColumn<Estudiante, String> colApellido;

    @FXML
    private TableColumn<Estudiante, String> colCedula;

    @FXML
    private TableColumn<Estudiante, String> colCarrera;

    @FXML
    private TableColumn<Estudiante, Integer> colNivel;

    private final EstudianteDAO dao = new EstudianteDAO();

    private final ObservableList<Estudiante> lista =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colCarrera.setCellValueFactory(new PropertyValueFactory<>("carrera"));
        colNivel.setCellValueFactory(new PropertyValueFactory<>("nivel"));

        cargarTabla();

        tablaEstudiantes.getSelectionModel().selectedItemProperty().addListener(
                (observable, anterior, estudiante) -> {

                    if (estudiante != null) {

                        txtNombre.setText(estudiante.getNombre());
                        txtApellido.setText(estudiante.getApellido());
                        txtCedula.setText(estudiante.getCedula());
                        txtCarrera.setText(estudiante.getCarrera());
                        txtNivel.setText(String.valueOf(estudiante.getNivel()));
                        txtCorreo.setText(estudiante.getCorreo());

                    }

                });

    }

    private void cargarTabla() {

        lista.clear();
        lista.addAll(dao.listar());
        tablaEstudiantes.setItems(lista);

    }

    @FXML
    private void guardar() {

        try {

            Estudiante estudiante = new Estudiante();

            estudiante.setNombre(txtNombre.getText());
            estudiante.setApellido(txtApellido.getText());
            estudiante.setCedula(txtCedula.getText());
            estudiante.setCarrera(txtCarrera.getText());
            estudiante.setNivel(Integer.parseInt(txtNivel.getText()));
            estudiante.setCorreo(txtCorreo.getText());

            if (dao.guardar(estudiante)) {
                limpiar();
                cargarTabla();
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El nivel debe ser un número entero");
        }
    }

    @FXML
    private void actualizar() {

        Estudiante estudiante = tablaEstudiantes.getSelectionModel().getSelectedItem();

        if (estudiante == null) {
            return;
        }

        try {

            estudiante.setNombre(txtNombre.getText());
            estudiante.setApellido(txtApellido.getText());
            estudiante.setCedula(txtCedula.getText());
            estudiante.setCarrera(txtCarrera.getText());
            estudiante.setNivel(Integer.parseInt(txtNivel.getText()));
            estudiante.setCorreo(txtCorreo.getText());

            dao.actualizar(estudiante);

            limpiar();
            cargarTabla();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El nivel debe ser un número entero");
        }
    }

    @FXML
    private void eliminar() {

        Estudiante estudiante = tablaEstudiantes.getSelectionModel().getSelectedItem();

        if (estudiante == null) {
            return;
        }

        dao.eliminar(estudiante.getId());

        limpiar();
        cargarTabla();
    }

    private void limpiar() {

        txtNombre.clear();
        txtApellido.clear();
        txtCedula.clear();
        txtCarrera.clear();
        txtNivel.clear();
        txtCorreo.clear();

        tablaEstudiantes.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensaje) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}