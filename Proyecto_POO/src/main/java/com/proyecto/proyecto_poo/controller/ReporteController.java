package com.proyecto.proyecto_poo.controller;

import com.proyecto.proyecto_poo.dao.ReporteDAO;
import com.proyecto.proyecto_poo.model.Reporte;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class ReporteController {

    @FXML
    private TextField txtTitulo;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private DatePicker dpFecha;

    @FXML
    private TextField txtUsuarioId;

    @FXML
    private TableView<Reporte> tablaReportes;

    @FXML
    private TableColumn<Reporte, Integer> colId;

    @FXML
    private TableColumn<Reporte, String> colTitulo;

    @FXML
    private TableColumn<Reporte, String> colDescripcion;

    @FXML
    private TableColumn<Reporte, LocalDate> colFecha;

    @FXML
    private TableColumn<Reporte, Integer> colUsuarioId;

    private final ReporteDAO dao = new ReporteDAO();

    private final ObservableList<Reporte> lista =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colUsuarioId.setCellValueFactory(new PropertyValueFactory<>("usuarioId"));

        cargarTabla();

        tablaReportes.getSelectionModel().selectedItemProperty().addListener(
                (observable, anterior, reporte) -> {

                    if (reporte != null) {

                        txtTitulo.setText(reporte.getTitulo());
                        txtDescripcion.setText(reporte.getDescripcion());
                        dpFecha.setValue(reporte.getFecha());
                        txtUsuarioId.setText(String.valueOf(reporte.getUsuarioId()));

                    }

                });

    }

    private void cargarTabla() {

        lista.clear();
        lista.addAll(dao.listar());
        tablaReportes.setItems(lista);

    }

    @FXML
    private void guardar() {

        if (!validarCampos()) {
            return;
        }

        try {

            Reporte reporte = new Reporte();

            reporte.setTitulo(txtTitulo.getText().trim());
            reporte.setDescripcion(txtDescripcion.getText().trim());
            reporte.setFecha(dpFecha.getValue());
            reporte.setUsuarioId(Integer.parseInt(txtUsuarioId.getText().trim()));

            if (dao.guardar(reporte)) {

                mostrarInformacion("Éxito", "Reporte guardado correctamente.");
                limpiar();
                cargarTabla();

            }

        } catch (NumberFormatException e) {

            mostrarAlerta("Error", "El ID del usuario debe ser un número.");

        }

    }

    @FXML
    private void actualizar() {

        Reporte reporte = tablaReportes.getSelectionModel().getSelectedItem();

        if (reporte == null) {

            mostrarAlerta("Aviso", "Seleccione un reporte para actualizar.");
            return;

        }

        if (!validarCampos()) {
            return;
        }

        try {

            reporte.setTitulo(txtTitulo.getText().trim());
            reporte.setDescripcion(txtDescripcion.getText().trim());
            reporte.setFecha(dpFecha.getValue());
            reporte.setUsuarioId(Integer.parseInt(txtUsuarioId.getText().trim()));

            if (dao.actualizar(reporte)) {

                mostrarInformacion("Éxito", "Reporte actualizado correctamente.");
                limpiar();
                cargarTabla();

            }

        } catch (NumberFormatException e) {

            mostrarAlerta("Error", "El ID del usuario debe ser un número.");

        }

    }

    @FXML
    private void eliminar() {

        Reporte reporte = tablaReportes.getSelectionModel().getSelectedItem();

        if (reporte == null) {

            mostrarAlerta("Aviso", "Seleccione un reporte para eliminar.");
            return;

        }

        if (dao.eliminar(reporte.getId())) {

            mostrarInformacion("Éxito", "Reporte eliminado correctamente.");
            limpiar();
            cargarTabla();

        }

    }

    private boolean validarCampos() {

        if (txtTitulo.getText().trim().isEmpty()) {

            mostrarAlerta("Campo vacío", "Ingrese el título.");
            txtTitulo.requestFocus();
            return false;

        }

        if (txtDescripcion.getText().trim().isEmpty()) {

            mostrarAlerta("Campo vacío", "Ingrese la descripción.");
            txtDescripcion.requestFocus();
            return false;

        }

        if (dpFecha.getValue() == null) {

            mostrarAlerta("Campo vacío", "Seleccione una fecha.");
            dpFecha.requestFocus();
            return false;

        }

        if (txtUsuarioId.getText().trim().isEmpty()) {

            mostrarAlerta("Campo vacío", "Ingrese el ID del usuario.");
            txtUsuarioId.requestFocus();
            return false;

        }

        return true;

    }

    private void limpiar() {

        txtTitulo.clear();
        txtDescripcion.clear();
        dpFecha.setValue(null);
        txtUsuarioId.clear();

        tablaReportes.getSelectionModel().clearSelection();

    }

    private void mostrarAlerta(String titulo, String mensaje) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        alert.showAndWait();

    }

    private void mostrarInformacion(String titulo, String mensaje) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        alert.showAndWait();

    }

}