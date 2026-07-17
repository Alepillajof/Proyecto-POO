package com.proyecto.proyecto_poo.controller;

import com.proyecto.proyecto_poo.dao.ProfesorDAO;
import com.proyecto.proyecto_poo.model.Profesor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList; // Import necesario
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ProfesorController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtCedula;
    @FXML private TextField txtEspecialidad;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtBuscar; // ID del campo de búsqueda en FXML

    @FXML private TableView<Profesor> tablaProfesores;
    @FXML private TableColumn<Profesor, Integer> colId;
    @FXML private TableColumn<Profesor, String> colNombre;
    @FXML private TableColumn<Profesor, String> colApellido;
    @FXML private TableColumn<Profesor, String> colCedula;
    @FXML private TableColumn<Profesor, String> colEspecialidad;

    private final ProfesorDAO dao = new ProfesorDAO();
    private final ObservableList<Profesor> lista = FXCollections.observableArrayList();
    private FilteredList<Profesor> filteredData; // Lista filtrada

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));

        cargarTabla();

        // Configuración del buscador
        filteredData = new FilteredList<>(lista, b -> true);

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(profesor -> {
                if (newValue == null || newValue.isEmpty()) return true;

                String lowerCaseFilter = newValue.toLowerCase();

                // Filtra por nombre, apellido o cédula
                if (profesor.getNombre().toLowerCase().contains(lowerCaseFilter)) return true;
                if (profesor.getApellido().toLowerCase().contains(lowerCaseFilter)) return true;
                if (profesor.getCedula().contains(lowerCaseFilter)) return true;

                return false;
            });
        });

        tablaProfesores.setItems(filteredData); // Vinculamos la lista filtrada

        tablaProfesores.getSelectionModel().selectedItemProperty().addListener(
                (observable, anterior, profesor) -> {
                    if (profesor != null) {
                        txtNombre.setText(profesor.getNombre());
                        txtApellido.setText(profesor.getApellido());
                        txtCedula.setText(profesor.getCedula());
                        txtEspecialidad.setText(profesor.getEspecialidad());
                        txtCorreo.setText(profesor.getCorreo());
                    }
                });
    }

    private void cargarTabla() {
        lista.clear();
        lista.addAll(dao.listar());
        // No hace falta setear setItems aquí porque el FilteredList ya está vinculado
    }

    @FXML
    private void guardar() {
        if (!validarCampos()) return;

        Profesor profesor = new Profesor();
        profesor.setNombre(txtNombre.getText().trim());
        profesor.setApellido(txtApellido.getText().trim());
        profesor.setCedula(txtCedula.getText().trim());
        profesor.setEspecialidad(txtEspecialidad.getText().trim());
        profesor.setCorreo(txtCorreo.getText().trim());

        if (dao.guardar(profesor)) {
            limpiar();
            cargarTabla();
        }
    }

    @FXML
    private void actualizar() {
        Profesor profesor = tablaProfesores.getSelectionModel().getSelectedItem();
        if (profesor == null) {
            mostrarAlerta("Aviso", "Seleccione un profesor para actualizar.");
            return;
        }
        if (!validarCampos()) return;

        profesor.setNombre(txtNombre.getText().trim());
        profesor.setApellido(txtApellido.getText().trim());
        profesor.setCedula(txtCedula.getText().trim());
        profesor.setEspecialidad(txtEspecialidad.getText().trim());
        profesor.setCorreo(txtCorreo.getText().trim());

        dao.actualizar(profesor);
        limpiar();
        cargarTabla();
    }

    @FXML
    private void eliminar() {
        Profesor profesor = tablaProfesores.getSelectionModel().getSelectedItem();
        if (profesor == null) {
            mostrarAlerta("Aviso", "Seleccione un profesor para eliminar.");
            return;
        }
        dao.eliminar(profesor.getId());
        limpiar();
        cargarTabla();
    }

    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty()) { mostrarAlerta("Campo vacío", "Ingrese el nombre."); return false; }
        if (txtApellido.getText().trim().isEmpty()) { mostrarAlerta("Campo vacío", "Ingrese el apellido."); return false; }
        if (txtCedula.getText().trim().isEmpty()) { mostrarAlerta("Campo vacío", "Ingrese la cédula."); return false; }
        if (txtEspecialidad.getText().trim().isEmpty()) { mostrarAlerta("Campo vacío", "Ingrese la especialidad."); return false; }
        if (txtCorreo.getText().trim().isEmpty()) { mostrarAlerta("Campo vacío", "Ingrese el correo."); return false; }
        return true;
    }

    private void limpiar() {
        txtNombre.clear();
        txtApellido.clear();
        txtCedula.clear();
        txtEspecialidad.clear();
        txtCorreo.clear();
        tablaProfesores.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    @FXML
    private void nuevoProfesor() {
        // Limpia todos los campos de texto
        limpiar();

        // Opcional: poner el foco en el primer campo para empezar a escribir de una
        txtNombre.requestFocus();
    }
}