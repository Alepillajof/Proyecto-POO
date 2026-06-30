package com.proyecto.proyecto_poo.dao;

import com.proyecto.proyecto_poo.db.Conexion;
import com.proyecto.proyecto_poo.model.Reporte;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReporteDAO implements ICRUD<Reporte> {

    private Connection conexion;

    public ReporteDAO() {
        conexion = Conexion.getConexion();
    }

    @Override
    public boolean guardar(Reporte reporte) {

        String sql = """
                INSERT INTO reportes
                (titulo, descripcion, fecha, usuario_id)
                VALUES (?, ?, ?, ?)
                """;

        try {

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setString(1, reporte.getTitulo());
            ps.setString(2, reporte.getDescripcion());
            ps.setDate(3, Date.valueOf(reporte.getFecha()));
            ps.setInt(4, reporte.getUsuarioId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return false;

    }

    @Override
    public boolean actualizar(Reporte reporte) {

        String sql = """
                UPDATE reportes
                SET titulo=?,
                    descripcion=?,
                    fecha=?,
                    usuario_id=?
                WHERE id=?
                """;

        try {

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setString(1, reporte.getTitulo());
            ps.setString(2, reporte.getDescripcion());
            ps.setDate(3, Date.valueOf(reporte.getFecha()));
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

        String sql = "DELETE FROM reportes WHERE id=?";

        try {

            PreparedStatement ps = conexion.prepareStatement(sql);

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

        String sql = "SELECT * FROM reportes";

        try {

            Statement st = conexion.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                Reporte reporte = new Reporte();

                reporte.setId(rs.getInt("id"));
                reporte.setTitulo(rs.getString("titulo"));
                reporte.setDescripcion(rs.getString("descripcion"));
                reporte.setFecha(rs.getDate("fecha").toLocalDate());
                reporte.setUsuarioId(rs.getInt("usuario_id"));

                lista.add(reporte);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return lista;

    }

    public List<Reporte> listarPorUsuario(int usuarioId) {

        List<Reporte> lista = new ArrayList<>();

        String sql = "SELECT * FROM reportes WHERE usuario_id=?";

        try {

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setInt(1, usuarioId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Reporte reporte = new Reporte();

                reporte.setId(rs.getInt("id"));
                reporte.setTitulo(rs.getString("titulo"));
                reporte.setDescripcion(rs.getString("descripcion"));
                reporte.setFecha(rs.getDate("fecha").toLocalDate());
                reporte.setUsuarioId(rs.getInt("usuario_id"));

                lista.add(reporte);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return lista;

    }

}