package main.java.com.TLU.studentmanagement.view;

import main.java.com.TLU.studentmanagement.controller.LoginController;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {

    public Dashboard(String username) {
        initComponents(username);
        this.setTitle("Dashboard");
        this.setSize(600, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void initComponents(String username) {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Chào mừng " + username + " đến với Dashboard!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(welcomeLabel, BorderLayout.CENTER);

        JButton logoutButton = new JButton("Đăng xuất");
        logoutButton.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(Dashboard.this, "Bạn có muốn đăng xuất không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                // Đóng Dashboard và hiển thị lại màn hình đăng nhập
                dispose();
                new LoginController().setVisible(true);
            }
        });
        panel.add(logoutButton, BorderLayout.SOUTH);

        this.add(panel);
    }
}

