package org.lasalle.controller;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionMysql {
    Connection conn;
    
    public Connection open() {
        String user = "root"; // Usuario de Mysql
        String password = "root"; // Pass de Mysql
        String db_name = "gluco_api";
        String url = "jdbc:mysql://127.0.0.1:3306/" + db_name;
        String parametros = "?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        
        try {
            // Corrección: Class con mayúscula y forName bien escrito
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url + parametros, user, password);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        // Corrección: Cierre real de la conexión en lugar de lanzar una excepción
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}