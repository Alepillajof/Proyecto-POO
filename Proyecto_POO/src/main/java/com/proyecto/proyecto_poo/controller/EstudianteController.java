package com.proyecto.proyecto_poo.controller;

import com.proyecto.proyecto_poo.dao.EstudianteDAO;
import com.proyecto.proyecto_poo.dao.ReporteDAO;
import com.proyecto.proyecto_poo.model.Estudiante;
import com.proyecto.proyecto_poo.model.Reporte;
import com.proyecto.proyecto_poo.util.Sesion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.util.List;
import java.util.stream.Collectors;

public class EstudianteController {

    // Campos de datos personales (Panel Izquierdo)
    @FXML private TextField txtNombre, txtApellido, txtCedula, txtCarrera, txtNivel, txtCorreo;

    // Tabla superior (Datos Reporte / Profesor)
    @FXML private TableView<Reporte> tablaDatosEstudiante;
    @FXML private TableColumn<Reporte, String> colTituloReporte;
    @FXML private TableColumn<Reporte, String> colNombreProfesor;
    @FXML private TableColumn<Reporte, String> colEspecialidadProfesor;

    // Tabla inferior (Descripción)
    @FXML private TableView<Reporte> tablaDescripcion;
    @FXML private TableColumn<Reporte, String> colDescripcion;

    private final EstudianteDAO estudianteDao = new EstudianteDAO();
    private final ReporteDAO reporteDao = new ReporteDAO();

    private final ObservableList<Reporte> listaReportes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Enlazar columnas de la Tabla Superior
        colTituloReporte.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colNombreProfesor.setCellValueFactory(new PropertyValueFactory<>("nombreProfesor"));
        colEspecialidadProfesor.setCellValueFactory(new PropertyValueFactory<>("especialidadProfesor"));
        tablaDatosEstudiante.setItems(listaReportes);

        // Enlazar columna de la Tabla Inferior
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        tablaDescripcion.setItems(listaReportes);

        // Ajuste automático de texto (Word Wrap) para descripciones largas
        colDescripcion.setCellFactory(tc -> new TableCell<>() {
            private final Text text = new Text();
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    text.setText(item);
                    text.setStyle("-fx-fill: white;"); // Color blanco para el diseño oscuro
                    text.wrappingWidthProperty().bind(colDescripcion.widthProperty().subtract(15));
                    setGraphic(text);
                }
            }
        });
    }

    @FXML
    private void leer() {
        int idLogueado = Sesion.getInstancia().getIdUsuarioLogueado();
        Estudiante estudiante = estudianteDao.buscarPorId(idLogueado);

        if (estudiante != null) {
            // Rellenar campos de texto izquierdos
            txtNombre.setText(estudiante.getNombre());
            txtApellido.setText(estudiante.getApellido());
            txtCedula.setText(estudiante.getCedula());
            txtCarrera.setText(estudiante.getCarrera());
            txtNivel.setText(String.valueOf(estudiante.getNivel()));
            if (txtCorreo != null) {
                txtCorreo.setText(estudiante.getCorreo());
            }

            // Cargar y filtrar reportes correspondientes en las tablas
            cargarReportes(estudiante.getId());
        } else {
            mostrarAlerta("Error", "No se encontraron los datos del estudiante.");
        }
    }

    private void cargarReportes(int estudianteId) {
        listaReportes.clear();
        List<Reporte> todos = reporteDao.listar();

        // Filtrar para que el estudiante solo vea sus filas correspondientes (usuario_id)
        List<Reporte> filtrados = todos.stream()
                .filter(r -> r.getUsuarioId() == estudianteId)
                .collect(Collectors.toList());

        listaReportes.addAll(filtrados);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }
}