package com.proyecto.proyecto_poo.util;

import com.proyecto.proyecto_poo.model.Usuario;

public class Sesion {

    private static Sesion instancia;
    private static Usuario usuario;

    private int idUsuarioLogueado;
    private int idEntidadRol; // <- NUEVO: Guarda el ID real de la tabla estudiante/profesor
    private String rolActivo;
    private String nombreUsuario;

    private Sesion() {}

    public static Sesion getInstancia() {
        if (instancia == null) {
            instancia = new Sesion();
        }
        return instancia;
    }

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        Sesion.usuario = usuario;
        if (usuario != null) {
            getInstancia().setIdUsuarioLogueado(usuario.getId());
            getInstancia().setNombreUsuario(usuario.getNombre());
        }
    }

    public int getIdUsuarioLogueado() {
        return idUsuarioLogueado;
    }

    public void setIdUsuarioLogueado(int idUsuarioLogueado) {
        this.idUsuarioLogueado = idUsuarioLogueado;
    }

    public int getIdEntidadRol() { // <- NUEVO GETTER
        return idEntidadRol;
    }

    public void setIdEntidadRol(int idEntidadRol) { // <- NUEVO SETTER
        this.idEntidadRol = idEntidadRol;
    }

    public String getRolActivo() {
        return rolActivo;
    }

    public void setRolActivo(String rolActivo) {
        this.rolActivo = rolActivo;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}