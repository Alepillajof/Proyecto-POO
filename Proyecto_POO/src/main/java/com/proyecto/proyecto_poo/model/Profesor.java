package com.proyecto.proyecto_poo.model;

public class Profesor extends Usuario {

    private String especialidad;

    public Profesor() {
    }

    public Profesor(int id,
                    String nombre,
                    String apellido,
                    String correo,
                    String usuario,
                    String contrasena,
                    String rol,
                    String especialidad) {

        super(id, nombre, apellido, correo,
                usuario, contrasena, rol);

        this.especialidad = especialidad;

    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    @Override
    public String mostrarInformacion() {

        return "Profesor: "
                + getNombre()
                + " "
                + getApellido()
                + " - "
                + especialidad;

    }

}