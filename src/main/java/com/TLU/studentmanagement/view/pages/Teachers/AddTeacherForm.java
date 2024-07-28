package main.java.com.TLU.studentmanagement.view.pages.Teachers;

import main.java.com.TLU.studentmanagement.controller.teacher.TeacherController;
import raven.toast.Notifications;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddTeacherForm extends JDialog {
    private JTextField mgvField;
    private JTextField fullNameField;
    private JButton addButton;
    private JButton cancelButton;

    public AddTeacherForm(Frame parent) {
        super(parent, "Thêm Giáo Viên", true);
        setLayout(new GridLayout(3, 2));

        JLabel mgvLabel = new JLabel("Mã Giáo Viên:");
        JLabel fullNameLabel = new JLabel("Họ và Tên:");

        mgvField = new JTextField();
        fullNameField = new JTextField();

        addButton = new JButton("Thêm");
        cancelButton = new JButton("Hủy");

        add(mgvLabel);
        add(mgvField);
        add(fullNameLabel);
        add(fullNameField);
        add(addButton);
        add(cancelButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTeacher();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        pack();
        setLocationRelativeTo(parent);
    }

    private void addTeacher() {
        String mgv = mgvField.getText().trim();
        String fullName = fullNameField.getText().trim();

        if (mgv.isEmpty() || fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ thông tin.");
            return;
        }

        try {
            TeacherController.createTeacher(mgv, fullName);
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Thêm giáo viên thành công.");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm giáo viên: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

