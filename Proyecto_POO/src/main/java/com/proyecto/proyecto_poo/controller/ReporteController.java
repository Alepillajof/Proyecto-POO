package com.proyecto.proyecto_poo.controller;

import com.proyecto.proyecto_poo.dao.ReporteDAO;
import com.proyecto.proyecto_poo.model.Reporte;
import com.proyecto.proyecto_poo.util.Sesion;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

public class ReporteController {

    // Componentes del Formulario Izquierdo
    @FXML private TextField txtTitulo;
    @FXML private TextArea txtDescripcion;
    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<String> cmbEstado;

    // Componentes de la Tabla Derecha
    @FXML private TableView<AsignacionDTO> tablaAsignaciones;
    @FXML private TableColumn<AsignacionDTO, Integer> colAsignacionId;
    @FXML private TableColumn<AsignacionDTO, String> colCarrera;
    @FXML private TableColumn<AsignacionDTO, String> colNombreEstudiante;
    @FXML private TableColumn<AsignacionDTO, String> colApellidoEstudiante;
    @FXML private TableColumn<AsignacionDTO, String> colCorreoEstudiante;

    private final ReporteDAO dao = new ReporteDAO();
    private final ObservableList<AsignacionDTO> listaAsignaciones = FXCollections.observableArrayList();
    private AsignacionDTO asignacionSeleccionada;

    @FXML
    public void initialize() {
        // Inicializar el ComboBox de Estados
        cmbEstado.setItems(FXCollections.observableArrayList("Pendiente", "Aprobado", "En Revisión", "Corregir"));

        // Enlazar de forma estricta las columnas con las propiedades de AsignacionDTO
        colAsignacionId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCarrera.setCellValueFactory(new PropertyValueFactory<>("carrera"));
        colNombreEstudiante.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidoEstudiante.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colCorreoEstudiante.setCellValueFactory(new PropertyValueFactory<>("correo"));

        tablaAsignaciones.setItems(listaAsignaciones);

        // Listener para capturar la fila del estudiante seleccionado
        tablaAsignaciones.getSelectionModel().selectedItemProperty().addListener(
                (observable, anterior, seleccionado) -> {
                    if (seleccionado != null) {
                        asignacionSeleccionada = seleccionado;
                        txtTitulo.setText("Reporte - " + seleccionado.getNombre() + " " + seleccionado.getApellido());
                    }
                });

        // Control de vistas por rol
        String rolActivo = Sesion.getInstancia().getRolActivo();
        if (rolActivo != null && rolActivo.equalsIgnoreCase("ADMIN")) {
            txtTitulo.setDisable(true);
            txtDescripcion.setDisable(true);
            dpFecha.setDisable(true);
            cmbEstado.setDisable(true);

            tablaAsignaciones.setVisible(false);
            tablaAsignaciones.setManaged(false);

            javafx.application.Platform.runLater(() -> {
                mostrarAlerta(Alert.AlertType.WARNING, "Acceso Denegado", "El rol de Administrador no gestiona reportes.");
            });
            return;
        }

    }

    @FXML
    private void leer() {
        if (verificarBloqueoAdmin()) return;
        cargarTabla();
    }

    private void cargarTabla() {
        listaAsignaciones.clear();
        // Usamos directamente tu método original de la sesión
        int idUsuarioLogueado = Sesion.getInstancia().getIdUsuarioLogueado();

        List<AsignacionDTO> asignacionesDB = dao.listarEstudiantesAsignados(idUsuarioLogueado);
        if (asignacionesDB != null) {
            listaAsignaciones.addAll(asignacionesDB);
        }
    }

    @FXML
    private void guardar() {
        if (verificarBloqueoAdmin()) return;
        if (asignacionSeleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida", "Por favor, seleccione un estudiante de la tabla derecha.");
            return;
        }
        if (validarCampos()) return;

        Reporte nuevoReporte = new Reporte();
        nuevoReporte.setTitulo(txtTitulo.getText().trim());
        nuevoReporte.setDescripcion(txtDescripcion.getText().trim());
        nuevoReporte.setFecha(dpFecha.getValue() != null ? dpFecha.getValue() : LocalDate.now());

        nuevoReporte.setUsuarioId(asignacionSeleccionada.getEstudianteId());
        nuevoReporte.setProfesorId(Sesion.getInstancia().getIdUsuarioLogueado());

        if (dao.guardar(nuevoReporte)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Reporte académico guardado correctamente.");
            limpiar();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo registrar el reporte.");
        }
    }

    @FXML
    private void actualizar() {
        if (verificarBloqueoAdmin()) return;
        mostrarAlerta(Alert.AlertType.INFORMATION, "Aviso", "La actualización se maneja mediante la selección de nuevas asignaciones.");
    }

    @FXML
    private void eliminar() {
        if (verificarBloqueoAdmin()) return;
        mostrarAlerta(Alert.AlertType.INFORMATION, "Aviso", "Los reportes se eliminan desde el historial de reportes global.");
    }

    @FXML
    private void limpiar() {
        txtTitulo.clear();
        txtDescripcion.clear();
        dpFecha.setValue(null);
        cmbEstado.setValue(null);
        asignacionSeleccionada = null;
        tablaAsignaciones.getSelectionModel().clearSelection();
    }

    private boolean verificarBloqueoAdmin() {
        String rolActivo = Sesion.getInstancia().getRolActivo();
        if (rolActivo != null && rolActivo.equalsIgnoreCase("ADMIN")) {
            mostrarAlerta(Alert.AlertType.ERROR, "Acción no permitida", "Los administradores no interactúan con esta sección.");
            return true;
        }
        return false;
    }

    private boolean validarCampos() {
        if (txtTitulo.getText().trim().isEmpty() ||
                txtDescripcion.getText().trim().isEmpty() ||
                cmbEstado.getValue() == null) {

            mostrarAlerta(Alert.AlertType.WARNING, "Campos Vacíos", "Por favor, complete el título, la descripción y el estado.");
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

    public static class AsignacionDTO {
        private int id;
        private int estudianteId;
        private String carrera;
        private String nombre;
        private String apellido;
        private String correo;

        public AsignacionDTO() {}

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public int getEstudianteId() { return estudianteId; }
        public void setEstudianteId(int estudianteId) { this.estudianteId = estudianteId; }

        public String getCarrera() { return carrera; }
        public void setCarrera(String carrera) { this.carrera = carrera; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getApellido() { return apellido; }
        public void setApellido(String apellido) { this.apellido = apellido; }

        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }
    }
}