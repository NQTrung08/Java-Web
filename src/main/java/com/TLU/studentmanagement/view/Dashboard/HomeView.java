package main.java.com.TLU.studentmanagement.view.Dashboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Objects;

import main.java.com.TLU.studentmanagement.model.User;
import main.java.com.TLU.studentmanagement.model.Teacher;
import main.java.com.TLU.studentmanagement.main.Application;
import main.java.com.TLU.studentmanagement.session.TeacherSession;
import main.java.com.TLU.studentmanagement.session.UserSession;
import main.java.com.TLU.studentmanagement.view.AccountPanel;

public class HomeView extends JFrame {

    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JLabel nameLabel;
    private JButton logoutButton;

    public HomeView() {
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(new Color(42, 63, 84));
        navPanel.setPreferredSize(new Dimension(200, getHeight()));

        nameLabel = new JLabel(getWelcomeMessage());
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        navPanel.add(nameLabel);
        navPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        logoutButton = new JButton("Đăng Xuất");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogout();
            }
        });

        navPanel.add(logoutButton);

        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);

        contentPanel.add(createPage("Trang Học Sinh"), "HỌC SINH");
        contentPanel.add(createPage("Trang Danh Sách Lớp"), "DANH SÁCH LỚP");
        contentPanel.add(createPage("Trang Lớp"), "LỚP");
        contentPanel.add(createPage("Trang Giáo Viên"), "GIÁO VIÊN");
        contentPanel.add(createPage("Trang Môn Học"), "MÔN HỌC");
        contentPanel.add(createPage("Trang Học Kỳ"), "HỌC KỲ");
        contentPanel.add(createPage("Trang Điểm"), "ĐIỂM");
        contentPanel.add(createPage("Trang Tổng Kết"), "TỔNG KẾT");

        contentPanel.add(new AccountPanel(), "TÀI KHOẢN");

        String[] navItems = {"HỌC SINH", "DANH SÁCH LỚP", "LỚP", "GIÁO VIÊN", "MÔN HỌC", "HỌC KỲ", "ĐIỂM", "TỔNG KẾT", "TÀI KHOẢN"};
        String[] icons = {
                "/main/resources/images/student.png",
                "/main/resources/images/classList.png",
                "/main/resources/images/class.png",
                "/main/resources/images/teacher.png",
                "/main/resources/images/course.png",
                "/main/resources/images/semester.png",
                "/main/resources/images/grades.png",
                "/main/resources/images/avgGrades.png",
                "/main/resources/images/account.png"
        };

        for (int i = 0; i < navItems.length; i++) {
            JPanel navItemPanel = new JPanel();
            navItemPanel.setLayout(new BoxLayout(navItemPanel, BoxLayout.Y_AXIS));
            navItemPanel.setBackground(new Color(42, 63, 84));
            navItemPanel.setMaximumSize(new Dimension(200, 50));
            navItemPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // Sử dụng đường dẫn tài nguyên đúng
            URL iconURL = getClass().getResource(icons[i]);
            if (iconURL == null) {
                System.err.println("Resource not found: " + icons[i]);
                continue;
            }
            JLabel iconLabel = new JLabel(new ImageIcon(iconURL));
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel navLabel = new JLabel(navItems[i]);
            navLabel.setForeground(Color.WHITE);
            navLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            navItemPanel.add(iconLabel);
            navItemPanel.add(navLabel);

            final String pageName = navItems[i];
            navItemPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    cardLayout.show(contentPanel, pageName);
                }
            });

            navPanel.add(navItemPanel);
            navPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        mainPanel.add(navPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
        setVisible(true);
    }

    private JPanel createPage(String pageName) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(pageName, JLabel.CENTER);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private String getWelcomeMessage() {
        // Lấy thông tin người dùng từ mô hình User hoặc Teacher
        // Giả sử rằng bạn đã lưu thông tin người dùng vào `User` hoặc `Teacher` theo cách nào đó
        User user = UserSession.getUser(); // Hoặc TeacherSession.getTeacher(); nếu là Teacher
        if (user != null) {
            return "Hello, " + user.getFullName();
        } else {
            Teacher teacher = TeacherSession.getTeacher(); // Thay thế nếu là Teacher
            if (teacher != null) {
                return "Hello, " + teacher.getFullName();
            }
        }
        return "Hello, Guest";
    }

    private void performLogout() {
        // Xóa thông tin người dùng khỏi session
        UserSession.clear(); // Hoặc TeacherSession.clear(); nếu là Teacher
        // Quay lại màn hình đăng nhập
        this.dispose();
        new Application().setVisible(true);
    }
}
