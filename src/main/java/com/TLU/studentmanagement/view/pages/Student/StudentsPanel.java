package main.java.com.TLU.studentmanagement.view.pages.Student;

import com.formdev.flatlaf.FlatClientProperties;
import main.java.com.TLU.studentmanagement.controller.UserController;
import main.java.com.TLU.studentmanagement.model.Major;
import main.java.com.TLU.studentmanagement.model.Teacher;
import main.java.com.TLU.studentmanagement.model.User;
import main.java.com.TLU.studentmanagement.session.UserSession;
import raven.toast.Notifications;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;


public class StudentsPanel extends JPanel {
    private UserController userController;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton refreshButton;
    private List<Major> majors;
    private List<User> user;

    public StudentsPanel() {
        userController = new UserController();
        initUI();
        loadUserTable();
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
        JLabel titleLabel = new JLabel("Danh sách sinh viên", JLabel.CENTER);
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
                loadUserTable();
            }
        });

        // Nút Thêm sinh viên
        addButton = new JButton("Thêm sinh viên");
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
                    AddStudentForm addStudentForm = new AddStudentForm(userController);
                    addStudentForm.setVisible(true);
                    loadUserTable(); // Tải lại bảng sau khi thêm
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

        // Tạo bảng
        tableModel = new DefaultTableModel(new Object[]{"STT", "Tên", "Mã sinh viên", "GVCN", "Chuyên ngành", "Lớp", "Hành động"}, 0);
        userTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Chỉ cho phép chỉnh sửa cột Hành động
            }
        };

        userTable.setFillsViewportHeight(true);
        userTable.setRowHeight(40);
        userTable.setBackground(Color.WHITE);
        userTable.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        userTable.setShowVerticalLines(true);
        userTable.setShowHorizontalLines(true);
        userTable.setGridColor(new Color(220, 220, 220));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < userTable.getColumnCount(); i++) {
            userTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JTableHeader header = userTable.getTableHeader();
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(Color.BLACK);

        userTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        userTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox()));

        // Add a JScrollPane with padding around the table
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Add padding to the scroll pane
        add(scrollPane, BorderLayout.CENTER);
    }

    void loadUserTable() {
        List<User> users = userController.getAllUsers();
        tableModel.setRowCount(0); // Clear existing rows

//        System.out.println(users.toString());

        for (User user : users) {
//            String gvcnFullName = user.getGvcn(); // Lấy tên giáo viên chủ nhiệm từ user
//            String majorName = user.getMajorId(); // Lấy tên chuyên ngành từ user
//
//            System.out.println("User: " + user);
//            System.out.println("Ten gv: " + gvcnFullName);
//            System.out.println("Ten nganh: " + majorName);
//
//
//            // Tìm tên chuyên ngành dựa trên majorId
//            for (Major major : majors) {
//                if (major.getId().equals(user.getMajorId())) {
//                    majorName = major.getName();
//                    break;
//                }
//            }

            tableModel.addRow(new Object[]{
                    tableModel.getRowCount() + 1, // Số thứ tự
                    user.getFullName(), // Tên đầy đủ
                    user.getMsv(), // Mã sinh viên
                    user.getGvcnName(), // Tên giáo viên chủ nhiệm
                    user.getMajorName(), // Tên chuyên ngành
                    user.getClassName(), // Tên lớp
                    "Sửa | Xóa | Xem chi tiết" // Các tác vụ khác
            });
        }
    }


    private void showUserDetails(User user) {
        StudentDetail studentDetail = new StudentDetail(user);
        studentDetail.setVisible(true);
    }

    private void showUpdateUserForm(User user) {
        UpdateStudentForm updateStudentForm = new UpdateStudentForm(user, userController);
        updateStudentForm.setVisible(true);
        loadUserTable(); // Tải lại bảng sau khi cập nhật
    }

    private class ButtonRenderer extends JPanel implements TableCellRenderer {
        private final JButton editButton;
        private final JButton deleteButton;
        private final JButton detailButton;

        public ButtonRenderer() {
            setLayout(new FlowLayout());

            editButton = new JButton("Sửa");
            deleteButton = new JButton("Xóa");
            detailButton = new JButton("Xem chi tiết");

            add(editButton);
            add(deleteButton);
            add(detailButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        protected JPanel panel;
        private final JButton editButton;
        private final JButton deleteButton;
        private final JButton detailButton;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);

            panel = new JPanel(new FlowLayout());
            editButton = new JButton("Sửa");
            deleteButton = new JButton("Xóa");
            detailButton = new JButton("Xem chi tiết");

            panel.add(editButton);
            panel.add(deleteButton);
            panel.add(detailButton);

            editButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int row = userTable.getSelectedRow();
                    String userId = tableModel.getValueAt(row, 2).toString();
                    User user = userController.getAllUsers().stream().filter(u -> u.getMsv().equals(userId)).findFirst().orElse(null);
                    if (user != null) {
                        showUpdateUserForm(user);
                    }
                }
            });

            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int row = userTable.getSelectedRow();
                    String userId = tableModel.getValueAt(row, 2).toString();
                    userController.deleteUser(userId);
                    loadUserTable(); // Tải lại bảng sau khi xóa
                }
            });

            detailButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int row = userTable.getSelectedRow();
                    String userId = tableModel.getValueAt(row, 2).toString();
                    User user = userController.getAllUsers().stream().filter(u -> u.getMsv().equals(userId)).findFirst().orElse(null);
                    if (user != null) {
                        showUserDetails(user);
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }

        @Override
        public boolean stopCellEditing() {
            fireEditingStopped();
            return super.stopCellEditing();
        }
    }
}
