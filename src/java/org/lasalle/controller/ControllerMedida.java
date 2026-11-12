package org.lasalle.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.lasalle.model.Medida;

public class ControllerMedida {

    public List<Medida> getAll() throws SQLException {
        return query("SELECT * FROM medida", null);
    }

    public List<Medida> getByUsuarioId(int idUsuario) throws SQLException {
        return query("SELECT * FROM medida WHERE idUsuario = ?", idUsuario);
    }

    private List<Medida> query(String sql, Integer idUsuario) throws SQLException {
        List<Medida> lista = new ArrayList<>();

        ConnectionMysql connMysql = new ConnectionMysql();
        Connection conn = connMysql.open();
        if (conn == null) {
            throw new SQLException("No se pudo conectar a la base de datos");
        }

        try (conn;
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            if (idUsuario != null) {
                pstm.setInt(1, idUsuario);
            }

            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    lista.add(fillMedida(rs));
                }
            }
        } finally {
            connMysql.close();
        }

        return lista;
    }

    public Medida save(Medida medida) throws SQLException {
        String sql = "INSERT INTO medida (idUsuario, glucosa, fechaHora, contexto, notas) VALUES (?, ?, ?, ?, ?)";
        
        ConnectionMysql connMysql = new ConnectionMysql();
        Connection conn = connMysql.open();
        if (conn == null) {
            throw new SQLException("No se pudo conectar a la base de datos");
        }

        try (conn; 
             PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstm.setInt(1, medida.getIdUsuario());
            pstm.setInt(2, medida.getGlucosa());
            pstm.setString(3, medida.getFechaHora());
            pstm.setString(4, medida.getContexto());
            pstm.setString(5, medida.getNotas());

            if (pstm.executeUpdate() == 0) {
                throw new SQLException("No se pudo registrar la medida");
            }

            try (ResultSet keys = pstm.getGeneratedKeys()) {
                if (keys.next()) {
                    medida.setIdMedida(keys.getInt(1));
                }
            }
            return medida;
        } finally {
            connMysql.close();
        }
    }

    public Medida fillMedida(ResultSet rs) throws SQLException {
        Medida medida = new Medida();
        medida.setIdMedida(rs.getInt("idMedida"));
        medida.setIdUsuario(rs.getInt("idUsuario"));
        medida.setGlucosa(rs.getInt("glucosa"));
        medida.setFechaHora(rs.getString("fechaHora"));
        medida.setContexto(rs.getString("contexto"));
        medida.setNotas(rs.getString("notas"));

        return medida;
    }
}