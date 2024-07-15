package main.java.com.TLU.studentmanagement.view.pages.Information;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatClientProperties;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.com.TLU.studentmanagement.session.TeacherSession;
import main.java.com.TLU.studentmanagement.session.UserSession;
import main.java.com.TLU.studentmanagement.model.Teacher;
import main.java.com.TLU.studentmanagement.model.User;
import main.java.com.TLU.studentmanagement.util.HttpUtil;

public class PersonalInfoPanel extends JPanel {
    private JLabel lblId;
    private JLabel lblFullName;
    private JLabel lblGender;
    private JLabel lblIdCard;
    private JLabel lblStudentClass;
    private JLabel lblPhone;
    private JLabel lblEmail;
    private JLabel lblCountry;
    private JLabel lblAddress;
    private JLabel lblMajor;
    private JLabel lblYear;
    private JLabel lblDob;
    private JLabel lblIsAdmin;

    public PersonalInfoPanel() {
//        FlatLightLaf.setup();
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // Panel thông tin cá nhân
        JPanel personalInfoPanel = new JPanel(new GridLayout(8, 1, 10, 10));
        personalInfoPanel.setBorder(BorderFactory.createTitledBorder("Thông tin cá nhân"));

        lblId = createLabel("ID");
        lblFullName = createLabel("Họ tên");
        lblGender = createLabel("Giới tính");
        lblIdCard = createLabel("CMND/CCCD");
        lblStudentClass = createLabel("Lớp sinh viên");
        lblMajor = createLabel("Ngành học");
        lblYear = createLabel("Năm học");
        lblDob = createLabel("Ngày sinh");
        lblIsAdmin = createLabel("Quyền");

        personalInfoPanel.add(lblId);
        personalInfoPanel.add(lblFullName);
        personalInfoPanel.add(lblGender);
        personalInfoPanel.add(lblIdCard);
        personalInfoPanel.add(lblStudentClass);
        personalInfoPanel.add(lblMajor);
        personalInfoPanel.add(lblYear);
        personalInfoPanel.add(lblDob);
        personalInfoPanel.add(lblIsAdmin);

        // Panel thông tin liên lạc
        JPanel contactInfoPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        contactInfoPanel.setBorder(BorderFactory.createTitledBorder("Thông tin liên lạc"));

        lblPhone = createLabel("Điện thoại");
        lblEmail = createLabel("Email cá nhân");
        lblCountry = createLabel("Quốc gia");
        lblAddress = createLabel("Địa chỉ");

        contactInfoPanel.add(lblPhone);
        contactInfoPanel.add(lblEmail);
        contactInfoPanel.add(lblCountry);
        contactInfoPanel.add(lblAddress);

        mainPanel.add(personalInfoPanel);
        mainPanel.add(contactInfoPanel);

        JButton updateButton = new JButton("Cập nhật thông tin cá nhân");
        updateButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);

        add(mainPanel, BorderLayout.CENTER);
        add(updateButton, BorderLayout.SOUTH);

        loadData();

    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.putClientProperty(FlatClientProperties.STYLE, "font: bold");
        return label;
    }

    public void loadData() {
        try {
            User user = UserSession.getUser();
//            System.out.println(user.toString());
            Teacher teacher = TeacherSession.getTeacher();

            if (user != null) {
                lblId.setText("ID: " + user.getMsv());
                lblFullName.setText("Name: " + user.getFullName());
                lblEmail.setText("Email: " + user.getEmail());
                lblIsAdmin.setText("Role: " + (user.isAdmin() ? "Admin" : "Student"));
//                lblAddress.setText("Dia chi: " + user.get);
            } else if (teacher != null) {
                lblId.setText("ID: " + teacher.getMgv());
                lblFullName.setText("Name: " + teacher.getFullName());
                lblIsAdmin.setText("Role: " + (teacher.isGV() ? "Teacher" : "Unknown"));
            }
        } catch (Exception ex) {
            Logger.getLogger(PersonalInfoPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
