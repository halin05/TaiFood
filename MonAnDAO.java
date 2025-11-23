package com.halin.taifooddesktop.dao;

import com.halin.taifooddesktop.model.MonAn;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MonAnDAO {

    public List<MonAn> layTatCa() {
        List<MonAn> ds = new ArrayList<>();
        String sql = "SELECT * FROM MonAn";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MonAn m = new MonAn();
                m.setMaMonAn(rs.getString("maMonAn"));  // String, không còn getInt
                m.setTenMonAn(rs.getString("tenMonAn"));
                m.setGia(rs.getDouble("gia"));
                m.setMoTa(rs.getString("moTa"));
                m.setHinhAnh(rs.getString("hinhAnh"));
                ds.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }
}