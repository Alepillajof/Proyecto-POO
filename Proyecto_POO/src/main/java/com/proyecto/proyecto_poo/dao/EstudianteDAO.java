package com.proyecto.proyecto_poo.dao;

import com.proyecto.proyecto_poo.db.Conexion;
import com.proyecto.proyecto_poo.model.Estudiante;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO implements ICRUD<Estudiante> {

    @Override
    public boolean guardar(Estudiante estudiante) {
        String sql = """
                INSERT INTO estudiantes (nombre, apellido, cedula, carrera, nivel, correo)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conexion = Conexion.getConexion();
             PreparedStatement ps = conexion != null ? conexion.prepareStatement(sql) : null) {

            if (ps == null) return false;

            ps.setString(1, estudiante.getNombre());
            ps.setString(2, estudiante.getApellido());
            ps.setString(3, estudiante.getCedula());
            ps.setString(4, estudiante.getCarrera());
            ps.setInt(5, estudiante.getNivel());
            ps.setString(6, estudiante.getCorreo());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean actualizar(Estudiante estudiante) {
        String sql = """
                UPDATE estudiantes
                SET nombre = ?,
                    apellido = ?,
                    cedula = ?,
                    carrera = ?,
                    nivel = ?,
                    correo = ?
                WHERE id = ?
                """;

        try (Connection conexion = Conexion.getConexion();
             PreparedStatement ps = conexion != null ? conexion.prepareStatement(sql) : null) {

            if (ps == null) return false;

            ps.setString(1, estudiante.getNombre());
            ps.setString(2, estudiante.getApellido());
            ps.setString(3, estudiante.getCedula());
            ps.setString(4, estudiante.getCarrera());
            ps.setInt(5, estudiante.getNivel());
            ps.setString(6, estudiante.getCorreo());
            ps.setInt(7, estudiante.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM estudiantes WHERE id = ?";

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
    public List<Estudiante> listar() {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "SELECT * FROM estudiantes";

        try (Connection conexion = Conexion.getConexion();
             Statement st = conexion != null ? conexion.createStatement() : null;
             ResultSet rs = st != null ? st.executeQuery(sql) : null) {

            if (rs == null) return lista;

            while (rs.next()) {
                Estudiante estudiante = new Estudiante();

                // Mapeo directo uno a uno con las columnas reales
                estudiante.setId(rs.getInt("id"));
                estudiante.setNombre(rs.getString("nombre"));
                estudiante.setApellido(rs.getString("apellido"));
                estudiante.setCedula(rs.getString("cedula"));
                estudiante.setCarrera(rs.getString("carrera"));
                estudiante.setNivel(rs.getInt("nivel"));
                estudiante.setCorreo(rs.getString("correo"));

                lista.add(estudiante);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Estudiante buscarPorId(int id) {
        String sql = "SELECT * FROM estudiantes WHERE id = ?";

        try (Connection conexion = Conexion.getConexion();
             PreparedStatement ps = conexion != null ? conexion.prepareStatement(sql) : null) {

            if (ps == null) return null;
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Estudiante estudiante = new Estudiante();

                    estudiante.setId(rs.getInt("id"));
                    estudiante.setNombre(rs.getString("nombre"));
                    estudiante.setApellido(rs.getString("apellido"));
                    estudiante.setCedula(rs.getString("cedula"));
                    estudiante.setCarrera(rs.getString("carrera"));
                    estudiante.setNivel(rs.getInt("nivel"));
                    estudiante.setCorreo(rs.getString("correo"));

                    return estudiante;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}