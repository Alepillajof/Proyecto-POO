package com.proyecto.proyecto_poo.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String HOST = "tokaido.proxy.rlwy.net";
    private static final String PORT = "34155";
    private static final String DATABASE = "railway";
    private static final String USER = "root";

    private static final String PASSWORD = "EaTFGpGUoobdcxdiVBqDOnGUWAPgAXkG";

    private static final String URL =
            "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE
                    + "?useSSL=false"
                    + "&allowPublicKeyRetrieval=true"
                    + "&serverTimezone=UTC";

    public static Connection getConexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("----------------------------------------");
            System.out.println("¡CONEXIÓN EXITOSA A RAILWAY!");
            System.out.println("----------------------------------------");
            return con;

        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: No se encontró el driver de MySQL en el proyecto.");
            return null;
        } catch (SQLException e) {
            System.err.println("ERROR DE CONEXIÓN: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        getConexion();
    }
}