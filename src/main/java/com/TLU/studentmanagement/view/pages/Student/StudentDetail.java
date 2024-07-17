package main.java.com.TLU.studentmanagement.view.pages.Student;

import main.java.com.TLU.studentmanagement.model.User;

import javax.swing.*;
import java.awt.*;

public class StudentDetail extends JDialog {
    public StudentDetail(User user) {
        setTitle("Chi tiết sinh viên");
        setModal(true);
        setLayout(new GridLayout(10, 2));
        setSize(400, 400);

        add(new JLabel("Tên:"));
        add(new JLabel(user.getFullName()));

        add(new JLabel("Mã sinh viên:"));
        add(new JLabel(user.getMsv()));

        add(new JLabel("Năm:"));
        add(new JLabel(user.getYear()));

        add(new JLabel("Giáo viên chủ nhiệm:"));
        add(new JLabel(user.getGvcn()));

        add(new JLabel("Giới tính:"));
        add(new JLabel(user.getGender()));

        add(new JLabel("Lớp:"));
        add(new JLabel(user.getClassName()));

        add(new JLabel("Email:"));
        add(new JLabel(user.getEmail()));

        add(new JLabel("Chuyên ngành:"));
        add(new JLabel(user.getMajorId()));

        JButton closeButton = new JButton("Đóng");
        closeButton.addActionListener(e -> dispose());
        add(closeButton);
    }
}
