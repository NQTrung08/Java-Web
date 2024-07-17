package main.java.com.TLU.studentmanagement.view.pages.Student;

import main.java.com.TLU.studentmanagement.controller.UserController;
import main.java.com.TLU.studentmanagement.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class StudentsPanel extends JPanel {
    private UserController userController;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton addButton;

    public StudentsPanel() {
        userController = new UserController();
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Tạo bảng
        tableModel = new DefaultTableModel(new Object[]{"STT", "Tên", "Mã sinh viên", "Giáo viên chủ nhiệm", "Lớp", "Hành động"}, 0);
        userTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Chỉ cho phép chỉnh sửa cột Hành động
            }
        };

        // Tạo cột hành động
        userTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        userTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));

        loadUserTable();

        add(new JScrollPane(userTable), BorderLayout.CENTER);

        // Nút thêm sinh viên
        addButton = new JButton("Thêm sinh viên");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddStudentForm addStudentForm = new AddStudentForm(userController);
                addStudentForm.setVisible(true);
                loadUserTable(); // Tải lại bảng sau khi thêm
            }
        });
        add(addButton, BorderLayout.SOUTH);
    }

    private void loadUserTable() {
        List<User> users = userController.getAllUsers();
        tableModel.setRowCount(0); // Clear existing rows

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            tableModel.addRow(new Object[]{
                    i + 1,
                    user.getFullName(),
                    user.getMsv(),
                    user.getGvcn(),
                    user.getClassName(),
                    "Sửa | Xóa | Xem chi tiết"
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
    }
}






//package main.java.com.TLU.studentmanagement.view.pages.Student;
//
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import main.java.com.TLU.studentmanagement.controller.UserController;
//import main.java.com.TLU.studentmanagement.model.User;
//import main.java.com.TLU.studentmanagement.session.UserSession;
//
//import javax.swing.*;
//        import javax.swing.table.*;
//        import java.awt.*;
//        import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class StudentsPanel extends JPanel {
//
//    private JButton addButton;
//    private JTable userTable;
//    private UserTableModel userTableModel;
//    private Map<String, String> majors = new HashMap<>();
//    private Map<String, String> teachers = new HashMap<>();
//
//    public StudentsPanel() {
//        initUI();
//        getAllUsers();
//        loadMajorsAndTeachers();
//    }
//
//    private void initUI() {
//        setLayout(new BorderLayout());
//
//        JLabel titleLabel = new JLabel("Danh sách sinh viên", JLabel.CENTER);
//        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
//        add(titleLabel, BorderLayout.NORTH);
//
//        userTableModel = new UserTableModel();
//        userTable = new JTable(userTableModel);
//        userTable.setFillsViewportHeight(true);
//
//        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
//        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
//
//        userTable.setRowHeight(40);
//
//        userTable.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//        userTable.setShowVerticalLines(true);
//        userTable.setShowHorizontalLines(true);
//
//        for (int i = 0; i < userTable.getColumnCount(); i++) {
//            userTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
//        }
//
//        JTableHeader header = userTable.getTableHeader();
//        header.setFont(header.getFont().deriveFont(Font.BOLD, 18));
//        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
//
//        if (UserSession.getUser() != null && UserSession.getUser().isAdmin()) {
//            userTable.getColumnModel().getColumn(6).setCellRenderer(new ActionRenderer());
//            userTable.getColumnModel().getColumn(6).setCellEditor(new ActionEditor());
//
//            addButton = new JButton("Thêm sinh viên");
//            addButton.setFocusPainted(false);
//            addButton.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    AddStudentForm addStudentForm = new AddStudentForm(majors, teachers);
//                    addStudentForm.showForm();
//                    getAllUsers();
//                }
//            });
//            add(addButton, BorderLayout.SOUTH);
//        }
//
//        add(new JScrollPane(userTable), BorderLayout.CENTER);
//    }
//
//    private void loadMajorsAndTeachers() {
//        try {
//            // Lấy dữ liệu chuyên ngành
//            URL url = new URL("http://localhost:8080/api/major/getAll");
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod("GET");
//            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            String inputLine;
//            StringBuilder content = new StringBuilder();
//            while ((inputLine = in.readLine()) != null) {
//                content.append(inputLine);
//            }
//            in.close();
//            con.disconnect();
//            JsonArray majorArray = JsonParser.parseString(content.toString()).getAsJsonArray();
//            for (int i = 0; i < majorArray.size(); i++) {
//                JsonObject majorObj = majorArray.get(i).getAsJsonObject();
//                majors.put(majorObj.get("id").getAsString(), majorObj.get("name").getAsString());
//            }
//
//            // Lấy dữ liệu giáo viên
//            url = new URL("http://localhost:8080/api/teacher/getAll");
//            con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod("GET");
//            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            content = new StringBuilder();
//            while ((inputLine = in.readLine()) != null) {
//                content.append(inputLine);
//            }
//            in.close();
//            con.disconnect();
//            JsonArray teacherArray = JsonParser.parseString(content.toString()).getAsJsonArray();
//            for (int i = 0; i < teacherArray.size(); i++) {
//                JsonObject teacherObj = teacherArray.get(i).getAsJsonObject();
//                teachers.put(teacherObj.get("id").getAsString(), teacherObj.get("name").getAsString());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
//        }
//    }
//
//    private void showUpdateUserForm(User user) {
//        UpdateStudentForm updateStudentForm = new UpdateStudentForm(user, majors, teachers);
//        updateStudentForm.showForm();
//        getAllUsers();
//    }
//
//    private void showUserDetails(User user) {
//        StudentDetail studentDetail = new StudentDetail(user);
//        studentDetail.showDetails();
//    }
//
//    private void getAllUsers() {
//        try {
//            List<User> users = UserController.getAllUsers();
//            for (User user : users) {
//                // Lấy tên giáo viên thông qua API
//                URL url = new URL("http://localhost:8080/api/teacher/" + user.getGvcn());
//                HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                con.setRequestMethod("GET");
//                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//                String inputLine;
//                StringBuilder content = new StringBuilder();
//                while ((inputLine = in.readLine()) != null) {
//                    content.append(inputLine);
//                }
//                in.close();
//                con.disconnect();
//                JsonObject teacherObj = JsonParser.parseString(content.toString()).getAsJsonObject();
//                user.setGvcn(teacherObj.get("name").getAsString());
//
//                // Lấy tên chuyên ngành thông qua API
//                url = new URL("http://localhost:8080/api/major/" + user.getMajor());
//                con = (HttpURLConnection) url.openConnection();
//                con.setRequestMethod("GET");
//                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//                content = new StringBuilder();
//                while ((inputLine = in.readLine()) != null) {
//                    content.append(inputLine);
//                }
//                in.close();
//                con.disconnect();
//                JsonObject majorObj = JsonParser.parseString(content.toString()).getAsJsonObject();
//                user.setMajor(majorObj.get("name").getAsString());
//            }
//            userTableModel.setUsers(users);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
//        }
//    }
//
//    private void deleteUser(User user) {
//        int option = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa sinh viên " + user.getFullName() + " không?", "Xóa sinh viên", JOptionPane.YES_NO_OPTION);
//        if (option == JOptionPane.YES_OPTION) {
//            try {
//                UserController.deleteUser(user.getId());
//                getAllUsers();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
//            }
//        }
//    }
//
//    private class UserTableModel extends AbstractTableModel {
//
//        private final String[] columnNames = {"STT", "Mã SV", "Họ và tên", "Chuyên ngành", "GVCN", "Lớp", "Hành động"};
//        private List<User> users = new ArrayList<>();
//
//        public void setUsers(List<User> users) {
//            this.users = users;
//            fireTableDataChanged();
//        }
//
//        @Override
//        public int getRowCount() {
//            return users.size();
//        }
//
//        @Override
//        public int getColumnCount() {
//            return columnNames.length;
//        }
//
//        @Override
//        public Object getValueAt(int rowIndex, int columnIndex) {
//            User user = users.get(rowIndex);
//            switch (columnIndex) {
//                case 0:
//                    return rowIndex + 1;
//                case 1:
//                    return user.getMsv();
//                case 2:
//                    return user.getFullName();
//                case 3:
//                    return user.getMajor();
//                case 4:
//                    return user.getGvcn();
//                case 5:
//                    return user.getClassName();
//                case 6:
//                    return "Hành động";
//                default:
//                    return null;
//            }
//        }
//
//        @Override
//        public String getColumnName(int column) {
//            return columnNames[column];
//        }
//
//        @Override
//        public boolean isCellEditable(int rowIndex, int columnIndex) {
//            return columnIndex == 6;
//        }
//    }
//
//    private class ActionRenderer extends DefaultTableCellRenderer {
//        @Override
//        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
//            panel.add(new JButton("Sửa"));
//            panel.add(new JButton("Xóa"));
//            panel.add(new JButton("Xem chi tiết"));
//            return panel;
//        }
//    }
//
//    private class ActionEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
//
//        private final JPanel panel;
//        private final JButton editButton;
//        private final JButton deleteButton;
//        private final JButton viewButton;
//        private User currentUser;
//
//        public ActionEditor() {
//            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
//
//            editButton = new JButton("Sửa");
//            deleteButton = new JButton("Xóa");
//            viewButton = new JButton("Xem chi tiết");
//
//            editButton.addActionListener(this);
//            deleteButton.addActionListener(this);
//            viewButton.addActionListener(this);
//
//            panel.add(editButton);
//            panel.add(deleteButton);
//            panel.add(viewButton);
//        }
//
//        @Override
//        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//            currentUser = ((UserTableModel) table.getModel()).users.get(row);
//            return panel;
//        }
//
//        @Override
//        public Object getCellEditorValue() {
//            return currentUser;
//        }
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            if (e.getSource() == editButton) {
//                showUpdateUserForm(currentUser);
//            } else if (e.getSource() == deleteButton) {
//                deleteUser(currentUser);
//            } else if (e.getSource() == viewButton) {
//                showUserDetails(currentUser);
//            }
//            fireEditingStopped();
//        }
//    }
//}


