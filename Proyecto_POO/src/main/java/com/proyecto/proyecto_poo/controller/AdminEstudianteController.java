package com.proyecto.proyecto_poo.controller;

import com.proyecto.proyecto_poo.dao.AdminEstudianteDAO;
import com.proyecto.proyecto_poo.model.Estudiante;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminEstudianteController {

    // --- Inputs del Formulario FXML ---
    @FXML private TextField txtId;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtCedula;
    @FXML private TextField txtCarrera;
    @FXML private TextField txtNivel;

    // --- Componentes de la Tabla Derecha ---
    @FXML private TableView<Estudiante> tablaEstudiantes;
    @FXML private TableColumn<Estudiante, Integer> colId;
    @FXML private TableColumn<Estudiante, String> colNombre;
    @FXML private TableColumn<Estudiante, String> colApellido;
    @FXML private TableColumn<Estudiante, String> colCedula;
    @FXML private TableColumn<Estudiante, String> colCarrera;
    @FXML private TableColumn<Estudiante, Integer> colNivel;
    @FXML private TableColumn<Estudiante, String> colCorreo;

    private final AdminEstudianteDAO estudianteDAO = new AdminEstudianteDAO();
    private final ObservableList<Estudiante> listaEstudiantes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colCarrera.setCellValueFactory(new PropertyValueFactory<>("carrera"));
        colNivel.setCellValueFactory(new PropertyValueFactory<>("nivel"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));

        // --- RESTRICCIÓN EN TIEMPO REAL PARA EL TEXTFIELD DE CÉDULA ---
        txtCedula.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;

            // Filtra dejando únicamente los caracteres numéricos
            String soloNumeros = newValue.replaceAll("[^\\d]", "");

            // Recorta a un máximo estricto de 10 caracteres
            if (soloNumeros.length() > 10) {
                soloNumeros = soloNumeros.substring(0, 10);
            }

            // Solo actualiza el TextField si cambió el valor procesado
            if (!newValue.equals(soloNumeros)) {
                txtCedula.setText(soloNumeros);
            }
        });

        tablaEstudiantes.setItems(listaEstudiantes);

        tablaEstudiantes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtId.setText(String.valueOf(newSelection.getId()));
                txtNombre.setText(newSelection.getNombre());
                txtApellido.setText(newSelection.getApellido());
                txtCedula.setText(newSelection.getCedula());
                txtCarrera.setText(newSelection.getCarrera());
                txtNivel.setText(String.valueOf(newSelection.getNivel()));
            }
        });

        leer();
    }

    @FXML
    private void leer() {
        listaEstudiantes.clear();
        listaEstudiantes.addAll(estudianteDAO.listar());
    }

    @FXML
    private void guardar() {

        if (!validarCampos()) {
            return;
        }

        Estudiante nuevo = new Estudiante();
        nuevo.setNombre(txtNombre.getText().trim());
        nuevo.setApellido(txtApellido.getText().trim());
        nuevo.setCedula(txtCedula.getText().trim());
        nuevo.setCarrera(txtCarrera.getText().trim());
        nuevo.setNivel(Integer.parseInt(txtNivel.getText().trim()));

        if (estudianteDAO.guardar(nuevo)) {
            mostrarAlerta(
                    Alert.AlertType.INFORMATION,
                    "Éxito",
                    "Estudiante guardado.\nCorreo asignado: " + nuevo.getCorreo()
            );
            limpiar();
            leer();
        } else {
            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error",
                    "No se pudo procesar el registro."
            );
        }
    }

    @FXML
    private void actualizar() {

        if (txtId.getText().isEmpty()) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Atención",
                    "Seleccione un estudiante de la tabla para modificarlo."
            );
            return;
        }

        if (!validarCampos()) {
            return;
        }

        Estudiante editado = new Estudiante();
        editado.setId(Integer.parseInt(txtId.getText()));
        editado.setNombre(txtNombre.getText().trim());
        editado.setApellido(txtApellido.getText().trim());
        editado.setCedula(txtCedula.getText().trim());
        editado.setCarrera(txtCarrera.getText().trim());
        editado.setNivel(Integer.parseInt(txtNivel.getText().trim()));

        if (estudianteDAO.actualizar(editado)) {
            mostrarAlerta(
                    Alert.AlertType.INFORMATION,
                    "Actualizado",
                    "Datos del estudiante actualizados con éxito."
            );
            limpiar();
            leer();
        } else {
            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error",
                    "No se pudo actualizar el registro."
            );
        }
    }

    @FXML
    private void eliminar() {

        String idStr = txtId.getText();

        if (idStr.isEmpty()) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Atención",
                    "Seleccione el alumno que desea eliminar de la lista."
            );
            return;
        }

        Alert confirmacion = new Alert(
                Alert.AlertType.CONFIRMATION,
                "¿Está seguro de eliminar permanentemente este estudiante?",
                ButtonType.YES,
                ButtonType.NO
        );

        confirmacion.setHeaderText(null);
        confirmacion.showAndWait();

        if (confirmacion.getResult() == ButtonType.YES) {

            int id = Integer.parseInt(idStr);

            if (estudianteDAO.eliminar(id)) {
                mostrarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Eliminado",
                        "Registro removido del sistema."
                );
                limpiar();
                leer();
            } else {
                mostrarAlerta(
                        Alert.AlertType.ERROR,
                        "Error",
                        "El registro no pudo ser borrado."
                );
            }
        }
    }

    @FXML
    private void limpiar() {
        txtId.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtCedula.clear();
        txtCarrera.clear();
        txtNivel.clear();
        tablaEstudiantes.getSelectionModel().clearSelection();
    }

    private boolean validarCampos() {

        if (txtNombre.getText().trim().isEmpty() ||
                txtApellido.getText().trim().isEmpty() ||
                txtCedula.getText().trim().isEmpty() ||
                txtCarrera.getText().trim().isEmpty() ||
                txtNivel.getText().trim().isEmpty()) {

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Campos vacíos",
                    "Por favor complete todos los datos del formulario."
            );
            return false;
        }

        // VALIDACIÓN DE LONGITUD DE CÉDULA
        String cedula = txtCedula.getText().trim();

        if (cedula.length() != 10) {
            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Cédula incompleta",
                    "La cédula debe contener exactamente 10 dígitos."
            );
            txtCedula.requestFocus();
            return false;
        }

        // VALIDACIÓN DEL NIVEL
        try {
            Integer.parseInt(txtNivel.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Dato Inválido",
                    "El campo 'Nivel' debe ser un número entero válido."
            );
            txtNivel.requestFocus();
            return false;
        }

        return true;
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}