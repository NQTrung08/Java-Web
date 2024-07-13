package main.java.com.TLU.studentmanagement.view.Dashboard;

import com.formdev.flatlaf.FlatClientProperties;
import main.java.com.TLU.studentmanagement.main.Application;
import net.miginfocom.swing.MigLayout;
import main.java.com.TLU.studentmanagement.model.Teacher;
import main.java.com.TLU.studentmanagement.model.User;
import main.java.com.TLU.studentmanagement.session.TeacherSession;
import main.java.com.TLU.studentmanagement.session.UserSession;
import main.java.com.TLU.studentmanagement.view.AccountPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

public class HomeView extends JPanel {

    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JLabel nameLabel;
    private JButton logoutButton;

    // Mảng đường dẫn ảnh
    private String[] icons = {
            "/main/resources/images/account.png", // "Thông tin cá nhân"
            "/main/resources/images/course.png", // "Môn học"
            "/main/resources/images/grades.png", // "Phiếu báo điểm"
            "/main/resources/images/student.png", // "Thông tin Học Sinh"
            "/main/resources/images/teacher.png", // "Thông tin Giáo Viên
            "/main/resources/images/semester.png", // "Thông tin Học Kỳ
//            "/main/resources/images/avgGrades.png"
    };

    public HomeView() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel navPanel = new JPanel(new MigLayout("wrap,fillx,insets 20", "fill,200:200"));
        navPanel.setBackground(new Color(42, 63, 84));
        navPanel.setPreferredSize(new Dimension(240, getHeight()));

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

        // Luôn thêm trang "Thông tin cá nhân" trước tiên
        addPage("Thông tin cá nhân", "TT cá nhân", navPanel, contentPanel, icons[0]);
        addPage("Thông tin Môn học", "TT Môn học", navPanel, contentPanel, icons[1]);

        if (user != null && !user.isGv() && !user.isAdmin()) {
            // Nếu là sinh viên, chỉ hiển thị các trang học sinh
            addPage("Phiếu báo điểm", "Phiếu báo điểm", navPanel, contentPanel, icons[2]);
        } else {
            // Nếu là admin hoặc giáo viên, hiển thị tất cả các trang
            addPage("Thông tin Sinh viên", "TT Sinh viên", navPanel, contentPanel, icons[3]);
            addPage("Thông tin Giáo Viên", "TT Giáo VIên", navPanel, contentPanel, icons[4]);
            addPage("Thông tin Học Kỳ", "TT Học Kỳ", navPanel, contentPanel, icons[5]);
            addPage("Thông tin bảng điểm", "TT Bảng điểm", navPanel, contentPanel, icons[2]);
        }

        mainPanel.add(navPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void addPage(String pageName, String navItem, JPanel navPanel, JPanel contentPanel, String iconPath) {
        JPanel page = createPage(pageName);
        contentPanel.add(page, pageName);

        URL iconURL = getClass().getResource(iconPath);
        if (iconURL == null) {
            System.err.println("Resource not found: " + iconPath);
            return;
        }

        try {
            ImageIcon icon = new ImageIcon(ImageIO.read(iconURL));
            Image scaledImage = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));

            JLabel navLabel = new JLabel(navItem);
            navLabel.setForeground(Color.WHITE);
            navLabel.setFont(new Font("Roboto", Font.BOLD, 14));

            JPanel navItemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
            navItemPanel.setBackground(new Color(42, 63, 84));
            navItemPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            navItemPanel.add(iconLabel);
            navItemPanel.add(navLabel);

            navItemPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    cardLayout.show(contentPanel, pageName);
                }
            });

            navPanel.add(navItemPanel, "gapbottom 20, growx");
        } catch (IOException ex) {
            System.err.println("Error loading icon: " + iconPath);
            ex.printStackTrace();
        }
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
        UserSession.clear();
        TeacherSession.clear();
        SwingUtilities.getWindowAncestor(this).dispose();
        new Application().setVisible(true);
    }
}
