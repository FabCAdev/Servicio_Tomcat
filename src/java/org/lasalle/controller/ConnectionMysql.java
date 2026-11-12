package org.lasalle.controller;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionMysql {
    Connection conn;
    
    public Connection open() {
        // Lee las variables de entorno de Render, o usa las locales por defecto
        String user = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "root";
        String password = System.getenv("DB_PASS") != null ? System.getenv("DB_PASS") : "root";
        String dbUrl = System.getenv("DB_URL") != null ? System.getenv("DB_URL") : "jdbc:mysql://127.0.0.1:3306/gluco_api";
        
        String parametros = "?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(dbUrl + parametros, user, password);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}