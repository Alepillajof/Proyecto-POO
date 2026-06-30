package com.proyecto.proyecto_poo.util;

import com.proyecto.proyecto_poo.model.Usuario;

public class Sesion {

    private static Usuario usuario;

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        Sesion.usuario = usuario;
    }

}