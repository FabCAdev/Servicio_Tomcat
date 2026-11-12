package org.lasalle.controller;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionMysql {
    Connection conn;

    public Connection open() {
        String user = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "root";
        String password = System.getenv("DB_PASS") != null ? System.getenv("DB_PASS") : "root";

        // La URL por defecto ya incluye sus propios parámetros correctamente
        String defaultUrl = "jdbc:mysql://127.0.0.1:3306/gluco_api?useSSL=false&useUnicode=true&characterEncoding=utf-8";
        String dbUrl = System.getenv("DB_URL") != null ? System.getenv("DB_URL") : defaultUrl;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Usamos la URL limpia, Render ya le inyecta sus propios parámetros
            conn = DriverManager.getConnection(dbUrl, user, password);
            return conn;
        } catch (Exception e) {
            System.out.println("=== ERROR FATAL DE CONEXION ===");
            System.out.println("URL Intentada: " + dbUrl);
            System.out.println("Causa: " + e.getMessage());
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