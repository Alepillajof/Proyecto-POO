package com.proyecto.proyecto_poo.controller;

import com.proyecto.proyecto_poo.dao.AsignacionDAO;
import com.proyecto.proyecto_poo.model.Asignacion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class AsignacionController {

    // Inyección de componentes del Formulario Izquierdo
    @FXML private TextField txtProfesorId;
    @FXML private TextField txtEstudianteId;
    @FXML private ComboBox<String> cmbEstado;

    // Inyección de la Tabla y sus Columnas (Derecha)
    @FXML private TableView<Asignacion> tablaAsignaciones;
    @FXML private TableColumn<Asignacion, Integer> colId;
    @FXML private TableColumn<Asignacion, Integer> colProfesor;
    @FXML private TableColumn<Asignacion, String> colEstudiante;
    @FXML private TableColumn<Asignacion, String> colCedula;
    @FXML private TableColumn<Asignacion, String> colCarrera;
    @FXML private TableColumn<Asignacion, String> colEstado;

    private final AsignacionDAO dao = new AsignacionDAO();
    private ObservableList<Asignacion> listaObservable;

    @FXML
    public void initialize() {
        System.out.println("✅ Cargando vista y componentes de Asignaciones...");

        // Enlazar las columnas de la tabla con las propiedades exactas del Modelo (Asignacion.java)
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProfesor.setCellValueFactory(new PropertyValueFactory<>("profesorId"));
        colEstudiante.setCellValueFactory(new PropertyValueFactory<>("nombreEstudiante"));
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedulaEstudiante"));
        colCarrera.setCellValueFactory(new PropertyValueFactory<>("carreraEstudiante"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Configurar opciones por defecto para el ComboBox de Estado
        cmbEstado.setItems(FXCollections.observableArrayList("PENDIENTE", "COMPLETADO"));
        cmbEstado.getSelectionModel().selectFirst();

        // Al hacer clic sobre un elemento de la tabla, rellenar los campos de texto de la izquierda
        tablaAsignaciones.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtProfesorId.setText(String.valueOf(newSelection.getProfesorId()));
                txtEstudianteId.setText(String.valueOf(newSelection.getEstudianteId()));
                cmbEstado.setValue(newSelection.getEstado());
            }
        });

        // Cargar los registros iniciales de la base de datos
        cargarDatosAsignacion();
    }

    private void cargarDatosAsignacion() {
        try {
            List<Asignacion> listaDB = dao.listar();
            listaObservable = FXCollections.observableArrayList(listaDB);
            tablaAsignaciones.setItems(listaObservable);
        } catch (Exception e) {
            System.err.println("Error al listar asignaciones desde el DAO:");
            e.printStackTrace();
        }
    }

    @FXML
    private void guardar(ActionEvent event) {
        Asignacion a = obtenerDatosFormulario();
        if (a == null) return;

        if (dao.guardar(a)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Asignación guardada correctamente.");
            limpiarFormulario();
            cargarDatosAsignacion();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo guardar la asignación.");
        }
    }

    @FXML
    private void actualizar(ActionEvent event) {
        Asignacion seleccionada = tablaAsignaciones.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Selecciona una asignación de la tabla para modificar.");
            return;
        }

        Asignacion a = obtenerDatosFormulario();
        if (a == null) return;
        a.setId(seleccionada.getId());

        if (dao.actualizar(a)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Asignación actualizada correctamente.");
            limpiarFormulario();
            cargarDatosAsignacion();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo actualizar la asignación.");
        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
        Asignacion seleccionada = tablaAsignaciones.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Selecciona una asignación de la tabla para eliminar.");
            return;
        }

        if (dao.eliminar(seleccionada.getId())) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Asignación eliminada correctamente.");
            limpiarFormulario();
            cargarDatosAsignacion();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo eliminar la asignación.");
        }
    }

    @FXML
    private void leer(ActionEvent event) {
        cargarDatosAsignacion();
        mostrarAlerta(Alert.AlertType.INFORMATION, "Sincronización", "Datos actualizados desde la base de datos.");
    }

    @FXML
    private void limpiar(ActionEvent event) {
        limpiarFormulario();
    }

    // Método auxiliar para validar y mapear los campos del formulario
    private Asignacion obtenerDatosFormulario() {
        if (txtProfesorId.getText().trim().isEmpty() || txtEstudianteId.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Vacíos", "Por favor, digite los identificadores requeridos.");
            return null;
        }

        try {
            int profId = Integer.parseInt(txtProfesorId.getText().trim());
            int estId = Integer.parseInt(txtEstudianteId.getText().trim());
            String estado = cmbEstado.getValue();

            return new Asignacion(0, profId, estId, estado);
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Formato", "Los IDs de Profesor y Estudiante deben ser únicamente valores numéricos enteros.");
            return null;
        }
    }

    private void limpiarFormulario() {
        txtProfesorId.clear();
        txtEstudianteId.clear();
        cmbEstado.getSelectionModel().selectFirst();
        tablaAsignaciones.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}