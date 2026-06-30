package com.proyecto.proyecto_poo.model;

import java.time.LocalDate;

public class Reporte {

    private int id;
    private String titulo;
    private String descripcion;
    private LocalDate fecha;
    private int usuarioId;

    public Reporte() {
    }

    public Reporte(int id,
                   String titulo,
                   String descripcion,
                   LocalDate fecha,
                   int usuarioId) {

        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.usuarioId = usuarioId;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }


    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

}