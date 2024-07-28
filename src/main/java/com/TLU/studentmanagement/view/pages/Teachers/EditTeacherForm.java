package main.java.com.TLU.studentmanagement.view.pages.Teachers;

import main.java.com.TLU.studentmanagement.controller.teacher.TeacherController;
import main.java.com.TLU.studentmanagement.model.Teacher;
import raven.toast.Notifications;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditTeacherForm extends JDialog {
    private JTextField mgvField;
    private JTextField fullNameField;
    private JButton saveButton;
    private JButton cancelButton;
    private Teacher teacher;
    private TeachersPanel parentPanel;

    public EditTeacherForm(Frame parent, Teacher teacher, TeachersPanel parentPanel) {
        super(parent, "Sửa Giáo Viên", true);
        this.teacher = teacher;
        this.parentPanel = parentPanel;

        setLayout(new GridLayout(3, 2));

        JLabel mgvLabel = new JLabel("Mã Giáo Viên:");
        JLabel fullNameLabel = new JLabel("Họ và Tên:");

        mgvField = new JTextField(teacher.getMgv());
        fullNameField = new JTextField(teacher.getFullName());

        saveButton = new JButton("Lưu");
        cancelButton = new JButton("Hủy");

        add(mgvLabel);
        add(mgvField);
        add(fullNameLabel);
        add(fullNameField);
        add(saveButton);
        add(cancelButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTeacher();
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

    private void updateTeacher() {
        String mgv = mgvField.getText().trim();
        String fullName = fullNameField.getText().trim();

        if (mgv.isEmpty() || fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ thông tin.");
            return;
        }

        try {
            TeacherController.updateTeacher(teacher.getId(), mgv, fullName);
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Cập nhật giáo viên thành công.");
            parentPanel.loadTeachers(); // Refresh danh sách giáo viên sau khi cập nhật
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật giáo viên: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
