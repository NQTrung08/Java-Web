package com.TLU.studentmanagement.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.TLU.studentmanagement.util.HttpUtil;

public class HomeView extends JFrame {

    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JLabel nameLabel;
    private JButton loginButton;
    private JButton logoutButton;
    private boolean isLoggedIn = false;

    public HomeView() {
        // Cấu hình JFrame
        setTitle("Student Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Tạo panel chính
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Tạo panel điều hướng
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(new Color(42, 63, 84)); // Đặt màu nền
        navPanel.setPreferredSize(new Dimension(200, getHeight()));

        // Thêm icon và dòng chữ "Hello, [Tên Sinh Viên]"
        nameLabel = new JLabel("Hello, Guest");
        nameLabel.setForeground(Color.WHITE); // Màu chữ
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        navPanel.add(nameLabel);
        navPanel.add(Box.createRigidArea(new Dimension(0, 30))); // Thêm khoảng cách

        // Thêm nút Đăng Nhập và Đăng Xuất
        loginButton = new JButton("Đăng Nhập");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openLoginView();
            }
        });

        logoutButton = new JButton("Đăng Xuất");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogout();
            }
        });

        // Mặc định hiển thị nút Đăng Nhập
        updateLoginLogoutButtons();
        navPanel.add(loginButton);
        navPanel.add(logoutButton);
        logoutButton.setVisible(false); // Ẩn nút Đăng Xuất ban đầu

        // Tạo contentPanel với CardLayout
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);

        // Thêm các trang nội dung vào contentPanel
        contentPanel.add(createPage("Trang Học Sinh"), "HỌC SINH");
        contentPanel.add(createPage("Trang Danh Sách Lớp"), "DANH SÁCH LỚP");
        contentPanel.add(createPage("Trang Lớp"), "LỚP");
        contentPanel.add(createPage("Trang Giáo Viên"), "GIÁO VIÊN");
        contentPanel.add(createPage("Trang Môn Học"), "MÔN HỌC");
        contentPanel.add(createPage("Trang Học Kỳ"), "HỌC KỲ");
        contentPanel.add(createPage("Trang Điểm"), "ĐIỂM");
        contentPanel.add(createPage("Trang Tổng Kết"), "TỔNG KẾT");

        // Thêm trang Tài Khoản từ account.java
        contentPanel.add(new AccountPanel(), "TÀI KHOẢN");

        // Thêm các nút điều hướng vào panel
        String[] navItems = {"HỌC SINH", "DANH SÁCH LỚP", "LỚP", "GIÁO VIÊN", "MÔN HỌC", "HỌC KỲ", "ĐIỂM", "TỔNG KẾT", "TÀI KHOẢN"};
        String[] icons = {
                "icon_hocsinh.png", "icon_danhsachlop.png", "icon_lop.png", "icon_giaovien.png",
                "icon_monhoc.png", "icon_hocky.png", "icon_diem.png", "icon_tongket.png", "icon_taikhoan.png"
        };

        for (int i = 0; i < navItems.length; i++) {
            navPanel.add(createNavButton(navItems[i], icons[i]));
            navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        // Thêm panel điều hướng và panel nội dung vào panel chính
        mainPanel.add(navPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Thêm panel chính vào JFrame
        add(mainPanel);

        // Hiển thị JFrame
        setVisible(true);
    }

    // Phương thức để cập nhật trạng thái nút Đăng Nhập và Đăng Xuất
    private void updateLoginLogoutButtons() {
        loginButton.setVisible(!isLoggedIn);
        logoutButton.setVisible(isLoggedIn);
    }

    // Phương thức để mở cửa sổ đăng nhập
    private void openLoginView() {
        LoginView loginView = new LoginView();
        loginView.setVisible(true);
        loginView.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                // Sau khi cửa sổ đăng nhập đóng lại, kiểm tra đăng nhập thành công và cập nhật giao diện
                isLoggedIn = true; // Giả sử đăng nhập thành công
                updateLoginLogoutButtons(); // Cập nhật nút Đăng Nhập và Đăng Xuất
                // Cập nhật thông tin người dùng (ví dụ: tên người dùng)
                nameLabel.setText("Hello, Admin"); // Thay đổi lại tên người dùng ở đây
            }
        });
    }

    // Phương thức để thực hiện đăng xuất
    private void performLogout() {
        isLoggedIn = false; // Cập nhật trạng thái đăng nhập
        updateLoginLogoutButtons(); // Cập nhật nút Đăng Nhập và Đăng Xuất
        // Xóa thông tin người dùng và cập nhật giao diện
        nameLabel.setText("Hello, Guest"); // Đặt lại tên người dùng
        cardLayout.show(contentPanel, "HỌC SINH"); // Hiển thị trang mặc định sau khi đăng xuất
    }

    // Tạo panel header với icon và dòng chữ "Hello, [Tên Sinh Viên]"
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(42, 63, 84)); // Đặt màu nền

        String iconPath = "./images/icon_user.png";
        ImageIcon icon = loadImageIcon(iconPath);

        JLabel iconLabel = new JLabel(icon); // Thay thế đường dẫn icon_user.png bằng đường dẫn thực tế
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(iconLabel);

        nameLabel = new JLabel("Hello, Guest");
        nameLabel.setForeground(Color.WHITE); // Màu chữ
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(nameLabel);

        return panel;
    }

    // Hàm tiện ích để tải ảnh
    private ImageIcon loadImageIcon(String path) {
        ImageIcon icon = new ImageIcon(path);
        if (icon.getIconWidth() == -1) {
            System.out.println("Không thể tải ảnh: " + path);
            // Bạn có thể thay thế bằng một ảnh mặc định nếu ảnh không thể tải
            icon = new ImageIcon("./images/default_icon.png");
        }
        return icon;
    }

    // Tạo các nút điều hướng
    private JPanel createNavButton(String text, String iconPath) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(new Color(42, 63, 84)); // Đặt màu nền

        ImageIcon icon = loadImageIcon("./images/" + iconPath);
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        panel.add(iconLabel);

        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setForeground(Color.WHITE); // Màu chữ
        button.setBackground(new Color(42, 63, 84)); // Màu nền nút
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.addActionListener(new NavButtonActionListener(text));
        panel.add(button);

        return panel;
    }

    // Lấy tên sinh viên từ API (giả sử)
    private String getStudentName() {
        String apiUrl = "http://localhost:8080/api/user/getAll"; // Thay thế URL bằng URL thực tế của bạn
        try {
            String response = HttpUtil.sendGet(apiUrl);
            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            return jsonObject.get("fullname").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    // Tạo các trang nội dung
    private JPanel createPage(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    // ActionListener cho các nút điều hướng
    private class NavButtonActionListener implements ActionListener {
        private String page;

        public NavButtonActionListener(String page) {
            this.page = page;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            cardLayout.show(contentPanel, page);
        }
    }

    public static void main(String[] args) {
        // Chạy ứng dụng
        SwingUtilities.invokeLater(() -> new HomeView());
    }
}
