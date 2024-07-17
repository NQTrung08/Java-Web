package main.java.com.TLU.studentmanagement.view.pages.Courses;

import com.formdev.flatlaf.FlatClientProperties;
import main.java.com.TLU.studentmanagement.controller.courses.CourseController;
import main.java.com.TLU.studentmanagement.controller.majors.MajorController;
import main.java.com.TLU.studentmanagement.model.Course;
import main.java.com.TLU.studentmanagement.model.Major;
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

public class CoursePanel extends JPanel {

    private JButton addButton;
    private JButton refreshButton;
    private JTable courseTable;
    private CourseTableModel courseTableModel;
    private List<Major> majors;

    public CoursePanel() {
        initUI();
        getAllCourses();
        getAllMajors();  // Load the list of majors
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Set the theme
        UIManager.put("TitlePane.background", new Color(240, 240, 240));
        UIManager.put("Button.arc", 10);
        UIManager.put("Component.arc", 10);
        UIManager.put("Button.margin", new Insets(4, 6, 4, 6));
        UIManager.put("TextComponent.arc", 10);
        UIManager.put("TextField.margin", new Insets(4, 6, 4, 6));
        UIManager.put("PasswordField.margin", new Insets(4, 6, 4, 6));
        UIManager.put("ComboBox.padding", new Insets(4, 6, 4, 6));
        UIManager.put("TitlePane.unifiedBackground", false);
        UIManager.put("TitlePane.buttonSize", new Dimension(35, 23));
        UIManager.put("TitlePane.background", new Color(230, 230, 230));
        UIManager.put("TitlePane.foreground", Color.BLACK);

        // Panel cho tiêu đề và các nút
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Thông tin khóa học", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        // Nút Refresh
        refreshButton = new JButton("Refresh");
        refreshButton.setFocusPainted(false);
        refreshButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.setBackground(new Color(88, 86, 214));  // Accent color
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setPreferredSize(new Dimension(120, 40));
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getAllCourses();
                getAllMajors();  // Refresh the list of majors
            }
        });

        // Nút Thêm khóa học
        addButton = new JButton("Thêm khóa học");
        addButton.setFocusPainted(false);
        addButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBackground(new Color(88, 86, 214));  // Accent color
        addButton.setForeground(Color.WHITE);
        addButton.setPreferredSize(new Dimension(150, 40));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (UserSession.getUser() != null && UserSession.getUser().isAdmin()) {
                    AddCourseForm.showAddCourseForm(CoursePanel.this, majors, CoursePanel.this);
                } else {
                    Notifications.getInstance().show(Notifications.Type.ERROR, "Access denied");
                }
            }
        });

        // Panel cho các nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);

        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        courseTableModel = new CourseTableModel();
        courseTable = new JTable(courseTableModel);
        courseTable.setFillsViewportHeight(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        courseTable.setRowHeight(40);
        courseTable.setBackground(Color.WHITE);
        courseTable.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        courseTable.setShowVerticalLines(true);
        courseTable.setShowHorizontalLines(true);
        courseTable.setGridColor(new Color(220, 220, 220));

        for (int i = 0; i < courseTable.getColumnCount(); i++) {
            courseTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JTableHeader header = courseTable.getTableHeader();
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(Color.BLACK);

        courseTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer("Sửa"));
        courseTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer("Xóa"));

        courseTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor("Sửa"));
        courseTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor("Xóa"));

        // Add a JScrollPane with padding around the table
        JScrollPane scrollPane = new JScrollPane(courseTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Add padding to the scroll pane
        add(scrollPane, BorderLayout.CENTER);
    }

    public void getAllCourses() {
        if (UserSession.getUser() != null || TeacherSession.getTeacher() != null) {
            try {
                List<Course> courses = CourseController.getAllCourses();
                courseTableModel.setCourses(courses);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Bạn không có quyền xem thông tin này.");
        }
    }

    public void getAllMajors() {
        try {
            majors = MajorController.getAllMajors();
            // Call refreshMajors() on any open AddCourseForm instances
            for (Window window : Window.getWindows()) {
                if (window instanceof AddCourseForm) {
                    ((AddCourseForm) window).refreshMajors();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    private void deleteCourse(Course course) {
        int option = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa khóa học " + course.getName() + " không?", "Xóa khóa học", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                CourseController.deleteCourse(course.getId());
                getAllCourses();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        }
    }

    private class CourseTableModel extends AbstractTableModel {

        private final String[] columnNames = {"STT", "Tên", "Mã", "Số tín chỉ", "Chuyên ngành", "Hành động", ""};
        private List<Course> courses = new ArrayList<>();

        public void setCourses(List<Course> courses) {
            this.courses = courses;
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return courses.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Course course = courses.get(rowIndex);
            String majorName = "";
            for (Major major : majors) {
                if (major.getId().equals(course.getMajorId())) {
                    majorName = major.getName();
                }
            }

            switch (columnIndex) {
                case 0:
                    return rowIndex + 1; // STT
                case 1:
                    return course.getName();
                case 2:
                    return course.getCode();
                case 3:
                    return course.getCredit();
                case 4:
                    return majorName;
                case 5:
                    return "Sửa";
                case 6:
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
            return columnIndex == 5 || columnIndex == 6;
        }
    }

    private class ButtonRenderer extends JButton implements TableCellRenderer {
        private final String buttonType;

        public ButtonRenderer(String buttonType) {
            this.buttonType = buttonType;
            setText(buttonType);
            setFont(new Font("Arial", Font.BOLD, 14));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setOpaque(true);
            setBorderPainted(false);
            setBackground(new Color(87, 87, 166));  // Accent color
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(new Color(103, 102, 166));  // Accent color
                setForeground(Color.WHITE);
            }
            return this;
        }
    }

    private class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private final JButton button;
        private Course currentCourse;

        public ButtonEditor(String buttonType) {
            button = new JButton(buttonType);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setOpaque(true);
            button.setBorderPainted(false);
            button.setBackground(new Color(88, 86, 214));  // Accent color

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int row = courseTable.getSelectedRow();
                    if (UserSession.getUser() != null && UserSession.getUser().isAdmin()) {
                        if (row != -1) {
                            currentCourse = courseTableModel.courses.get(row);
                            if ("Sửa".equals(buttonType)) {
                                UpdateCourseForm.showUpdateCourseForm(currentCourse, CoursePanel.this, majors);
                            } else if ("Xóa".equals(buttonType)) {
                                deleteCourse(currentCourse);
                            }
                        }
                        fireEditingStopped();
                    } else {
                        Notifications.getInstance().show(Notifications.Type.ERROR, "Access denied");
                    }
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