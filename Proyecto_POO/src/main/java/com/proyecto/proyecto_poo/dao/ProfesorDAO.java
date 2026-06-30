package com.proyecto.proyecto_poo.dao;

import com.proyecto.proyecto_poo.db.Conexion;
import com.proyecto.proyecto_poo.model.Profesor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfesorDAO implements ICRUD<Profesor> {

    private Connection conexion;

    public ProfesorDAO() {
        conexion = Conexion.getConexion();
    }

    @Override
    public boolean guardar(Profesor profesor) {

        String sql = """
                INSERT INTO profesores
                (nombre,apellido,cedula,especialidad,correo)
                VALUES(?,?,?,?,?)
                """;

        try {

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setString(1, profesor.getNombre());
            ps.setString(2, profesor.getApellido());
            ps.setString(3, profesor.getCedula());
            ps.setString(4, profesor.getEspecialidad());
            ps.setString(5, profesor.getCorreo());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }

    @Override
    public boolean actualizar(Profesor profesor) {

        String sql = """
                UPDATE profesores
                SET nombre=?,
                    apellido=?,
                    cedula=?,
                    especialidad=?,
                    correo=?
                WHERE id=?
                """;

        try {

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setString(1, profesor.getNombre());
            ps.setString(2, profesor.getApellido());
            ps.setString(3, profesor.getCedula());
            ps.setString(4, profesor.getEspecialidad());
            ps.setString(5, profesor.getCorreo());
            ps.setInt(6, profesor.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }

    @Override
    public boolean eliminar(int id) {

        String sql = "DELETE FROM profesores WHERE id=?";

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
    public List<Profesor> listar() {

        List<Profesor> lista = new ArrayList<>();

        String sql = "SELECT * FROM profesores";

        try {

            Statement st = conexion.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                Profesor profesor = new Profesor();

                profesor.setId(rs.getInt("id"));
                profesor.setNombre(rs.getString("nombre"));
                profesor.setApellido(rs.getString("apellido"));
                profesor.setCedula(rs.getString("cedula"));
                profesor.setEspecialidad(rs.getString("especialidad"));
                profesor.setCorreo(rs.getString("correo"));

                lista.add(profesor);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;

    }

    public Profesor buscarPorId(int id) {

        String sql = "SELECT * FROM profesores WHERE id=?";

        try {

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Profesor profesor = new Profesor();

                profesor.setId(rs.getInt("id"));
                profesor.setNombre(rs.getString("nombre"));
                profesor.setApellido(rs.getString("apellido"));
                profesor.setCedula(rs.getString("cedula"));
                profesor.setEspecialidad(rs.getString("especialidad"));
                profesor.setCorreo(rs.getString("correo"));

                return profesor;

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

}