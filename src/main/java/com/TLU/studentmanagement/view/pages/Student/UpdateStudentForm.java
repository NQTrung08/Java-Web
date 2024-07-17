package main.java.com.TLU.studentmanagement.view.pages.Student;

import main.java.com.TLU.studentmanagement.controller.UserController;
import main.java.com.TLU.studentmanagement.controller.teacher.TeacherController;
import main.java.com.TLU.studentmanagement.model.User;
import main.java.com.TLU.studentmanagement.view.pages.Student.StudentsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UpdateStudentForm extends JDialog {
    private JTextField nameField, msvField, classField, emailField, genderField, yearField;
    private JComboBox<String> gvcnComboBox, majorComboBox;
    private UserController userController;
    private TeacherController teacherController;
    private User user;

    private StudentsPanel studentsPanel;

    // Constructor
    public UpdateStudentForm (StudentsPanel studentsPanel) {
        this.studentsPanel = studentsPanel;
        // Khởi tạo giao diện và các thành phần khác
    }

    public UpdateStudentForm(User user, UserController userController) {
        this.user = user;
        this.userController = userController;
        setTitle("Cập nhật sinh viên");
        setModal(true);
        setLayout(new GridLayout(10, 2));
        setSize(400, 400);

//        add(new JLabel("Tên:"));
//        nameField = new JTextField(user.getFullName());
//        add(nameField);
//
//        add(new JLabel("Mã sinh viên:"));
//        msvField = new JTextField(user.getMsv());
//        add(msvField);
//
//        add(new JLabel("Năm:"));
//        yearField = new JTextField(user.getYear());
//        add(yearField);

        add(new JLabel("Giáo viên chủ nhiệm:"));
        gvcnComboBox = new JComboBox<>();
        loadTeachers();
        gvcnComboBox.setSelectedItem(user.getGvcn());
        add(gvcnComboBox);

//        add(new JLabel("Giới tính:"));
//        genderField = new JTextField(user.getGender());
//        add(genderField);
//
//        add(new JLabel("Lớp:"));
//        classField = new JTextField(user.getClassName());
//        add(classField);
//
//        add(new JLabel("Email:"));
//        emailField = new JTextField(user.getEmail());
//        add(emailField);

        add(new JLabel("Chuyên ngành:"));
        majorComboBox = new JComboBox<>();
        loadMajors();
        majorComboBox.setSelectedItem(user.getMajorId());
        add(majorComboBox);

        JButton updateButton = new JButton("Cập nhật");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUser();
            }
        });
        add(updateButton);

        JButton cancelButton = new JButton("Hủy");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        add(cancelButton);
    }

    private void loadTeachers() {
        List<String> teachers = userController.getAllTeachers();
        for (String teacher : teachers) {
            gvcnComboBox.addItem(teacher);
        }
    }

    private void loadMajors() {
        List<String> majors = userController.getAllMajors();
        for (String major : majors) {
            majorComboBox.addItem(major);
        }
    }

    private void updateUser() {

//        user.setFullName(nameField.getText());
//        user.setMsv(msvField.getText());
//        user.setYear(yearField.getText());
        user.setGvcnName(gvcnComboBox.getSelectedItem().toString());
//        user.setGender(genderField.getText());
//        user.setClassName(classField.getText());
//        user.setEmail(emailField.getText());
        user.setMajorName(majorComboBox.getSelectedItem().toString());

        userController.updateUser(user.getId(), user);

        if (studentsPanel != null) {
            studentsPanel.loadUserTable();
        }
        dispose();
    }
}
