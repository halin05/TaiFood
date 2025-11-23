package com.halin.taifooddesktop.test;

import java.sql.*;

public class TestConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/taifood_db?useSSL=false&allowPublicKeyRetrieval=true&jdbcCompliantTruncation=false";
        String user = "root";
        String pass = "Halinn_05";

        try {
            Connection conn = DriverManager.getConnection(url, user, pass);
            System.out.println("KẾT NỐI THÀNH CÔNG!!!");
            
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT maMonAn, tenMonAn FROM MonAn LIMIT 5");
            while (rs.next()) {
                System.out.println(rs.getString(1) + " - " + rs.getString(2));
            }
            conn.close();
        } catch (Exception e) {
            System.out.println("LỖI KẾT NỐI:");
            e.printStackTrace();   // ← DÒNG NÀY SẼ IN RA LỖI CHÍNH XÁC NHẤT
        }
    }
}