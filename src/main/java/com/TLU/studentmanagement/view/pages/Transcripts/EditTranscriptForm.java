package main.java.com.TLU.studentmanagement.view.pages.Transcripts;

import main.java.com.TLU.studentmanagement.controller.transcripts.TranscriptController;
import main.java.com.TLU.studentmanagement.controller.semesters.SemesterController;
import main.java.com.TLU.studentmanagement.controller.UserController;
import main.java.com.TLU.studentmanagement.model.Transcript;
import main.java.com.TLU.studentmanagement.model.Semester;
import main.java.com.TLU.studentmanagement.model.User;
import raven.toast.Notifications;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EditTranscriptForm extends JDialog {

    private JComboBox<String> studentComboBox;
    private JComboBox<String> semesterComboBox;
    private JButton saveButton;
    private JButton cancelButton;

    private TranscriptController transcriptController;
    private TranscriptPanel parentPanel;
    private List<User> students;
    private List<Semester> semesters;
    private Transcript transcript;

    public EditTranscriptForm(Frame parent, List<User> students, List<Semester> semesters, Transcript transcript, TranscriptPanel parentPanel) {
        super(parent, "Chỉnh sửa bảng điểm", true);
        this.students = students;
        this.semesters = semesters;
        this.transcript = transcript;
        this.parentPanel = parentPanel;
        this.transcriptController = new TranscriptController();
        initUI();
        populateFields();
    }

    private void initUI() {
        setLayout(new GridLayout(3, 2, 10, 10));

        // Initialize components
        studentComboBox = new JComboBox<>();
        semesterComboBox = new JComboBox<>();
        saveButton = new JButton("Lưu");
        cancelButton = new JButton("Hủy");

        // Populate combo boxes
        for (User student : students) {
            studentComboBox.addItem(student.getFullName() + " - " + student.getMsv());
        }

        for (Semester semester : semesters) {
            semesterComboBox.addItem(semester.getSemester() + " - " + semester.getGroup() + " - Năm học: " + semester.getYear());
        }

        // Add components to the form
        add(new JLabel("Sinh viên:"));
        add(studentComboBox);
        add(new JLabel("Học kỳ:"));
        add(semesterComboBox);
        add(saveButton);
        add(cancelButton);

        // Set button actions
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTranscript();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // Set default form properties
        setSize(400, 200);
        setLocationRelativeTo(getParent());
    }

    private void populateFields() {
        if (transcript != null) {
            String studentId = transcript.getStudentId();
            String semesterId = transcript.getSemesterId();

            String studentDisplay = getStudentDisplayById(studentId);
            String semesterDisplay = getSemesterDisplayById(semesterId);

            studentComboBox.setSelectedItem(studentDisplay);
            semesterComboBox.setSelectedItem(semesterDisplay);
        }
    }

    private void updateTranscript() {
        try {
            String studentDisplay = (String) studentComboBox.getSelectedItem();
            String semesterDisplay = (String) semesterComboBox.getSelectedItem();

            String studentId = getStudentIdByDisplay(studentDisplay);
            String semesterId = getSemesterIdByDisplay(semesterDisplay);

            if (studentId != null && semesterId != null) {
                Transcript updatedTranscript = new Transcript(studentId, semesterId);
//                updatedTranscript.setId(transcript.getId()); // Preserve the existing transcript ID
                int result = transcriptController.updateTranscript(transcript.getId(), updatedTranscript);

                if (result == 1) {
                    parentPanel.loadTranscripts(); // Tải lại danh sách bảng điểm trong panel cha
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, "Cập nhật bảng điểm thành công.");
                    dispose(); // Đóng form
                } else if (result == -1) {
                    Notifications.getInstance().show(Notifications.Type.ERROR, "Bảng điểm của sinh viên đã tồn tại.");
                } else {
                    Notifications.getInstance().show(Notifications.Type.ERROR, "Không thể cập nhật bảng điểm. Vui lòng thử lại sau.");
                }
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Dữ liệu không hợp lệ.");
            }
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Không thể cập nhật bảng điểm. Vui lòng thử lại sau.");
        }
    }

    private String getStudentDisplayById(String studentId) {
        for (User student : students) {
            if (student.getId().equals(studentId)) {
                return student.getFullName() + " - " + student.getMsv();
            }
        }
        return null;
    }

    private String getSemesterDisplayById(String semesterId) {
        for (Semester semester : semesters) {
            if (semester.getId().equals(semesterId)) {
                return semester.getSemester() + " - " + semester.getGroup() + " - Năm học: " + semester.getYear();
            }
        }
        return null;
    }

    private String getStudentIdByDisplay(String displayText) {
        for (User student : students) {
            if ((student.getFullName() + " - " + student.getMsv()).equals(displayText)) {
                return student.getId();
            }
        }
        return null;
    }

    private String getSemesterIdByDisplay(String displayText) {
        for (Semester semester : semesters) {
            if ((semester.getSemester() + " - " + semester.getGroup() + " - Năm học: " + semester.getYear()).equals(displayText)) {
                return semester.getId();
            }
        }
        return null;
    }
}