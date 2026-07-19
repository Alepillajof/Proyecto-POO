package com.proyecto.proyecto_poo.controller;

import com.proyecto.proyecto_poo.dao.ProfesorDAO;
import com.proyecto.proyecto_poo.model.Profesor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class ProfesorController {

    // Componentes del Formulario Izquierdo
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtCedula;
    @FXML private TextField txtEspecialidad;

    // Componentes de la Tabla Derecha
    @FXML private TextField txtBuscar;
    @FXML private TableView<Profesor> tablaProfesores;
    @FXML private TableColumn<Profesor, Integer> colId;
    @FXML private TableColumn<Profesor, String> colNombre;
    @FXML private TableColumn<Profesor, String> colApellido;
    @FXML private TableColumn<Profesor, String> colCedula;
    @FXML private TableColumn<Profesor, String> colEspecialidad;

    private final ProfesorDAO dao = new ProfesorDAO();
    private final ObservableList<Profesor> listaProfesores = FXCollections.observableArrayList();
    private FilteredList<Profesor> filteredData;

    private Profesor profesorSeleccionado;

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));

        // --- RESTRICCIÓN EN TIEMPO REAL PARA EL TEXTFIELD DE CÉDULA ---
        // Escucha los cambios de texto e impide escribir más de 10 caracteres o letras
        txtCedula.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;

            // Reemplaza todo lo que no sea un número numérico entero
            String soloNumeros = newValue.replaceAll("[^\\d]", "");

            // Si excede los 10 dígitos, corta el texto sobrante
            if (soloNumeros.length() > 10) {
                soloNumeros = soloNumeros.substring(0, 10);
            }

            // Aplica el valor limpio si es diferente al original ingresado
            if (!newValue.equals(soloNumeros)) {
                txtCedula.setText(soloNumeros);
            }
        });

        filteredData = new FilteredList<>(listaProfesores, b -> true);

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(profesor -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String filtro = newValue.toLowerCase();

                return profesor.getNombre().toLowerCase().contains(filtro)
                        || profesor.getApellido().toLowerCase().contains(filtro)
                        || profesor.getCedula().contains(filtro)
                        || profesor.getEspecialidad().toLowerCase().contains(filtro);
            });
        });

        tablaProfesores.setItems(filteredData);

        tablaProfesores.getSelectionModel().selectedItemProperty().addListener(
                (observable, anterior, seleccionado) -> {

                    if (seleccionado != null) {

                        profesorSeleccionado = seleccionado;

                        txtNombre.setText(seleccionado.getNombre());
                        txtApellido.setText(seleccionado.getApellido());
                        txtCedula.setText(seleccionado.getCedula());
                        txtEspecialidad.setText(seleccionado.getEspecialidad());
                    }
                });

        cargarTabla();
    }

    @FXML
    private void leer() {
        cargarTabla();
        mostrarAlerta(Alert.AlertType.INFORMATION,
                "Sincronización",
                "Datos actualizados desde la base de datos.");
    }

    private void cargarTabla() {
        listaProfesores.clear();
        List<Profesor> profesoresDB = dao.listar();

        if (profesoresDB != null) {
            listaProfesores.addAll(profesoresDB);
        }
    }

    @FXML
    private void guardar() {
        if (validarCampos()) {
            return;
        }

        Profesor nuevoProfesor = obtenerDatosFormulario();

        if (dao.guardar(nuevoProfesor)) {
            mostrarAlerta(Alert.AlertType.INFORMATION,
                    "Éxito",
                    "Profesor registrado correctamente.\nSu correo y usuario institucional han sido autogenerados.");

            limpiarFormulario();
            cargarTabla();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR,
                    "Error",
                    "No se pudo registrar al profesor.");
        }
    }

    @FXML
    private void actualizar() {
        if (profesorSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING,
                    "Aviso",
                    "Por favor, seleccione un profesor de la tabla.");
            return;
        }

        if (validarCampos()) {
            return;
        }

        Profesor modificado = obtenerDatosFormulario();
        modificado.setId(profesorSeleccionado.getId());

        if (dao.actualizar(modificado)) {
            mostrarAlerta(Alert.AlertType.INFORMATION,
                    "Éxito",
                    "Datos del profesor actualizados correctamente.");

            limpiarFormulario();
            cargarTabla();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR,
                    "Error",
                    "No se pudo actualizar la información.");
        }
    }

    @FXML
    private void eliminar() {
        if (profesorSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING,
                    "Aviso",
                    "Por favor, seleccione un profesor de la tabla.");
            return;
        }

        Alert confirmacion = new Alert(
                Alert.AlertType.CONFIRMATION,
                "¿Está seguro de eliminar a este profesor? Se borrarán sus credenciales.",
                ButtonType.YES,
                ButtonType.NO
        );

        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText(null);

        if (confirmacion.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            if (dao.eliminar(profesorSeleccionado)) {
                mostrarAlerta(Alert.AlertType.INFORMATION,
                        "Éxito",
                        "Profesor removido del sistema.");

                limpiarFormulario();
                cargarTabla();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR,
                        "Error",
                        "No se pudo eliminar el registro.");
            }
        }
    }

    @FXML
    private void limpiarFormulario() {
        txtNombre.clear();
        txtApellido.clear();
        txtCedula.clear();
        txtEspecialidad.clear();

        profesorSeleccionado = null;
        tablaProfesores.getSelectionModel().clearSelection();
    }

    private Profesor obtenerDatosFormulario() {
        Profesor p = new Profesor();
        p.setNombre(txtNombre.getText().trim());
        p.setApellido(txtApellido.getText().trim());
        p.setCedula(txtCedula.getText().trim());
        p.setEspecialidad(txtEspecialidad.getText().trim());
        return p;
    }

    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty() ||
                txtApellido.getText().trim().isEmpty() ||
                txtCedula.getText().trim().isEmpty() ||
                txtEspecialidad.getText().trim().isEmpty()) {

            mostrarAlerta(Alert.AlertType.WARNING,
                    "Campos Incompletos",
                    "Debe rellenar todos los campos del formulario.");
            return true;
        }

        String cedula = txtCedula.getText().trim();

        // Validar que tenga exactamente los 10 dígitos obligatorios antes de procesar
        if (cedula.length() != 10) {
            mostrarAlerta(Alert.AlertType.WARNING,
                    "Cédula incompleta",
                    "La cédula debe contener exactamente 10 dígitos.");
            txtCedula.requestFocus();
            return true;
        }

        return false;
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}