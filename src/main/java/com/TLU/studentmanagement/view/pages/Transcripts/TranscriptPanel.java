package main.java.com.TLU.studentmanagement.view.pages.Transcripts;

import main.java.com.TLU.studentmanagement.controller.transcripts.TranscriptController;
import main.java.com.TLU.studentmanagement.controller.semesters.SemesterController;
import main.java.com.TLU.studentmanagement.controller.UserController;
import main.java.com.TLU.studentmanagement.model.Transcript;
import main.java.com.TLU.studentmanagement.model.Semester;
import main.java.com.TLU.studentmanagement.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TranscriptPanel extends JPanel {

    private TranscriptController transcriptController;
    private JTextField searchField;
    private JButton searchButton;
    private JButton addTranscriptButton;
    private JTable transcriptTable;
    private DefaultTableModel tableModel;

    private List<User> students;
    private List<Semester> semesters;

    public TranscriptPanel() {
        transcriptController = new TranscriptController();
        initUI();
        loadStudentAndSemesterData();
        loadTranscripts();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        searchField = new JTextField(20);
        searchButton = new JButton("Tìm kiếm");
        addTranscriptButton = new JButton("Thêm bảng điểm mới");

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchTranscripts();
            }
        });

        addTranscriptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAddTranscriptForm();
            }
        });

        topPanel.add(new JLabel("Tìm kiếm:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(addTranscriptButton);

        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Student", "Semester", "Actions"}, 0);
        transcriptTable = new JTable(tableModel);
        transcriptTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        transcriptTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));

        add(new JScrollPane(transcriptTable), BorderLayout.CENTER);
    }

    private void loadStudentAndSemesterData() {
        try {
            students = UserController.getAllUsers();
            semesters = SemesterController.getAllSemesters();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadTranscripts() {
        List<Transcript> transcripts = transcriptController.getAllTranscripts();
        tableModel.setRowCount(0); // Clear existing rows
        for (Transcript transcript : transcripts) {
            String studentName = getStudentNameById(transcript.getStudentId());
            String semesterName = getSemesterNameById(transcript.getSemesterId());
            tableModel.addRow(new Object[]{
                    studentName,
                    semesterName,
                    "View"
            });
        }
    }

    private String getStudentNameById(String studentId) {
        for (User student : students) {
            if (student.getId().equals(studentId)) {
                return student.getFullName() + " - " + student.getMsv();
            }
        }
        return null;
    }

    private String getSemesterNameById(String semesterId) {
        for (Semester semester : semesters) {
            if (semester.getId().equals(semesterId)) {
                return semester.getSemester() + " - " + semester.getGroup() + " - Năm học: " + semester.getYear();
            }
        }
        return null;
    }

    private void searchTranscripts() {
        String keyword = searchField.getText().toLowerCase();

        List<Transcript> transcripts = transcriptController.searchTranscripts(keyword);
        tableModel.setRowCount(0); // Clear existing rows
        for (Transcript transcript : transcripts) {
            String studentName = getStudentNameById(transcript.getStudentId());
            String semesterName = getSemesterNameById(transcript.getSemesterId());
            tableModel.addRow(new Object[]{
                    studentName,
                    semesterName,
                    "View"
            });
        }
    }

    private void openAddTranscriptForm() {
        new AddTranscriptForm(
                (Frame) SwingUtilities.getWindowAncestor(this),
                students,
                semesters,
                this
        ).setVisible(true);
    }

    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private String label;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            JButton button = new JButton(label);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    viewTranscript(row);
                }
            });
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return label;
        }
    }

    private void viewTranscript(int row) {
        String studentDisplay = (String) tableModel.getValueAt(row, 0);
        String semesterDisplay = (String) tableModel.getValueAt(row, 1);
        String studentId = getStudentIdByDisplay(studentDisplay);
        String semesterId = getSemesterIdByDisplay(semesterDisplay);

        Transcript transcript = transcriptController.getTranscriptBySemester(studentId, semesterId);
        // Implement logic to display transcript details and actions (edit/delete)
        JFrame frame = new JFrame("Transcript Details");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);

        JTextArea transcriptDetails = new JTextArea(transcript.toString()); // Customize to show detailed information
        frame.add(new JScrollPane(transcriptDetails), BorderLayout.CENTER);

        JPanel actionsPanel = new JPanel();
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement edit functionality
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transcriptController.deleteTranscript(transcript.getId());
                loadTranscripts(); // Reload transcripts after deletion
                frame.dispose();
            }
        });

        actionsPanel.add(editButton);
        actionsPanel.add(deleteButton);
        frame.add(actionsPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private String getStudentIdByDisplay(String studentDisplay) {
        String msv = studentDisplay.split(" - ")[1]; // Extract msv from display string
        for (User student : students) {
            if (student.getMsv().equals(msv)) {
                return student.getId();
            }
        }
        return null;
    }

    private String getSemesterIdByDisplay(String semesterDisplay) {
        String[] parts = semesterDisplay.split(" - ");
        String semester = parts[0];
        String group = parts[1];
        String year = parts[2].replace("Năm học: ", "");

        for (Semester sem : semesters) {
            if (sem.getSemester().equals(semester) && sem.getGroup().equals(group) && sem.getYear().equals(year)) {
                return sem.getId();
            }
        }
        return null;
    }
}
