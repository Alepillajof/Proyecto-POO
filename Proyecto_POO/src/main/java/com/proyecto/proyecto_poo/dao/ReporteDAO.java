package com.proyecto.proyecto_poo.dao;

import com.proyecto.proyecto_poo.controller.ReporteController.AsignacionDTO;
import com.proyecto.proyecto_poo.db.Conexion;
import com.proyecto.proyecto_poo.model.Reporte;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReporteDAO implements ICRUD<Reporte> {

    @Override
    public boolean guardar(Reporte reporte) {
        String sql = "INSERT INTO reportes (titulo, descripcion, fecha, usuario_id) VALUES (?, ?, ?, ?)";

        try (Connection conexion = Conexion.getConexion();
             PreparedStatement ps = conexion != null ? conexion.prepareStatement(sql) : null) {

            if (ps == null) return false;

            ps.setString(1, reporte.getTitulo());
            ps.setString(2, reporte.getDescripcion());
            ps.setDate(3, reporte.getFecha() != null ? Date.valueOf(reporte.getFecha()) : null);
            ps.setInt(4, reporte.getUsuarioId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean actualizar(Reporte reporte) {
        String sql = "UPDATE reportes SET titulo = ?, descripcion = ?, fecha = ?, usuario_id = ? WHERE id = ?";

        try (Connection conexion = Conexion.getConexion();
             PreparedStatement ps = conexion != null ? conexion.prepareStatement(sql) : null) {

            if (ps == null) return false;

            ps.setString(1, reporte.getTitulo());
            ps.setString(2, reporte.getDescripcion());
            ps.setDate(3, reporte.getFecha() != null ? Date.valueOf(reporte.getFecha()) : null);
            ps.setInt(4, reporte.getUsuarioId());
            ps.setInt(5, reporte.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM reportes WHERE id = ?";
        try (Connection conexion = Conexion.getConexion();
             PreparedStatement ps = conexion != null ? conexion.prepareStatement(sql) : null) {
            if (ps == null) return false;
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Reporte> listar() {
        List<Reporte> lista = new ArrayList<>();
        // Relación cruzada: un reporte le pertenece a un estudiante (usuario_id).
        // Mediante la tabla 'asignaciones', encontramos qué profesor está a cargo de ese estudiante
        // para así poder pintar el nombre del Profesor y su Especialidad en la tabla superior.
        String sql = """
                SELECT r.*, p.nombre AS prof_nombre, p.apellido AS prof_apellido, p.especialidad\s
                FROM reportes r
                LEFT JOIN asignaciones a ON r.usuario_id = a.estudiante_id
                LEFT JOIN profesores p ON a.profesor_id = p.id
                """;

        try (Connection conexion = Conexion.getConexion();
             Statement st = conexion != null ? conexion.createStatement() : null;
             ResultSet rs = st != null ? st.executeQuery(sql) : null) {

            if (rs == null) return lista;
            while (rs.next()) {
                Reporte r = new Reporte();
                r.setId(rs.getInt("id"));
                r.setTitulo(rs.getString("titulo"));
                r.setDescripcion(rs.getString("descripcion"));

                Date fechaSql = rs.getDate("fecha");
                if (fechaSql != null) {
                    r.setFecha(fechaSql.toLocalDate());
                }

                r.setUsuarioId(rs.getInt("usuario_id"));

                // Poblar los campos auxiliares que requiere tu vista/tabla de estudiantes
                String nomProf = rs.getString("prof_nombre");
                String apeProf = rs.getString("prof_apellido");

                if (nomProf != null) {
                    r.setNombreProfesor(nomProf + " " + apeProf);
                    r.setEspecialidadProfesor(rs.getString("especialidad"));
                } else {
                    r.setNombreProfesor("Sin Profesor");
                    r.setEspecialidadProfesor("N/A");
                }

                lista.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<AsignacionDTO> listarEstudiantesAsignados(int idUsuarioLogueado) {
        List<AsignacionDTO> lista = new ArrayList<>();
        String sql = """
                SELECT a.id AS asignacion_id, a.estudiante_id, e.nombre, e.apellido, e.carrera, e.correo 
                FROM asignaciones a
                INNER JOIN estudiantes e ON a.estudiante_id = e.id
                INNER JOIN profesores p ON a.profesor_id = p.id
                INNER JOIN usuarios u ON p.correo = u.correo
                WHERE u.id = ?
                """;

        try (Connection conexion = Conexion.getConexion();
             PreparedStatement ps = conexion != null ? conexion.prepareStatement(sql) : null) {

            if (ps == null) return lista;
            ps.setInt(1, idUsuarioLogueado);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AsignacionDTO dto = new AsignacionDTO();
                    dto.setId(rs.getInt("asignacion_id"));
                    dto.setEstudianteId(rs.getInt("estudiante_id"));
                    dto.setCarrera(rs.getString("carrera"));
                    dto.setNombre(rs.getString("nombre"));
                    dto.setApellido(rs.getString("apellido"));
                    dto.setCorreo(rs.getString("correo"));
                    lista.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}