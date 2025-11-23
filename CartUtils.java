package com.halin.taifooddesktop.utils;

import javax.swing.table.DefaultTableModel;
import org.json.JSONArray;
import org.json.JSONObject;

public class CartUtils {

    // Thêm món vào giỏ
    public static void addToCart(String ma, String ten, double gia, DefaultTableModel model) {
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals(ma)) {
                int sl = Integer.parseInt(model.getValueAt(i, 2).toString()) + 1;
                model.setValueAt(sl, i, 2);
                model.setValueAt(sl * gia, i, 3);
                return;
            }
        }
        model.addRow(new Object[]{ma, ten, 1, gia, ""}); 
    }

    // Tính tổng tiền
    public static double getTotal(DefaultTableModel model) {
        double tong = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            Object value = model.getValueAt(i, 3);
            if (value != null) {
                tong += Double.parseDouble(value.toString());
            }
        }
        return tong;
    }

    // Tạo JSON đơn hàng
    public static String createOrderJson(DefaultTableModel model, String tenKH, String sdt, String diaChi, String ghiChu) {
        JSONObject order = new JSONObject();
        order.put("tenKhachHang", tenKH);
        order.put("soDienThoai", sdt);
        order.put("diaChi", diaChi);
        order.put("ghiChu", ghiChu);
        order.put("tongTien", getTotal(model));

        JSONArray items = new JSONArray();
        for (int i = 0; i < model.getRowCount(); i++) {
            JSONObject item = new JSONObject();
            item.put("ma", model.getValueAt(i, 0));
            item.put("ten", model.getValueAt(i, 1));
            item.put("soLuong", model.getValueAt(i, 2));
            item.put("thanhTien", model.getValueAt(i, 3));
            items.put(item);
        }

        order.put("gioHang", items);

        return order.toString(4);
    }

    // Lấy đơn giá gốc của món
    public static double getUnitPrice(int rowIndex, DefaultTableModel model) {
        try {
            double thanhTien = Double.parseDouble(model.getValueAt(rowIndex, 3).toString());
            int soLuong = Integer.parseInt(model.getValueAt(rowIndex, 2).toString());

            if (soLuong <= 0) return 0;
            return thanhTien / soLuong;

        } catch (Exception e) {
            return 0;
        }
    }
}
