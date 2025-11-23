package com.halin.taifooddesktop.dao;

import java.sql.*;

public class DBConnect {
    private static final String URL = "jdbc:mysql://localhost:3306/taifood_db?useSSL=false&allowPublicKeyRetrieval=true&jdbcCompliantTruncation=false";
    private static final String USER = "root";
    private static final String PASS = "Halinn_05";

    public static Connection getConnection() {
        try {
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Kết nối CSDL thành công!");
            return con;
        } catch (SQLException e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL: " + e.getMessage());
            return null;
        }
    }
}