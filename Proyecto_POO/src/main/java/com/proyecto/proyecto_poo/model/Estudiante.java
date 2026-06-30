package com.proyecto.proyecto_poo.model;

public class Estudiante extends Usuario {

    private String cedula;
    private String carrera;
    private int nivel;

    public Estudiante() {
    }

    public Estudiante(int id,
                      String nombre,
                      String apellido,
                      String correo,
                      String usuario,
                      String contrasena,
                      String rol,
                      String cedula,
                      String carrera,
                      int nivel) {

        super(id, nombre, apellido, correo, usuario, contrasena, rol);

        this.cedula = cedula;
        this.carrera = carrera;
        this.nivel = nivel;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    @Override
    public String mostrarInformacion() {

        return "Estudiante: "
                + getNombre() + " "
                + getApellido()
                + " | Carrera: "
                + carrera;

    }

}