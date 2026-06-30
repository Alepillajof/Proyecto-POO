package com.proyecto.proyecto_poo.dao;

import java.util.List;

public interface ICRUD<T> {

    boolean guardar(T objeto);

    boolean actualizar(T objeto);

    boolean eliminar(int id);

    List<T> listar();

}