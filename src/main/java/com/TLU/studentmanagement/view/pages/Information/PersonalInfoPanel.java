package main.java.com.TLU.studentmanagement.view.pages.Information;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.com.TLU.studentmanagement.session.TeacherSession;
import main.java.com.TLU.studentmanagement.session.UserSession;
import main.java.com.TLU.studentmanagement.model.Teacher;
import main.java.com.TLU.studentmanagement.model.User;
import main.java.com.TLU.studentmanagement.util.HttpUtil;

public class PersonalInfoPanel extends JPanel {
    private JTable personalInfoTable;
    private JTable contactInfoTable;
    private JButton updateButton;

    public PersonalInfoPanel() {
        // Apply FlatLaf theme settings
        FlatLaf.setup(new FlatLightLaf());

        // Set the theme
        UIManager.put("TitlePane.background", new Color(240, 240, 240));
        UIManager.put("Toast.background", new Color(240, 240, 240));
        UIManager.put("Button.arc", 10);
        UIManager.put("Component.arc", 10);
        UIManager.put("Button.margin", new Insets(4, 6, 4, 6));
        UIManager.put("TextComponent.arc", 10);
        UIManager.put("TextField.margin", new Insets(4, 6, 4, 6));
        UIManager.put("PasswordField.margin", new Insets(4, 6, 4, 6));
        UIManager.put("ComboBox.padding", new Insets(4, 6, 4, 6));
        UIManager.put("TitlePane.unifiedBackground", false);
        UIManager.put("TitlePane.buttonSize", new Dimension(35, 23));
        UIManager.put("TitlePane.background", new Color(230, 230, 230));
        UIManager.put("TitlePane.foreground", Color.BLACK);

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel thông tin cá nhân
        JPanel personalInfoPanel = new JPanel(new BorderLayout(10, 10));
        personalInfoPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                "Thông tin cá nhân",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 18),
                Color.BLACK
        ));

        String[] personalColumns = {"Thông tin", "Chi tiết"};
        Object[][] personalData = {
                {"ID", ""},
                {"Họ tên", ""},
                {"Giới tính", ""},
                {"CMND/CCCD", ""},
                {"Lớp sinh viên", ""},
                {"Ngành học", ""},
                {"Năm học", ""},
                {"Ngày sinh", ""},
                {"Quyền", ""}
        };
        personalInfoTable = new JTable(personalData, personalColumns);
        personalInfoTable.setEnabled(false);  // Disable editing
        personalInfoTable.setRowHeight(30);
        personalInfoTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        personalInfoTable.setFont(new Font("Arial", Font.PLAIN, 14));
        personalInfoTable.setBackground(Color.WHITE);
        personalInfoTable.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane personalScrollPane = new JScrollPane(personalInfoTable);
        personalScrollPane.setBorder(BorderFactory.createEmptyBorder());
        personalInfoPanel.add(personalScrollPane, BorderLayout.CENTER);

        // Panel thông tin liên lạc
        JPanel contactInfoPanel = new JPanel(new BorderLayout(10, 10));
        contactInfoPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                "Thông tin liên lạc",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 18),
                Color.BLACK
        ));

        String[] contactColumns = {"Thông tin", "Chi tiết"};
        Object[][] contactData = {
                {"Điện thoại", ""},
                {"Email cá nhân", ""},
                {"Quốc gia", ""},
                {"Địa chỉ", ""}
        };
        contactInfoTable = new JTable(contactData, contactColumns);
        contactInfoTable.setEnabled(false);  // Disable editing
        contactInfoTable.setRowHeight(30);
        contactInfoTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        contactInfoTable.setFont(new Font("Arial", Font.PLAIN, 14));
        contactInfoTable.setBackground(Color.WHITE);
        contactInfoTable.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane contactScrollPane = new JScrollPane(contactInfoTable);
        contactScrollPane.setBorder(BorderFactory.createEmptyBorder());
        contactInfoPanel.add(contactScrollPane, BorderLayout.CENTER);

        JPanel tablesPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        tablesPanel.add(personalInfoPanel);
        tablesPanel.add(contactInfoPanel);

        updateButton = new JButton("Cập nhật thông tin cá nhân");
        updateButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        updateButton.setFont(new Font("Arial", Font.BOLD, 14));
        updateButton.setBackground(new Color(88, 86, 214));  // Accent color
        updateButton.setForeground(Color.WHITE);
        updateButton.setPreferredSize(new Dimension(250, 40));
        updateButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(updateButton);

        add(tablesPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadData();
    }

    public void loadData() {
        try {
            User user = UserSession.getUser();
            Teacher teacher = TeacherSession.getTeacher();

            if (user != null) {
                personalInfoTable.setValueAt(user.getId(), 0, 1);
                personalInfoTable.setValueAt(user.getFullName(), 1, 1);
                personalInfoTable.setValueAt(user.getGender() != null ? user.getGender() : "N/A", 2, 1);
                personalInfoTable.setValueAt("N/A", 3, 1);  // CMND/CCCD không có trong lớp User
                personalInfoTable.setValueAt("N/A", 4, 1);  // Lớp sinh viên không có trong lớp User
                personalInfoTable.setValueAt(user.getMajor() != null ? user.getMajor() : "N/A", 5, 1);
                personalInfoTable.setValueAt(user.getYear() != null ? user.getYear() : "N/A", 6, 1);
                personalInfoTable.setValueAt("N/A", 7, 1);  // Ngày sinh không có trong lớp User
                personalInfoTable.setValueAt(user.isAdmin() ? "Admin" : "Student", 8, 1);

                contactInfoTable.setValueAt("N/A", 0, 1);  // Điện thoại không có trong lớp User
                contactInfoTable.setValueAt(user.getEmail() != null ? user.getEmail() : "N/A", 1, 1);
                contactInfoTable.setValueAt("N/A", 2, 1);  // Quốc gia không có trong lớp User
                contactInfoTable.setValueAt("N/A", 3, 1);  // Địa chỉ không có trong lớp User
            } else if (teacher != null) {
                personalInfoTable.setValueAt(teacher.getId(), 0, 1);
                personalInfoTable.setValueAt(teacher.getFullName(), 1, 1);
                personalInfoTable.setValueAt("N/A", 2, 1);  // Giới tính không có cho giáo viên
                personalInfoTable.setValueAt("N/A", 3, 1);  // CMND/CCCD không có cho giáo viên
                personalInfoTable.setValueAt("N/A", 4, 1);  // Lớp sinh viên không có cho giáo viên
//                personalInfoTable.setValueAt(teacher.getMajor() != null ? teacher.getMajor() : "N/A", 5, 1);
//                personalInfoTable.setValueAt(teacher.getYear() != null ? teacher.getYear() : "N/A", 6, 1);
//                personalInfoTable.setValueAt(teacher.getDob() != null ? teacher.getDob() : "N/A", 7, 1);
                personalInfoTable.setValueAt(teacher.isGV() ? "Teacher" : "Unknown", 8, 1);

                contactInfoTable.setValueAt("N/A", 0, 1);  // Điện thoại không có cho giáo viên
                contactInfoTable.setValueAt("N/A", 1, 1);  // Email không có cho giáo viên
                contactInfoTable.setValueAt("N/A", 2, 1);  // Quốc gia không có cho giáo viên
                contactInfoTable.setValueAt("N/A", 3, 1);  // Địa chỉ không có cho giáo viên
            }
        } catch (Exception ex) {
            Logger.getLogger(PersonalInfoPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
