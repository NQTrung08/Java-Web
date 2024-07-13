package main.java.com.TLU.studentmanagement.view.Dashboard;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;
import main.java.com.TLU.studentmanagement.model.User;
import main.java.com.TLU.studentmanagement.model.Teacher;
import main.java.com.TLU.studentmanagement.main.Application;
import main.java.com.TLU.studentmanagement.session.TeacherSession;
import main.java.com.TLU.studentmanagement.session.UserSession;
import main.java.com.TLU.studentmanagement.view.AccountPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class HomeView extends JPanel {

    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JLabel nameLabel;
    private JButton logoutButton;

    public HomeView() {
        FlatLightLaf.setup(); // Sử dụng FlatLaf giao diện đẹp mắt hơn
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel navPanel = new JPanel(new MigLayout("wrap,fillx,insets 20", "fill,200:200"));
        navPanel.setBackground(new Color(42, 63, 84));
        navPanel.setPreferredSize(new Dimension(260, getHeight()));

        nameLabel = new JLabel(getWelcomeMessage());
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        navPanel.add(nameLabel, "gapbottom 30, align center");

        logoutButton = new JButton("Đăng Xuất");
        logoutButton.setPreferredSize(new Dimension(80, 40));
        logoutButton.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0;" +
                "font:16");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogout();
            }
        });

        navPanel.add(logoutButton, "gapbottom 30, align center");

        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);

        User user = UserSession.getUser();
        Teacher teacher = TeacherSession.getTeacher();

        if (user != null && !user.isGv() && !user.isAdmin()) {
            // Nếu là sinh viên, chỉ hiển thị các trang học sinh và lớp
            contentPanel.add(createPage("Trang Học Sinh"), "HỌC SINH");
            contentPanel.add(createPage("Trang Lớp"), "LỚP");
        } else {
            // Nếu là admin hoặc giáo viên, hiển thị tất cả các trang
            contentPanel.add(createPage("Trang Học Sinh"), "HỌC SINH");
            contentPanel.add(createPage("Trang Lớp"), "LỚP");
            contentPanel.add(createPage("Trang Giáo Viên"), "GIÁO VIÊN");
            contentPanel.add(createPage("Trang Môn Học"), "MÔN HỌC");
            contentPanel.add(createPage("Trang Học Kỳ"), "HỌC KỲ");
            contentPanel.add(createPage("Trang Điểm"), "ĐIỂM");
            contentPanel.add(createPage("Trang Tổng Kết"), "TỔNG KẾT");
            contentPanel.add(new AccountPanel(), "TÀI KHOẢN");
        }

        String[] navItems = {"HỌC SINH", "LỚP", "GIÁO VIÊN", "MÔN HỌC", "HỌC KỲ", "ĐIỂM", "TỔNG KẾT", "TÀI KHOẢN"};
        String[] icons = {
                "/main/resources/images/student.png",
                "/main/resources/images/class.png",
                "/main/resources/images/teacher.png",
                "/main/resources/images/course.png",
                "/main/resources/images/semester.png",
                "/main/resources/images/grades.png",
                "/main/resources/images/avgGrades.png",
                "/main/resources/images/account.png"
        };

        for (int i = 0; i < navItems.length; i++) {
            // Kiểm tra vai trò của người dùng để quyết định xem có thêm vào navigation panel hay không
            if ((user != null && !user.isGv() && !user.isAdmin()) &&
                    !navItems[i].equals("HỌC SINH") &&
                    !navItems[i].equals("MÔN HỌC") &&
                    !navItems[i].equals("ĐIỂM") &&
                    !navItems[i].equals("TÀI KHOẢN")) {
                continue; // Nếu là sinh viên, bỏ qua các nav item không được liệt kê
            }
            JPanel navItemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
            navItemPanel.setBackground(new Color(42, 63, 84));
            navItemPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            URL iconURL = getClass().getResource(icons[i]);
            if (iconURL == null) {
                System.err.println("Resource not found: " + icons[i]);
                continue;
            }

            try {
                ImageIcon icon = new ImageIcon(ImageIO.read(iconURL));
                Image scaledImage = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));

                JLabel navLabel = new JLabel(navItems[i]);
                navLabel.setForeground(Color.WHITE);
                navLabel.setFont(new Font("Roboto", Font.BOLD, 14));

                navItemPanel.add(iconLabel);
                navItemPanel.add(navLabel);

                final String pageName = navItems[i];
                navItemPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        cardLayout.show(contentPanel, pageName);
                    }
                });

                navPanel.add(navItemPanel, "gapbottom 20, growx");
            } catch (IOException ex) {
                System.err.println("Error loading icon: " + icons[i]);
                ex.printStackTrace();
            }
        }

        mainPanel.add(navPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }



    private JPanel createPage(String pageName) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(pageName, JLabel.CENTER);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private String getWelcomeMessage() {
        User user = UserSession.getUser();
        if (user != null) {
            return "Welcome, " + user.getFullName();
        } else {
            Teacher teacher = TeacherSession.getTeacher();
            if (teacher != null) {
                return "Welcome, " + teacher.getFullName();
            }
        }
        return "Welcome, Guest";
    }

    private void performLogout() {
        // Xóa thông tin người dùng khỏi session
        UserSession.clear(); // Hoặc TeacherSession.clear(); nếu là Teacher
        TeacherSession.clear();
        // Quay lại màn hình đăng nhập
        SwingUtilities.getWindowAncestor(this).dispose();
        new Application().setVisible(true);
    }

//    private static BufferedImage toGrayscale(BufferedImage original) {
//        BufferedImage grayscale = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
//        Graphics g = grayscale.getGraphics();
//        g.drawImage(original, 0, 0, null);
//        g.dispose();
//        return grayscale;
//    }
}
