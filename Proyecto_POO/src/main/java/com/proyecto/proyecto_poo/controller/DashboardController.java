package com.proyecto.proyecto_poo.controller;

import com.proyecto.proyecto_poo.app.MainApp;
import com.proyecto.proyecto_poo.model.Usuario;
import com.proyecto.proyecto_poo.util.Sesion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class DashboardController {

    @FXML private Label lblUsuario;
    @FXML private Label lblRol;
    @FXML private Button btnProfesores;
    @FXML private Button btnEstudiantes;      // "Vista Estudiante"
    @FXML private Button btnMiPanelEstudiante; // "Estudiantes" (Gestión CRUD)
    @FXML private Button btnReportes;
    @FXML private Button btnAsignacion;
    @FXML private StackPane panelPrincipal;

    @FXML
    public void initialize() {
        Usuario usuario = Sesion.getUsuario();

        if (usuario != null) {
            lblUsuario.setText(usuario.getNombre());
            lblRol.setText("Rol: " + usuario.getRol().toUpperCase());
            configurarPermisos(usuario.getRol());
        } else {
            lblUsuario.setText("Desconocido");
            lblRol.setText("Rol: ---");
        }
    }

    private void configurarPermisos(String rol) {
        if (rol == null) return;

        // --- Configuración por defecto (Resetear estados) ---
        btnProfesores.setVisible(true);
        btnProfesores.setManaged(true);
        btnProfesores.setDisable(false);

        btnEstudiantes.setVisible(true);
        btnEstudiantes.setManaged(true);
        btnEstudiantes.setDisable(false);

        btnMiPanelEstudiante.setVisible(true);
        btnMiPanelEstudiante.setManaged(true);
        btnMiPanelEstudiante.setDisable(false);

        btnReportes.setVisible(true);
        btnReportes.setManaged(true);
        btnReportes.setDisable(false);

        if (btnAsignacion != null) {
            btnAsignacion.setVisible(true);
            btnAsignacion.setManaged(true);
            btnAsignacion.setDisable(false);
        }

        // --- Aplicación estricta de Reglas de Roles ---
        switch (rol.toUpperCase().trim()) {
            case "ADMIN":
                // Admin ve gestión global de estudiantes, NO ve su panel personal ni reportes generales
                btnReportes.setDisable(true);
                btnReportes.setManaged(false);
                btnEstudiantes.setVisible(false);
                btnEstudiantes.setManaged(false);
                break;

            case "PROFESOR":
                // Profesor ÚNICAMENTE ve Reportes. Se oculta y desvincula todo lo demás
                btnProfesores.setVisible(false);
                btnProfesores.setManaged(false);

                btnMiPanelEstudiante.setVisible(false);
                btnMiPanelEstudiante.setManaged(false);

                btnEstudiantes.setVisible(false);
                btnEstudiantes.setManaged(false);

                btnReportes.setVisible(true);
                btnReportes.setManaged(true);

                if (btnAsignacion != null) {
                    btnAsignacion.setVisible(false);
                    btnAsignacion.setManaged(false);
                }
                break;

            case "ESTUDIANTE":
                // Estudiante SOLO debe ver la "Vista Estudiante" (Panel Académico). Se oculta reportes.
                btnProfesores.setVisible(false);
                btnProfesores.setManaged(false);

                if (btnAsignacion != null) {
                    btnAsignacion.setVisible(false);
                    btnAsignacion.setManaged(false);
                }

                btnMiPanelEstudiante.setVisible(false);
                btnMiPanelEstudiante.setManaged(false);

                // Ocultamos el botón de reportes global para que no aparezca en el menú
                btnReportes.setVisible(false);
                btnReportes.setManaged(false);
                break;
        }
    }

    private void activarBoton(Button botonActivo) {
        if (btnProfesores != null) btnProfesores.getStyleClass().remove("boton-menu-activo");
        if (btnEstudiantes != null) btnEstudiantes.getStyleClass().remove("boton-menu-activo");
        if (btnMiPanelEstudiante != null) btnMiPanelEstudiante.getStyleClass().remove("boton-menu-activo");
        if (btnReportes != null) btnReportes.getStyleClass().remove("boton-menu-activo");
        if (btnAsignacion != null) btnAsignacion.getStyleClass().remove("boton-menu-activo");

        if (botonActivo != null) {
            botonActivo.getStyleClass().add("boton-menu-activo");
        }
    }

    @FXML
    private void abrirProfesores() {
        Usuario actual = Sesion.getUsuario();
        if (actual == null || !"ADMIN".equalsIgnoreCase(actual.getRol().trim())) {
            System.out.println("Acceso denegado a la Gestión de Profesores.");
            return;
        }
        cargarVista("/view/profesores.fxml");
        activarBoton(btnProfesores);
    }

    @FXML
    private void abrirEstudiantes() {
        Usuario actual = Sesion.getUsuario();
        if (actual == null || !"ESTUDIANTE".equalsIgnoreCase(actual.getRol().trim())) {
            System.out.println("Acceso denegado a Vista Estudiante.");
            return;
        }
        cargarVista("/view/estudiantes.fxml");
        activarBoton(btnEstudiantes);
    }

    @FXML
    private void abrirMiPanelEstudiante() {
        Usuario actual = Sesion.getUsuario();
        if (actual == null || !"ADMIN".equalsIgnoreCase(actual.getRol().trim())) {
            System.out.println("Acceso denegado a la Gestión de Estudiantes.");
            return;
        }
        cargarVista("/view/AdminEstudiantes.fxml");
        activarBoton(btnMiPanelEstudiante);
    }

    @FXML
    private void abrirReportes() {
        // Bloquea por completo el clic en caso de que intentaran acceder siendo ADMIN o ESTUDIANTE
        Usuario actual = Sesion.getUsuario();
        if (actual == null || !"PROFESOR".equalsIgnoreCase(actual.getRol().trim())) {
            System.out.println("Acceso denegado al menú de reportes.");
            return;
        }

        cargarVista("/view/reportes.fxml");
        activarBoton(btnReportes);
    }

    @FXML
    private void abrirAsignaciones() {
        Usuario actual = Sesion.getUsuario();
        if (actual == null || !"ADMIN".equalsIgnoreCase(actual.getRol().trim())) {
            System.out.println("Acceso denegado al menú de asignaciones.");
            return;
        }
        cargarVista("/view/asignacion.fxml");
        activarBoton(btnAsignacion);
    }

    private void cargarVista(String ruta) {
        try {
            java.net.URL url = MainApp.class.getResource(ruta);
            if (url == null) {
                System.err.println("ERROR: No se encontró el archivo FXML en: " + ruta);
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent vista = loader.load();

            panelPrincipal.getChildren().clear();
            panelPrincipal.getChildren().add(vista);
            System.out.println(" Vista acoplada con éxito: " + ruta);
        } catch (Exception e) {
            System.err.println("ERROR CRÍTICO AL INICIALIZAR LA VISTA INTERNA DE: " + ruta);
            e.printStackTrace();
        }
    }

    @FXML
    public void cerrarSesion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/view/login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) lblUsuario.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}