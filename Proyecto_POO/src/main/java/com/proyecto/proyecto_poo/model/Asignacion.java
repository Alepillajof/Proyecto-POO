package com.proyecto.proyecto_poo.model;

public class Asignacion {

    // Campos mapeados de la tabla de la base de datos
    private int id;
    private int profesorId;
    private int estudianteId;
    private String estado; // "PENDIENTE" o "COMPLETADO"

    // Campos auxiliares requeridos para mostrar datos del estudiante en el TableView
    private String nombreEstudiante;
    private String cedulaEstudiante;
    private String carreraEstudiante;

    // Constructor vacío (Esencial para frameworks y mapeos manuales en DAOs)
    public Asignacion() {
    }

    // Constructor completo por si necesitas instanciarlo rápidamente
    public Asignacion(int id, int profesorId, int estudianteId, String estado) {
        this.id = id;
        this.profesorId = profesorId;
        this.estudianteId = estudianteId;
        this.estado = estado;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProfesorId() {
        return profesorId;
    }

    public void setProfesorId(int profesorId) {
        this.profesorId = profesorId;
    }

    public int getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(int estudianteId) {
        this.estudianteId = estudianteId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getCedulaEstudiante() {
        return cedulaEstudiante;
    }

    public void setCedulaEstudiante(String cedulaEstudiante) {
        this.cedulaEstudiante = cedulaEstudiante;
    }

    public String getCarreraEstudiante() {
        return carreraEstudiante;
    }

    public void setCarreraEstudiante(String carreraEstudiante) {
        this.carreraEstudiante = carreraEstudiante;
    }

    // Método opcional útil para depurar datos por consola (System.out.println)
    @Override
    public String toString() {
        return "Asignacion{" +
                "id=" + id +
                ", profesorId=" + profesorId +
                ", estudianteId=" + estudianteId +
                ", estado='" + estado + '\'' +
                ", estudiante='" + nombreEstudiante + '\'' +
                '}';
    }
}