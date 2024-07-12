package main.java.com.TLU.studentmanagement.view;
import javax.swing.*;
import java.awt.*;

public class AccountPanel extends JPanel {
    public AccountPanel() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Trang Tài Khoản (Account Panel)", SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }
}