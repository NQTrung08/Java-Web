package main.java.com.TLU.studentmanagement.view.pages.Transcripts;

import main.java.com.TLU.studentmanagement.controller.grades.GradeController;
import main.java.com.TLU.studentmanagement.controller.transcripts.TranscriptController;
import main.java.com.TLU.studentmanagement.controller.courses.CourseController;
import main.java.com.TLU.studentmanagement.model.Transcript;
import main.java.com.TLU.studentmanagement.model.Grade;
import main.java.com.TLU.studentmanagement.model.User;
import main.java.com.TLU.studentmanagement.view.pages.Grades.AddGradeForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TranscriptDetail extends JPanel {
    private TranscriptController transcriptController;
    private GradeController gradeController;
    private CourseController courseController; // Add this
    private static Transcript transcript;
    private JTable gradeTable;
    private static DefaultTableModel tableModel;

    public TranscriptDetail(Transcript transcript) {
        this.transcript = transcript;
        this.transcriptController = new TranscriptController();
        this.gradeController = new GradeController();
        this.courseController = new CourseController(); // Initialize this
//        loadTableData();
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Bảng điểm sinh viên", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        JButton addButton = new JButton("Thêm điểm");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddGradeForm();
            }
        });
        topPanel.add(addButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"Số thứ tự", "Tên môn", "Điểm giữa kỳ", "Điểm cuối kỳ", "Điểm tổng kết", "Hành động"};
        tableModel = new DefaultTableModel(columnNames, 0);
        gradeTable = new JTable(tableModel);
        gradeTable.setRowHeight(40);

        // Thêm dữ liệu vào bảng
        loadTableData();

        // Cài đặt Renderer và Editor cho cột hành động
        gradeTable.getColumn("Hành động").setCellRenderer(new ButtonRenderer());
        gradeTable.getColumn("Hành động").setCellEditor(new ButtonEditor(new JCheckBox()));

        add(new JScrollPane(gradeTable), BorderLayout.CENTER);
    }

    private void showAddGradeForm() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        AddGradeForm addGradeForm = new AddGradeForm(parentFrame, gradeController, courseController, transcript);
        addGradeForm.setVisible(true);
    }

    public static void loadTableData() {
        tableModel.setRowCount(0); // Xóa tất cả các hàng hiện tại

        for (int i = 0; i < transcript.getGrades().size(); i++) {
            Grade grade = transcript.getGrades().get(i);
            Object[] rowData = {
                    i + 1,
                    grade.getCourse(),
                    grade.getMidScore(),
                    grade.getFinalScore(),
                    grade.getAverageScore(),
                    "Sửa, Xóa"
            };
            tableModel.addRow(rowData);
        }
    }

    class ButtonRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private JButton editButton;
        private JButton deleteButton;

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER));
            editButton = new JButton("Sửa");
            deleteButton = new JButton("Xóa");

            add(editButton);
            add(deleteButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton editButton;
        private JButton deleteButton;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);

            // Initialize the panel and buttons
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            editButton = new JButton("Sửa");
            deleteButton = new JButton("Xóa");

            // Add buttons to the panel
            panel.add(editButton);
            panel.add(deleteButton);

            // Add action listeners
            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int row = gradeTable.getSelectedRow();
                    if (row >= 0) {
                        showEditDialog(row);
                        // Stop editing to ensure control is released properly
                        stopCellEditing();
                        loadTableData();
                    }
                }
            });

            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int row = gradeTable.getSelectedRow();
                    if (row >= 0) {
                        deleteGrade(row);
                        // Stop editing to ensure control is released properly
                        stopCellEditing();
                        loadTableData();
                    }
                }
            });
        }

        private void showEditDialog(int row) {
            Grade grade = transcript.getGrades().get(row);

            JTextField midScoreField = new JTextField(String.valueOf(grade.getMidScore()), 10);
            JTextField finalScoreField = new JTextField(String.valueOf(grade.getFinalScore()), 10);

            JPanel panel = new JPanel(new GridLayout(2, 2));
            panel.add(new JLabel("Điểm giữa kỳ:"));
            panel.add(midScoreField);
            panel.add(new JLabel("Điểm cuối kỳ:"));
            panel.add(finalScoreField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Chỉnh sửa điểm", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    double midScore = Double.parseDouble(midScoreField.getText());
                    double finalScore = Double.parseDouble(finalScoreField.getText());

                    // Update the grade object
                    grade.setMidScore(midScore);
                    grade.setFinalScore(finalScore);
                    grade.setAverageScore((midScore * 0.3) + (finalScore * 0.7));

                    // Update the data on the server
                    gradeController.updateGrade(grade.getId(), grade);

                    // Reload the table data
                    loadTableData();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Điểm không hợp lệ. Vui lòng nhập lại.");
                }
            }
        }

        private void deleteGrade(int row) {
            int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa môn học này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Grade grade = transcript.getGrades().get(row);

                // Remove the grade from the list
                transcript.getGrades().remove(row);

                // Delete the data on the server
                gradeController.deleteGrade(grade.getId());

                // Reload the table data
                loadTableData();
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }
}
