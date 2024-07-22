package main.java.com.TLU.studentmanagement.view.pages.Grades;

import main.java.com.TLU.studentmanagement.controller.grades.GradeController;
import main.java.com.TLU.studentmanagement.model.Grade;
import main.java.com.TLU.studentmanagement.model.Transcript;
import main.java.com.TLU.studentmanagement.view.pages.Transcripts.TranscriptDetail;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddGradeForm extends JDialog {
    private GradeController gradeController;
    private JComboBox<String> courseComboBox;
    private JTextField midScoreField;
    private JTextField finalScoreField;
    private JButton submitButton;
    private JButton cancelButton;
    private Transcript transcript;

    public AddGradeForm(Frame owner, GradeController gradeController, Transcript transcript) {
        super(owner, "Thêm điểm", true);
        this.gradeController = gradeController;
        this.transcript = transcript;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setSize(300, 200);
        setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(4, 2));
        courseComboBox = new JComboBox<>(getCourseNames());
        midScoreField = new JTextField();
        finalScoreField = new JTextField();

        formPanel.add(new JLabel("Môn học:"));
        formPanel.add(courseComboBox);
        formPanel.add(new JLabel("Điểm giữa kỳ:"));
        formPanel.add(midScoreField);
        formPanel.add(new JLabel("Điểm cuối kỳ:"));
        formPanel.add(finalScoreField);

        JPanel buttonPanel = new JPanel();
        submitButton = new JButton("Thêm");
        cancelButton = new JButton("Hủy");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addGrade();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private String[] getCourseNames() {
        // Thay thế bằng cách lấy tên môn học từ controller hoặc dữ liệu thực tế
        return new String[]{"Môn 1", "Môn 2", "Môn 3"};
    }

    private void addGrade() {
        try {
            String selectedCourse = (String) courseComboBox.getSelectedItem();
            double midScore = Double.parseDouble(midScoreField.getText());
            double finalScore = Double.parseDouble(finalScoreField.getText());

            Grade newGrade = new Grade();
            newGrade.setCourse(selectedCourse);
            newGrade.setMidScore(midScore);
            newGrade.setFinalScore(finalScore);
            newGrade.setAverageScore(midScore*0.3 + finalScore*0.7);

            // Gọi controller để lưu điểm mới vào server
            gradeController.createGrade(newGrade);

            // Cập nhật bảng điểm trong TranscriptDetail
            TranscriptDetail.loadTableData();

            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Điểm không hợp lệ. Vui lòng nhập lại.");
        }
    }
}
