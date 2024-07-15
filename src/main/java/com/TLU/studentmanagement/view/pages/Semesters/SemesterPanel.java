package main.java.com.TLU.studentmanagement.view.pages.Semesters;

import main.java.com.TLU.studentmanagement.controller.semesters.SemesterController;
import main.java.com.TLU.studentmanagement.model.Semester;
import main.java.com.TLU.studentmanagement.session.TeacherSession;
import main.java.com.TLU.studentmanagement.session.UserSession;
import raven.toast.Notifications;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SemesterPanel extends JPanel {

    private JButton addButton;
    private JTable semesterTable;
    private SemesterTableModel semesterTableModel;

    public SemesterPanel() {
        initUI();
        getAllSemesters();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Thông tin học kỳ", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        semesterTableModel = new SemesterTableModel();
        semesterTable = new JTable(semesterTableModel);
        semesterTable.setFillsViewportHeight(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        semesterTable.setRowHeight(40);

        semesterTable.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        semesterTable.setShowVerticalLines(true);
        semesterTable.setShowHorizontalLines(true);

        for (int i = 0; i < semesterTable.getColumnCount(); i++) {
            semesterTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JTableHeader header = semesterTable.getTableHeader();
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));

        semesterTable.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer("Sửa"));
        semesterTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer("Xóa"));

        semesterTable.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor("Sửa"));
        semesterTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor("Xóa"));

        add(new JScrollPane(semesterTable), BorderLayout.CENTER);

        addButton = new JButton("Thêm học kỳ");
        addButton.setFocusPainted(false);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (UserSession.getUser() != null && UserSession.getUser().isAdmin()) {
                    showAddSemesterForm();
                } else {
                    Notifications.getInstance().show(Notifications.Type.WARNING, "Bạn không có quyền xem thông tin này.");
                }
            }
        });
        add(addButton, BorderLayout.SOUTH);
    }

    private void showAddSemesterForm() {
        JTextField semesterField = new JTextField();
        JTextField groupField = new JTextField();
        JTextField yearField = new JTextField();

        Object[] message = {
                "Học kỳ:", semesterField,
                "Nhóm:", groupField,
                "Năm:", yearField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Thêm học kỳ", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String semester = semesterField.getText();
            String group = groupField.getText();
            String year = yearField.getText();

            try {
                SemesterController.createSemester(semester, group, year);
                getAllSemesters();
            } catch (Exception ex) {
                ex.printStackTrace();
                Notifications.getInstance().show(Notifications.Type.ERROR, "Error: " + ex.getMessage());
            }
        }
    }

    protected void getAllSemesters() {
        if (UserSession.getUser() != null && UserSession.getUser().isAdmin()) {
            try {
                List<Semester> semesters = SemesterController.getAllSemesters();
                semesterTableModel.setSemesters(semesters);
            } catch (Exception ex) {
                ex.printStackTrace();
                Notifications.getInstance().show(Notifications.Type.ERROR, "Error: " + ex.getMessage());
            }
        } else {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Bạn không có quyền xem thông tin này.");
        }
    }

    private void deleteSemester(Semester semester) {
        int option = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa học kỳ " + semester.getSemester() + " không?", "Xóa học kỳ", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                SemesterController.deleteSemester(semester.getId());
                getAllSemesters();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        }
    }

    private class SemesterTableModel extends AbstractTableModel {

        private final String[] columnNames = {"Học kỳ", "Nhóm", "Năm", "Hành động", ""};
        private List<Semester> semesters = new ArrayList<>();

        public void setSemesters(List<Semester> semesters) {
            this.semesters = semesters;
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return semesters.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Semester semester = semesters.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return semester.getSemester();
                case 1:
                    return semester.getGroup();
                case 2:
                    return semester.getYear();
                case 3:
                    return "Sửa";
                case 4:
                    return "Xóa";
                default:
                    throw new IllegalArgumentException("Invalid column index");
            }
        }

        @Override
        public String getColumnName(int columnIndex) {
            return columnNames[columnIndex];
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 3 || columnIndex == 4;
        }
    }

    private class ButtonRenderer extends JButton implements TableCellRenderer {
        private final String buttonType;

        public ButtonRenderer(String buttonType) {
            this.buttonType = buttonType;
            setText(buttonType);
            setFont(getFont().deriveFont(Font.BOLD));
            setFocusPainted(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            return this;
        }
    }

    private class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private final JButton button;
        private Semester currentSemester;

        public ButtonEditor(String buttonType) {
            button = new JButton(buttonType);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int row = semesterTable.getSelectedRow();
                    if (row != -1) {
                        currentSemester = semesterTableModel.semesters.get(row);
                        if ("Sửa".equals(buttonType)) {
                            UpdateSemesterForm.showUpdateSemesterForm(currentSemester, SemesterPanel.this);
                        } else if ("Xóa".equals(buttonType)) {
                            deleteSemester(currentSemester);
                        }
                    }
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }

        @Override
        public boolean stopCellEditing() {
            fireEditingStopped();
            return true;
        }
    }
}
