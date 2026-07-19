package com.proyecto.proyecto_poo.dao;

import com.proyecto.proyecto_poo.model.Estudiante;
import com.proyecto.proyecto_poo.db.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminEstudianteDAO {

    /**
     * REGISTRO EN DOS TABLAS (Transacción Completa):
     * Inserta los datos coordinadamente en 'usuarios' y 'estudiantes'.
     * Genera el correo automatico 'nombre.apellido@epn.edu.ec' y lo asigna también como 'usuario'.
     */
    public boolean guardar(Estudiante estudiante) {
        // --- Generación Automática de Credenciales ---
        String nombreLimpio = estudiante.getNombre().trim().toLowerCase().split(" ")[0];
        String apellidoLimpio = estudiante.getApellido().trim().toLowerCase().split(" ")[0];

        String correoInstitucional = nombreLimpio + "." + apellidoLimpio + "@epn.edu.ec";
        String usuarioGenerado = correoInstitucional; // Correo y usuario son idénticos
        String contraseniaPorDefecto = "123456";

        // Seteo de propiedades en el modelo
        estudiante.setCorreo(correoInstitucional);
        estudiante.setUsuario(usuarioGenerado);
        estudiante.setContrasena(contraseniaPorDefecto);
        estudiante.setRol("ESTUDIANTE");

        // Sentencias SQL adaptadas a la estructura real de tus tablas en Railway
        String sqlUsuario = "INSERT INTO usuarios (nombre, apellido, correo, usuario, contrasena, rol) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlEstudiante = "INSERT INTO estudiantes (id, nombre, apellido, cedula, carrera, nivel, correo) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement psUsuario = null;
        PreparedStatement psEstudiante = null;

        try {
            con = Conexion.getConexion();
            if (con == null) return false;

            // Desactivamos auto-commit para asegurar que se guarden ambas tablas o ninguna
            con.setAutoCommit(false);

            // 1. Inserción en la tabla 'usuarios'
            psUsuario = con.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS);
            psUsuario.setString(1, estudiante.getNombre());
            psUsuario.setString(2, estudiante.getApellido());
            psUsuario.setString(3, estudiante.getCorreo());
            psUsuario.setString(4, estudiante.getUsuario());
            psUsuario.setString(5, estudiante.getContrasena());
            psUsuario.setString(6, estudiante.getRol());

            int filasUsuario = psUsuario.executeUpdate();
            int idGenerado = -1;

            if (filasUsuario > 0) {
                try (ResultSet generatedKeys = psUsuario.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        idGenerado = generatedKeys.getInt(1);
                        estudiante.setId(idGenerado); // Seteamos el ID autogenerado al modelo
                    }
                }
            }

            // Si falló la obtención del ID de usuario, cancelamos la transacción
            if (idGenerado == -1) {
                con.rollback();
                return false;
            }

            // 2. Inserción en la tabla 'estudiantes' (Incluye los campos duplicados obligatorios de tu BD)
            psEstudiante = con.prepareStatement(sqlEstudiante);
            psEstudiante.setInt(1, idGenerado); // Llave primaria unificada con usuarios
            psEstudiante.setString(2, estudiante.getNombre());
            psEstudiante.setString(3, estudiante.getApellido());
            psEstudiante.setString(4, estudiante.getCedula());
            psEstudiante.setString(5, estudiante.getCarrera());
            psEstudiante.setInt(6, estudiante.getNivel());
            psEstudiante.setString(7, estudiante.getCorreo());

            int filasEstudiante = psEstudiante.executeUpdate();

            // Si ambas tablas respondieron de forma correcta, confirmamos en Railway
            if (filasEstudiante > 0) {
                con.commit();
                return true;
            } else {
                con.rollback();
                return false;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error crítico en la transacción de guardado: " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            // Cierre seguro de recursos de base de datos
            try {
                if (psUsuario != null) psUsuario.close();
                if (psEstudiante != null) psEstudiante.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * LECTURA DE DATOS (INNER JOIN):
     * Resuelve el conflicto de lectura uniendo 'usuarios' y 'estudiantes' mediante un alias directo.
     */
    public List<Estudiante> listar() {
        List<Estudiante> lista = new ArrayList<>();

        // Extraemos cruzando de manera inequívoca las dos tablas por su id
        String sql = "SELECT u.id, u.nombre, u.apellido, u.correo, u.usuario, u.contrasena, u.rol, "
                + "e.cedula, e.carrera, e.nivel "
                + "FROM usuarios u "
                + "INNER JOIN estudiantes e ON u.id = e.id";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Estudiante est = new Estudiante();
                // Datos base heredados de Usuario
                est.setId(rs.getInt("id"));
                est.setNombre(rs.getString("nombre"));
                est.setApellido(rs.getString("apellido"));
                est.setCorreo(rs.getString("correo"));
                est.setUsuario(rs.getString("usuario"));
                est.setContrasena(rs.getString("contrasena")); // Sincronizado con la columna 'contrasena'
                est.setRol(rs.getString("rol"));

                // Datos específicos de la tabla Estudiantes
                est.setCedula(rs.getString("cedula"));
                est.setCarrera(rs.getString("carrera"));
                est.setNivel(rs.getInt("nivel"));

                lista.add(est);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error crítico al leer la base de datos distribuida: " + e.getMessage());
        }
        return lista;
    }

    /**
     * ACTUALIZACIÓN EN CASCADA:
     * Modifica los datos correspondientes en ambas tablas al mismo tiempo.
     */
    public boolean actualizar(Estudiante estudiante) {
        String sqlUsuario = "UPDATE usuarios SET nombre = ?, apellido = ? WHERE id = ?";
        String sqlEstudiante = "UPDATE estudiantes SET nombre = ?, apellido = ?, cedula = ?, carrera = ?, nivel = ? WHERE id = ?";

        Connection con = null;
        try {
            con = Conexion.getConexion();
            if (con == null) return false;
            con.setAutoCommit(false);

            // Actualizar tabla usuarios
            try (PreparedStatement psU = con.prepareStatement(sqlUsuario)) {
                psU.setString(1, estudiante.getNombre());
                psU.setString(2, estudiante.getApellido());
                psU.setInt(3, estudiante.getId());
                psU.executeUpdate();
            }

            // Actualizar tabla estudiantes (incluyendo campos redundantes de tu estructura)
            try (PreparedStatement psE = con.prepareStatement(sqlEstudiante)) {
                psE.setString(1, estudiante.getNombre());
                psE.setString(2, estudiante.getApellido());
                psE.setString(3, estudiante.getCedula());
                psE.setString(4, estudiante.getCarrera());
                psE.setInt(5, estudiante.getNivel());
                psE.setInt(6, estudiante.getId());
                psE.executeUpdate();
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Error crítico al actualizar el registro: " + e.getMessage());
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    /**
     * ELIMINACIÓN EN CASCADA:
     * Remueve los registros cuidando las restricciones de clave foránea.
     */
    public boolean eliminar(int id) {
        // Primero borramos de la tabla hija (estudiantes) para evitar violaciones de llaves foráneas
        String sqlEstudiante = "DELETE FROM estudiantes WHERE id = ?";
        String sqlUsuario = "DELETE FROM usuarios WHERE id = ?";

        Connection con = null;
        try {
            con = Conexion.getConexion();
            if (con == null) return false;
            con.setAutoCommit(false);

            try (PreparedStatement psE = con.prepareStatement(sqlEstudiante)) {
                psE.setInt(1, id);
                psE.executeUpdate();
            }

            try (PreparedStatement psU = con.prepareStatement(sqlUsuario)) {
                psU.setInt(1, id);
                psU.executeUpdate();
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Error crítico al eliminar el registro: " + e.getMessage());
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}