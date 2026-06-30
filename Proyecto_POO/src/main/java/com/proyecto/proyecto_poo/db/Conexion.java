package com.proyecto.proyecto_poo.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL = "jdbc:mysql://localhost:3306/gestion_educativa";

    private static final String USER = "root";

    private static final String PASSWORD = "root123";

    private static Connection conexion;

    public static Connection getConexion() {

        try {

            if (conexion == null || conexion.isClosed()) {

                conexion = DriverManager.getConnection(URL, USER, PASSWORD);

                System.out.println("Conexión exitosa con MySQL.");

            }

        } catch (SQLException e) {

            System.out.println("Error al conectar con la base de datos.");

            e.printStackTrace();

        }

        return conexion;

    }

}