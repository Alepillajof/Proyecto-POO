package com.proyecto.proyecto_poo.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL =
            "jdbc:mysql://localhost:3306/gestion_educativa?serverTimezone=UTC";

    // Cambiar según la computadora
    private static final String USER = "root";
    private static final String PASSWORD = "root123";

    public static Connection getConexion() {

        Connection conexion = null;

        try {

            conexion = DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("Conexión exitosa.");

        } catch (SQLException e) {

            System.out.println("No se pudo conectar a la base de datos.");
            System.out.println("Mensaje: " + e.getMessage());

        }

        return conexion;

    }

}