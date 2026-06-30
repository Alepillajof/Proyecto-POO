package com.proyecto.proyecto_poo.model;

public class Estudiante extends Usuario {

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
                      String carrera,
                      int nivel) {

        super(id, nombre, apellido,
                correo, usuario,
                contrasena, rol);

        this.carrera = carrera;
        this.nivel = nivel;

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
                + getNombre()
                + " "
                + getApellido()
                + " - "
                + carrera;

    }

}