package com.proyecto.proyecto_poo.dao;

import com.proyecto.proyecto_poo.db.Conexion;
import com.proyecto.proyecto_poo.model.Profesor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfesorDAO {

    /**
     * Guarda el profesor usando su propio ID autoincremental en la tabla profesores
     * e inserta sus credenciales en la tabla usuarios de forma independiente.
     */
    public boolean guardar(Profesor profesor) {
        // --- Generación Automática de Credenciales ---
        String nombreLimpio = profesor.getNombre().trim().toLowerCase().split(" ")[0];
        String apellidoLimpio = profesor.getApellido().trim().toLowerCase().split(" ")[0];

        String correoInstitucional = nombreLimpio + "." + apellidoLimpio + "@epn.edu.ec";
        String usuarioGenerado = correoInstitucional;
        String contraseniaPorDefecto = "123456";

        profesor.setCorreo(correoInstitucional);

        String sqlUsuario = "INSERT INTO usuarios (nombre, apellido, correo, usuario, contrasena, rol) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlProfesor = "INSERT INTO profesores (nombre, apellido, cedula, especialidad, correo) VALUES (?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement psUsuario = null;
        PreparedStatement psProfesor = null;

        try {
            con = Conexion.getConexion();
            if (con == null) return false;

            con.setAutoCommit(false); // Iniciamos transacción para asegurar ambos registros

            // 1. Insertar en la tabla 'usuarios'
            psUsuario = con.prepareStatement(sqlUsuario);
            psUsuario.setString(1, profesor.getNombre());
            psUsuario.setString(2, profesor.getApellido());
            psUsuario.setString(3, profesor.getCorreo());
            psUsuario.setString(4, usuarioGenerado);
            psUsuario.setString(5, contraseniaPorDefecto);
            psUsuario.setString(6, "PROFESOR");
            psUsuario.executeUpdate();

            // 2. Insertar en la tabla 'profesores' (Dejando que la BD genere su propio ID)
            psProfesor = con.prepareStatement(sqlProfesor, Statement.RETURN_GENERATED_KEYS);
            psProfesor.setString(1, profesor.getNombre());
            psProfesor.setString(2, profesor.getApellido());
            psProfesor.setString(3, profesor.getCedula());
            psProfesor.setString(4, profesor.getEspecialidad());
            psProfesor.setString(5, profesor.getCorreo());

            int filasProfesor = psProfesor.executeUpdate();
            if (filasProfesor > 0) {
                try (ResultSet generatedKeys = psProfesor.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        profesor.setId(generatedKeys.getInt(1)); // Asignamos el ID real de la tabla profesores
                    }
                }
                con.commit(); // Éxito en ambas operaciones
                return true;
            } else {
                con.rollback();
                return false;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al guardar profesor en las tablas: " + e.getMessage());
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        } finally {
            try {
                if (psUsuario != null) psUsuario.close();
                if (psProfesor != null) psProfesor.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Lista los profesores directamente desde su propia tabla en Railway.
     */
    public List<Profesor> listar() {
        List<Profesor> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, apellido, cedula, especialidad, correo FROM profesores";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Profesor p = new Profesor();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setApellido(rs.getString("apellido"));
                p.setCedula(rs.getString("cedula"));
                p.setEspecialidad(rs.getString("especialidad"));
                p.setCorreo(rs.getString("correo"));

                lista.add(p);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al listar profesores: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Actualiza los datos basándose en el ID de la tabla profesores y busca el usuario por su correo.
     */
    public boolean actualizar(Profesor profesor) {
        String sqlProfesor = "UPDATE profesores SET nombre = ?, apellido = ?, cedula = ?, especialidad = ? WHERE id = ?";
        String sqlUsuario = "UPDATE usuarios SET nombre = ?, apellido = ? WHERE correo = ?";

        Connection con = null;
        try {
            con = Conexion.getConexion();
            if (con == null) return false;
            con.setAutoCommit(false);

            // Actualizamos tabla profesores
            try (PreparedStatement psP = con.prepareStatement(sqlProfesor)) {
                psP.setString(1, profesor.getNombre());
                psP.setString(2, profesor.getApellido());
                psP.setString(3, profesor.getCedula());
                psP.setString(4, profesor.getEspecialidad());
                psP.setInt(5, profesor.getId());
                psP.executeUpdate();
            }

            // Actualizamos tabla usuarios usando el correo como vínculo estable
            try (PreparedStatement psU = con.prepareStatement(sqlUsuario)) {
                psU.setString(1, profesor.getNombre());
                psU.setString(2, profesor.getApellido());
                psU.setString(3, profesor.getCorreo());
                psU.executeUpdate();
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar profesor: " + e.getMessage());
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    /**
     * Elimina el registro de profesores y su respectivo acceso en usuarios usando el correo.
     */
    public boolean eliminar(Profesor profesor) {
        String sqlProfesor = "DELETE FROM profesores WHERE id = ?";
        String sqlUsuario = "DELETE FROM usuarios WHERE correo = ?";

        Connection con = null;
        try {
            con = Conexion.getConexion();
            if (con == null) return false;
            con.setAutoCommit(false);

            try (PreparedStatement psP = con.prepareStatement(sqlProfesor)) {
                psP.setInt(1, profesor.getId());
                psP.executeUpdate();
            }

            try (PreparedStatement psU = con.prepareStatement(sqlUsuario)) {
                psU.setString(1, profesor.getCorreo());
                psU.executeUpdate();
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar profesor: " + e.getMessage());
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}