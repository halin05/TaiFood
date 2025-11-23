package com.halin.taifooddesktop.gui;

import com.halin.taifooddesktop.dao.MonAnDAO;
import com.halin.taifooddesktop.model.MonAn;
import com.halin.taifooddesktop.utils.CartUtils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class MainApp extends JFrame {
    private final DefaultTableModel modelMonAn;
    private final DefaultTableModel modelGioHang;
    private final JTable tblMonAn;
    private final JTable tblGioHang;
    private JLabel lblTongTien;

    public MainApp() {
        // Khởi tạo bảng món ăn
        modelMonAn = new DefaultTableModel(new String[]{"Mã", "Tên món", "Giá (đ)", "Mô tả", "Hình ảnh"}, 0);
        tblMonAn = new JTable(modelMonAn);
        tblMonAn.setRowHeight(35);
        tblMonAn.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        // Khởi tạo bảng giỏ hàng
        modelGioHang = new DefaultTableModel(new String[]{"Mã", "Tên món", "Số lượng", "Thành tiền", "Hành động"}, 0);
        tblGioHang = new JTable(modelGioHang);
        tblGioHang.setRowHeight(35);

        // Giao diện chính
        setLayout(new BorderLayout(10, 10));
        setTitle("TAI FOOD - ĐẶT MÓN ĂN NGON");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Tiêu đề
        JLabel title = new JLabel("TAI FOOD - ĐẶT MÓN ĂN NGON", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(new Color(220, 20, 60));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // Chia 2 bảng trái - phải
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            new JScrollPane(tblMonAn), new JScrollPane(tblGioHang));
        splitPane.setDividerLocation(650);
        add(splitPane, BorderLayout.CENTER);

        // Tải dữ liệu từ CSDL khi mở ứng dụng
        loadDanhSachMonAn();

        // Panel dưới cùng: nút + tổng tiền
        initButtonsAndTotal();

        // Hiển thị cửa sổ
        setSize(1300, 750);
        setLocationRelativeTo(null);
        setVisible(true);

        // Thiết lập cột "Hành động" cho giỏ hàng (nút + / -)
        setupCartActionColumn();
    }

    // HÀM TẢI + HIỂN THỊ DANH SÁCH MÓN ĂN
    private void loadDanhSachMonAn() {
        modelMonAn.setRowCount(0); // xóa dữ liệu cũ
        List<MonAn> ds = new MonAnDAO().layTatCa();
        for (MonAn m : ds) {
            modelMonAn.addRow(new Object[]{
                m.getMaMonAn(),
                m.getTenMonAn(),
                String.format("%,.0f đ", m.getGia()),
                m.getMoTa(),
                m.getHinhAnh()
            });
        }
        System.out.println("Đã hiển thị " + ds.size() + " món ăn!");
    }

    // HÀM KHỞI TẠO NÚT VÀ TỔNG TIỀN
    private void initButtonsAndTotal() {
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
        add(bottom, BorderLayout.SOUTH);

        lblTongTien = new JLabel("Tổng tiền: 0 đ");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTongTien.setForeground(Color.RED);

        JButton btnAdd = new JButton("Thêm vào giỏ");
        btnAdd.setBackground(Color.GREEN.darker());
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnAdd.setPreferredSize(new Dimension(180, 50));

        JButton btnRemove = new JButton("Xóa món");
        btnRemove.setBackground(Color.ORANGE.darker());
        btnRemove.setForeground(Color.WHITE);
        btnRemove.setPreferredSize(new Dimension(140, 50));

        JButton btnOrder = new JButton("ĐẶT HÀNG NGAY");
        btnOrder.setBackground(new Color(220, 20, 60));
        btnOrder.setForeground(Color.WHITE);
        btnOrder.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btnOrder.setPreferredSize(new Dimension(300, 60));

        bottom.add(lblTongTien);
        bottom.add(btnAdd);
        bottom.add(btnRemove);
        bottom.add(btnOrder);

        // === SỰ KIỆN NÚT ===
        btnAdd.addActionListener(e -> themVaoGio());
        btnRemove.addActionListener(e -> xoaKhoiGio());
        btnOrder.addActionListener(e -> datHang());
    }

    // THIẾT LẬP CỘT "Hành động" VỚI NÚT + / -
    private void setupCartActionColumn() {
        TableColumn actionColumn = tblGioHang.getColumnModel().getColumn(4);
        actionColumn.setCellRenderer(new ButtonRenderer());
        actionColumn.setCellEditor(new ButtonEditor(new JCheckBox()));
    }

    // Class để render nút + / - trong bảng
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        public ButtonRenderer() {
            setLayout(new GridLayout(1, 2));
            JButton btnTang = new JButton("+");
            btnTang.setBackground(Color.GREEN);
            JButton btnGiam = new JButton("-");
            btnGiam.setBackground(Color.RED);
            add(btnTang);
            add(btnGiam);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Class để edit nút + / -
    class ButtonEditor extends DefaultCellEditor {
        private JPanel panel = new JPanel(new GridLayout(1, 2));
        private JButton btnTang = new JButton("+");
        private JButton btnGiam = new JButton("-");
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel.add(btnTang);
            panel.add(btnGiam);
            btnTang.addActionListener(e -> tangSoLuong(row));
            btnGiam.addActionListener(e -> giamSoLuong(row));
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            return panel;
        }

        private void tangSoLuong(int row) {
            int sl = (int) modelGioHang.getValueAt(row, 2) + 1;
            double gia = (double) modelGioHang.getValueAt(row, 3) / (sl - 1);
            modelGioHang.setValueAt(sl, row, 2);
            modelGioHang.setValueAt(sl * gia, row, 3);
            capNhatTongTien();
        }

        private void giamSoLuong(int row) {
            int sl = (int) modelGioHang.getValueAt(row, 2) - 1;
            if (sl <= 0) {
                modelGioHang.removeRow(row);
            } else {
                double gia = (double) modelGioHang.getValueAt(row, 3) / (sl + 1);
                modelGioHang.setValueAt(sl, row, 2);
                modelGioHang.setValueAt(sl * gia, row, 3);
            }
            capNhatTongTien();
        }
    }

    private void themVaoGio() {
        int row = tblMonAn.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món ăn!");
            return;
        }
        String ma = (String) modelMonAn.getValueAt(row, 0);
        String ten = (String) modelMonAn.getValueAt(row, 1);
        double gia = Double.parseDouble(modelMonAn.getValueAt(row, 2).toString().replaceAll("[^0-9]", ""));
        CartUtils.addToCart(ma, ten, gia, modelGioHang);
        capNhatTongTien();
    }

    private void xoaKhoiGio() {
        int row = tblGioHang.getSelectedRow();
        if (row != -1) {
            modelGioHang.removeRow(row);
            capNhatTongTien();
        }
    }

    private void datHang() {
        if (modelGioHang.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng trống!");
            return;
        }
        // Mở trang nhập thông tin giao hàng
        new NhapThongTinGiaoHang(this, modelGioHang, lblTongTien).setVisible(true);
    }

    private void capNhatTongTien() {
        double tong = CartUtils.getTotal(modelGioHang);
        lblTongTien.setText("Tổng tiền: " + String.format("%,.0f đ", tong));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainApp());
    }
}