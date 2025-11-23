package com.halin.taifooddesktop.gui;

import com.halin.taifooddesktop.utils.CartUtils;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class NhapThongTinGiaoHang extends JFrame {
    private final DefaultTableModel modelGioHang;
    private final JLabel lblTongTien;
    private final MainApp mainApp;

    public NhapThongTinGiaoHang(MainApp mainApp, DefaultTableModel modelGioHang, JLabel lblTongTien) {
        this.mainApp = mainApp;
        this.modelGioHang = modelGioHang;
        this.lblTongTien = lblTongTien;

        setTitle("Nhập Thông Tin Giao Hàng");
        setLayout(new GridLayout(5, 2, 10, 10));
        setSize(500, 300);
        setLocationRelativeTo(mainApp);

        add(new JLabel("Tên khách hàng:"));
        JTextField txtTen = new JTextField();
        add(txtTen);

        add(new JLabel("Số điện thoại:"));
        JTextField txtSdt = new JTextField();
        add(txtSdt);

        add(new JLabel("Địa chỉ giao:"));
        JTextField txtDiaChi = new JTextField();
        add(txtDiaChi);

        add(new JLabel("Ghi chú (tùy chọn):"));
        JTextField txtGhiChu = new JTextField();
        add(txtGhiChu);

        JButton btnXacNhan = new JButton("XÁC NHẬN ĐẶT HÀNG");
        btnXacNhan.setBackground(new Color(220, 20, 60));
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.addActionListener(e -> xacNhanDatHang(txtTen.getText(), txtSdt.getText(), txtDiaChi.getText(), txtGhiChu.getText()));
        add(new JLabel(""));
        add(btnXacNhan);
    }

    private void xacNhanDatHang(String ten, String sdt, String diaChi, String ghiChu) {
        if (ten.isEmpty() || sdt.isEmpty() || diaChi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!");
            return;
        }

        // Tạo JSON đơn hàng
        String json = CartUtils.createOrderJson(modelGioHang, ten, sdt, diaChi, ghiChu);

        // Giả sử gửi API
        // HttpUtil.sendPost("https://api.example.com/donhang", json);  // Nếu có HttpUtil

        JOptionPane.showMessageDialog(this, "ĐẶT HÀNG THÀNH CÔNG!\nJSON đơn hàng: " + json);
        modelGioHang.setRowCount(0);
        lblTongTien.setText("Tổng tiền: 0 đ");
        this.dispose();
    }
}