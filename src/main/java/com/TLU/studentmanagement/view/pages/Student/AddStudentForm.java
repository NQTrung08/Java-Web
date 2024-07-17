package main.java.com.TLU.studentmanagement.view.pages.Student;

import main.java.com.TLU.studentmanagement.controller.UserController;
import main.java.com.TLU.studentmanagement.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AddStudentForm extends JDialog {
    private JTextField nameField, msvField, classField, emailField, genderField, yearField;
    private JComboBox<String> gvcnComboBox, majorComboBox;
    private UserController userController;

    public AddStudentForm(UserController userController) {
        this.userController = userController;
        setTitle("Thêm sinh viên");
        setModal(true);
        setLayout(new GridLayout(10, 2));
        setSize(400, 400);

        add(new JLabel("Tên:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Mã sinh viên:"));
        msvField = new JTextField();
        add(msvField);

        add(new JLabel("Năm:"));
        yearField = new JTextField();
        add(yearField);

        add(new JLabel("Giáo viên chủ nhiệm:"));
        gvcnComboBox = new JComboBox<>();
        loadTeachers();
        add(gvcnComboBox);

        add(new JLabel("Giới tính:"));
        genderField = new JTextField();
        add(genderField);

        add(new JLabel("Lớp:"));
        classField = new JTextField();
        add(classField);

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Chuyên ngành:"));
        majorComboBox = new JComboBox<>();
        loadMajors();
        add(majorComboBox);

        JButton addButton = new JButton("Thêm");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addUser();
            }
        });
        add(addButton);

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

    private void addUser() {
        String name = nameField.getText();
        String msv = msvField.getText();
        String year = yearField.getText();
        String gvcn = gvcnComboBox.getSelectedItem().toString();
        String gender = genderField.getText();
        String className = classField.getText();
        String email = emailField.getText();
        String major = majorComboBox.getSelectedItem().toString();

        User user = new User(name, msv, year, gvcn, gender, className, email, major);
        userController.createUser(user);
        System.out.println("User: " + user.toString());
        dispose();
    }
}
