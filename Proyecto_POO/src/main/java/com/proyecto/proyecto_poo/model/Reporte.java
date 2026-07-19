package com.proyecto.proyecto_poo.model;

import java.time.LocalDate;

public class Reporte {

    private int id;
    private String titulo;
    private String descripcion;
    private LocalDate fecha;
    private int usuarioId;     // ID del estudiante asignado
    private int profesorId;    // ID del profesor que emite el reporte

    // Campos auxiliares para mostrar en la interfaz del estudiante
    private String nombreProfesor;
    private String especialidadProfesor;

    // Constructor vacío
    public Reporte() {
    }

    // Constructor completo actualizado
    public Reporte(int id, String titulo, String descripcion, LocalDate fecha, int usuarioId, int profesorId) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.usuarioId = usuarioId;
        this.profesorId = profesorId;
    }

    // ==========================================
    //          GETTERS AND SETTERS NATIVOS
    // ==========================================

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public int getProfesorId() { return profesorId; }
    public void setProfesorId(int profesorId) { this.profesorId = profesorId; }

    // ==========================================
    //      GETTERS AND SETTERS AUXILIARES
    // ==========================================

    public String getNombreProfesor() {
        return nombreProfesor;
    }

    public void setNombreProfesor(String nombreProfesor) {
        this.nombreProfesor = nombreProfesor;
    }

    public String getEspecialidadProfesor() {
        return especialidadProfesor;
    }

    public void setEspecialidadProfesor(String especialidadProfesor) {
        this.especialidadProfesor = especialidadProfesor;
    }
}