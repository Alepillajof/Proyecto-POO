package com.proyecto.proyecto_poo.dao;

import com.proyecto.proyecto_poo.db.Conexion;
import com.proyecto.proyecto_poo.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO implements ICRUD<Usuario> {

    // ==========================
    // LOGIN
    // ==========================

    public Usuario iniciarSesion(String usuario, String contrasena) {
        Usuario user = null;
        // Ajustado a la tabla nativa 'usuarios' y la columna 'contrasena'
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND contrasena = ?";

        try (Connection conexion = Conexion.getConexion();
             PreparedStatement ps = conexion != null ? conexion.prepareStatement(sql) : null) {

            if (ps == null) return null;

            ps.setString(1, usuario);
            ps.setString(2, contrasena);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new Usuario();
                    user.setId(rs.getInt("id"));
                    user.setNombre(rs.getString("nombre"));
                    user.setApellido(rs.getString("apellido"));
                    user.setCorreo(rs.getString("correo"));
                    user.setUsuario(rs.getString("usuario"));
                    user.setContrasena(rs.getString("contrasena"));
                    user.setRol(rs.getString("rol"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    // ==========================
    // INSERT
    // ==========================

    @Override
    public boolean guardar(Usuario usuario) {
        String sql = """
                INSERT INTO usuarios (nombre, apellido, correo, usuario, contrasena, rol)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conexion = Conexion.getConexion();
             PreparedStatement ps = conexion != null ? conexion.prepareStatement(sql) : null) {

            if (ps == null) return false;

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, usuario.getUsuario());
            ps.setString(5, usuario.getContrasena());
            ps.setString(6, usuario.getRol());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================
    // UPDATE
    // ==========================

    @Override
    public boolean actualizar(Usuario usuario) {
        String sql = """
                UPDATE usuarios
                SET nombre = ?,
                    apellido = ?,
                    correo = ?,
                    usuario = ?,
                    contrasena = ?,
                    rol = ?
                WHERE id = ?
                """;

        try (Connection conexion = Conexion.getConexion();
             PreparedStatement ps = conexion != null ? conexion.prepareStatement(sql) : null) {

            if (ps == null) return false;

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, usuario.getUsuario());
            ps.setString(5, usuario.getContrasena());
            ps.setString(6, usuario.getRol());
            ps.setInt(7, usuario.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ==========================
    // DELETE
    // ==========================

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";

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

    // ==========================
    // SELECT
    // ==========================

    @Override
    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Connection conexion = Conexion.getConexion();
             Statement st = conexion != null ? conexion.createStatement() : null;
             ResultSet rs = st != null ? st.executeQuery(sql) : null) {

            if (rs == null) return lista;

            while (rs.next()) {
                Usuario usuario = new Usuario();

                usuario.setId(rs.getInt("id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellido(rs.getString("apellido"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setUsuario(rs.getString("usuario"));
                usuario.setContrasena(rs.getString("contrasena"));
                usuario.setRol(rs.getString("rol"));

                lista.add(usuario);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Agregamos buscarPorId para mantener consistencia absoluta con la interfaz ICRUD
    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";

        try (Connection conexion = Conexion.getConexion();
             PreparedStatement ps = conexion != null ? conexion.prepareStatement(sql) : null) {

            if (ps == null) return null;
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setApellido(rs.getString("apellido"));
                    usuario.setCorreo(rs.getString("correo"));
                    usuario.setUsuario(rs.getString("usuario"));
                    usuario.setContrasena(rs.getString("contrasena"));
                    usuario.setRol(rs.getString("rol"));
                    return usuario;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}