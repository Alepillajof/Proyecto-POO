package com.proyecto.proyecto_poo.dao;

import com.proyecto.proyecto_poo.model.Asignacion;
import com.proyecto.proyecto_poo.db.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AsignacionDAO {

    /**
     * Verifica si un estudiante ya cuenta con un profesor asignado cuyo estado
     * no sea 'COMPLETADO' ni 'FINALIZADO'.
     * @return true si el estudiante está ocupado con otra asignación activa.
     */
    public boolean tieneAsignacionActiva(int estudianteId, int asignacionIdActual) {
        // Buscamos si hay registros para este estudiante que sigan PENDIENTES o activos
        String sql = "SELECT COUNT(*) FROM asignaciones " +
                "WHERE estudiante_id = ? " +
                "AND UPPER(estado) NOT IN ('COMPLETADO', 'FINALIZADO') " +
                "AND id != ?"; // Excluye la asignación actual por si estamos editando

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, estudianteId);
            ps.setInt(2, asignacionIdActual); // Pasamos 0 si es un nuevo registro

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al verificar asignación activa: " + e.getMessage());
        }
        return false;
    }

    /**
     * Guarda la asignación verificando primero la regla de negocio.
     */
    public boolean guardar(Asignacion asignacion) {
        // REGLA DE NEGOCIO: Validar si el alumno ya está comprometido con otro tutor
        if (tieneAsignacionActiva(asignacion.getEstudianteId(), 0)) {
            System.err.println("⚠️ Restricción: El estudiante ya tiene un profesor asignado de forma activa.");
            return false;
        }

        String sql = "INSERT INTO asignaciones (profesor_id, estudiante_id, estado) VALUES (?, ?, ?)";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, asignacion.getProfesorId());
            ps.setInt(2, asignacion.getEstudianteId());
            ps.setString(3, asignacion.getEstado().toUpperCase());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        asignacion.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al guardar asignación: " + e.getMessage());
        }
        return false;
    }

    /**
     * Actualiza una asignación existente validando que no colisione con la regla.
     */
    public boolean actualizar(Asignacion asignacion) {
        // REGLA DE NEGOCIO: Validar que al cambiar de estudiante o reabrir el estado, no duplique tutores activos
        if (tieneAsignacionActiva(asignacion.getEstudianteId(), asignacion.getId())) {
            System.err.println("⚠️ Restricción: No se puede actualizar. El estudiante ya posee un tutor activo.");
            return false;
        }

        String sql = "UPDATE asignaciones SET profesor_id = ?, estudiante_id = ?, estado = ? WHERE id = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, asignacion.getProfesorId());
            ps.setInt(2, asignacion.getEstudianteId());
            ps.setString(3, asignacion.getEstado().toUpperCase());
            ps.setInt(4, asignacion.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar asignación: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lista todas las asignaciones cruzando los datos para la interfaz.
     */
    public List<Asignacion> listar() {
        List<Asignacion> lista = new ArrayList<>();
        // Ajusta las columnas 'e.nombre', 'e.apellido', 'e.cedula', 'e.carrera' según tu esquema real
        String sql = "SELECT a.id, a.profesor_id, a.estudiante_id, a.estado, " +
                "CONCAT(e.nombre, ' ', e.apellido) AS nombre_completo, e.cedula, e.carrera " +
                "FROM asignaciones a " +
                "INNER JOIN estudiantes e ON a.estudiante_id = e.id";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Asignacion a = new Asignacion();
                a.setId(rs.getInt("id"));
                a.setProfesorId(rs.getInt("profesor_id"));
                a.setEstudianteId(rs.getInt("estudiante_id"));
                a.setEstado(rs.getString("estado"));

                // Mapeos extraídos para las columnas de la interfaz JavaFX
                a.setNombreEstudiante(rs.getString("nombre_completo"));
                a.setCedulaEstudiante(rs.getString("cedula"));
                a.setCarreraEstudiante(rs.getString("carrera"));

                lista.add(a);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar asignaciones: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Elimina una asignación por ID.
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM asignaciones WHERE id = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar asignación: " + e.getMessage());
            return false;
        }
    }
}