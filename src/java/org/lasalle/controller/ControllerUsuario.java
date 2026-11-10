package org.lasalle.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.lasalle.model.Usuario;

public class ControllerUsuario {

    public List<Usuario> getAll() throws Exception {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario";

        ConnectionMysql connMysql = new ConnectionMysql();
        Connection conn = connMysql.open();
        PreparedStatement pstm = conn.prepareStatement(sql);
        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            lista.add(fillUsuario(rs));
        }

        rs.close();
        pstm.close();
        connMysql.close();
        conn.close();

        return lista;
    }

    public Usuario save(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (nombre, primerApellido, segundoApellido, email) "
                + "VALUES (?, ?, ?, ?)";

        ConnectionMysql connMysql = new ConnectionMysql();
        Connection conn = connMysql.open();
        if (conn == null) {
            throw new SQLException("No se pudo conectar a la base de datos");
        }

        try (conn;
                PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstm.setString(1, usuario.getNombre());
            pstm.setString(2, usuario.getPrimerApellido());
            if (usuario.getSegundoApellido() == null || usuario.getSegundoApellido().isBlank()) {
                pstm.setNull(3, Types.VARCHAR);
            } else {
                pstm.setString(3, usuario.getSegundoApellido());
            }
            pstm.setString(4, usuario.getEmail());

            if (pstm.executeUpdate() == 0) {
                throw new SQLException("No se pudo registrar el usuario");
            }

            try (ResultSet keys = pstm.getGeneratedKeys()) {
                if (keys.next()) {
                    usuario.setIdUsuario(keys.getInt(1));
                }
            }

            return usuario;
        } finally {
            connMysql.close();
        }
    }

    public Usuario fillUsuario(ResultSet rs) throws SQLException {
        Usuario user = new Usuario();
        user.setIdUsuario(rs.getInt("idUsuario"));
        user.setNombre(rs.getString("nombre"));
        user.setPrimerApellido(rs.getString("primerApellido"));
        user.setSegundoApellido(rs.getString("segundoApellido"));
        user.setEmail(rs.getString("email"));

        return user;
    }
}
