package main.java.com.TLU.studentmanagement.view.pages.Student;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import main.java.com.TLU.studentmanagement.controller.UserController;
import main.java.com.TLU.studentmanagement.model.User;
import main.java.com.TLU.studentmanagement.session.UserSession;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentsPanel extends JPanel {

    private JButton addButton;
    private JTable userTable;
    private UserTableModel userTableModel;

    public StudentsPanel() {
        initUI();
        getAllUsers();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Danh sách sinh viên", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        userTableModel = new UserTableModel();
        userTable = new JTable(userTableModel);
        userTable.setFillsViewportHeight(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        userTable.setRowHeight(40);

        userTable.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        userTable.setShowVerticalLines(true);
        userTable.setShowHorizontalLines(true);

        for (int i = 0; i < userTable.getColumnCount(); i++) {
            userTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JTableHeader header = userTable.getTableHeader();
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));

        if (UserSession.getUser() != null && UserSession.getUser().isAdmin()) {
            userTable.getColumnModel().getColumn(6).setCellRenderer(new ActionRenderer());
            userTable.getColumnModel().getColumn(6).setCellEditor(new ActionEditor());

            addButton = new JButton("Thêm sinh viên");
            addButton.setFocusPainted(false);
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showAddUserForm();
                }
            });
            add(addButton, BorderLayout.SOUTH);
        }

        add(new JScrollPane(userTable), BorderLayout.CENTER);
    }

    private void showAddUserForm() {
        JTextField fullnameField = new JTextField();
        JTextField msvField = new JTextField();
        JTextField majorField = new JTextField();
        JTextField gvcnField = new JTextField();
        JTextField classNameField = new JTextField();
        JTextField majorIdField = new JTextField();

        Object[] message = {
                "Họ và tên:", fullnameField,
                "Mã sinh viên:", msvField,
                "Chuyên ngành:", majorField,
                "GVCN:", gvcnField,
                "Lớp:", classNameField,
                "Chuyên ngành ID:", majorIdField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Thêm sinh viên", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String fullname = fullnameField.getText();
            String msv = msvField.getText();
            String major = majorField.getText();
            String gvcn = gvcnField.getText();
            String className = classNameField.getText();
            String majorId = majorIdField.getText();

            try {
                UserController.createUser(fullname, msv, major, "", gvcn, "", className, "", majorId);
                getAllUsers();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        }
    }

    private void showUpdateUserForm(User user) {
        JTextField fullnameField = new JTextField(user.getFullName());
        JTextField msvField = new JTextField(user.getMsv());
        JTextField majorField = new JTextField(user.getMajor());
        JTextField gvcnField = new JTextField(user.getGvcn());
        JTextField classNameField = new JTextField(user.getClassName());
        JTextField majorIdField = new JTextField(user.getMajorId());

        Object[] message = {
                "Họ và tên:", fullnameField,
                "Mã sinh viên:", msvField,
                "Chuyên ngành:", majorField,
                "GVCN:", gvcnField,
                "Lớp:", classNameField,
                "Chuyên ngành ID:", majorIdField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Cập nhật sinh viên", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String updatedFullname = fullnameField.getText();
            String updatedMsv = msvField.getText();
            String updatedMajor = majorField.getText();
            String updatedGvcn = gvcnField.getText();
            String updatedClassName = classNameField.getText();
            String updatedMajorId = majorIdField.getText();

            try {
                UserController.updateUser(user.getId(), updatedFullname, updatedMsv, updatedMajor, "", updatedGvcn, "", updatedClassName, "", updatedMajorId);
                getAllUsers();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        }
    }

    private void showUserDetails(User user) {
        JTextField fullnameField = new JTextField(user.getFullName());
        JTextField msvField = new JTextField(user.getMsv());
        JTextField majorField = new JTextField(user.getMajor());
        JTextField gvcnField = new JTextField(user.getGvcn());
        JTextField classNameField = new JTextField(user.getClassName());
        JTextField majorIdField = new JTextField(user.getMajorId());

        fullnameField.setEditable(false);
        msvField.setEditable(false);
        majorField.setEditable(false);
        gvcnField.setEditable(false);
        classNameField.setEditable(false);
        majorIdField.setEditable(false);

        Object[] message = {
                "Họ và tên:", fullnameField,
                "Mã sinh viên:", msvField,
                "Chuyên ngành:", majorField,
                "GVCN:", gvcnField,
                "Lớp:", classNameField,
                "Chuyên ngành ID:", majorIdField
        };

        JOptionPane.showMessageDialog(null, message, "Chi tiết sinh viên", JOptionPane.INFORMATION_MESSAGE);
    }

    private void getAllUsers() {
        try {
            List<User> users = UserController.getAllUsers();
            userTableModel.setUsers(users);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    private void deleteUser(User user) {
        int option = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa sinh viên " + user.getFullName() + " không?", "Xóa sinh viên", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                UserController.deleteUser(user.getId());
                getAllUsers();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        }
    }

    private class UserTableModel extends AbstractTableModel {

        private final String[] columnNames = {"STT", "Mã SV", "Họ và tên", "Chuyên ngành", "GVCN", "Lớp", "Hành động"};
        private List<User> users = new ArrayList<>();

        public void setUsers(List<User> users) {
            this.users = users;
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return users.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            User user = users.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return rowIndex + 1;
                case 1:
                    return user.getMsv();
                case 2:
                    return user.getFullName();
                case 3:
                    return user.getMajor();
                case 4:
                    // Lấy tên GVCN thay vì ID
//                    String gvcnName = UserController.getGvcnNameById(user.getGvcn());
//                    return gvcnName;
                    return user.getGvcn();
                case 5:
                    return user.getClassName();
                case 6:
                    return "Hành động";
                default:
                    return null;
            }
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 6;
        }
    }

    private class ActionRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.add(new JButton("Sửa"));
            panel.add(new JButton("Xóa"));
            panel.add(new JButton("Xem chi tiết"));
            return panel;
        }
    }

    private class ActionEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

        private final JPanel panel;
        private final JButton editButton;
        private final JButton deleteButton;
        private final JButton viewButton;
        private User currentUser;

        public ActionEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));

            editButton = new JButton("Sửa");
            deleteButton = new JButton("Xóa");
            viewButton = new JButton("Xem chi tiết");

            editButton.addActionListener(this);
            deleteButton.addActionListener(this);
            viewButton.addActionListener(this);

            panel.add(editButton);
            panel.add(deleteButton);
            panel.add(viewButton);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentUser = ((UserTableModel) table.getModel()).users.get(row);
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return currentUser;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == editButton) {
                showUpdateUserForm(currentUser);
            } else if (e.getSource() == deleteButton) {
                deleteUser(currentUser);
            } else if (e.getSource() == viewButton) {
                showUserDetails(currentUser);
            }
            fireEditingStopped();
        }
    }
}

//package main.java.com.TLU.studentmanagement.view.pages.Student;
//
//import javax.swing.*;
//        import javax.swing.table.*;
//        import java.awt.*;
//        import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.io.InputStreamReader;
//import java.io.BufferedReader;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import main.java.com.TLU.studentmanagement.controller.UserController;
//import main.java.com.TLU.studentmanagement.model.User;
//import main.java.com.TLU.studentmanagement.session.UserSession;
//
//import java.util.HashMap;
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
//            userTable.getColumnModel().getColumn(5).setCellRenderer(new ActionRenderer());
//            userTable.getColumnModel().getColumn(5).setCellEditor(new ActionEditor());
//
//            addButton = new JButton("Thêm sinh viên");
//            addButton.setFocusPainted(false);
//            addButton.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    showAddUserForm();
//                }
//            });
//            add(addButton, BorderLayout.SOUTH);
//        }
//
//        add(new JScrollPane(userTable), BorderLayout.CENTER);
//    }
//
//    private void loadMajorsAndTeachers() {
//        // Load Majors
//        try {
//            URL url = new URL("http://localhost:8080/api/major/getAll");
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String inputLine;
//            StringBuffer content = new StringBuffer();
//            while ((inputLine = in.readLine()) != null) {
//                content.append(inputLine);
//            }
//            in.close();
//            conn.disconnect();
//
//            JsonObject json = JsonParser.parseString(content.toString()).getAsJsonObject();
//            JsonArray data = json.getAsJsonArray("data");
//
//            for (int i = 0; i < data.size(); i++) {
//                JsonObject major = data.get(i).getAsJsonObject();
//                majors.put(major.get("_id").getAsString(), major.get("name").getAsString());
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // Load Teachers
//        try {
//            URL url = new URL("http://localhost:8080/api/teacher/get-all");
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String inputLine;
//            StringBuffer content = new StringBuffer();
//            while ((inputLine = in.readLine()) != null) {
//                content.append(inputLine);
//            }
//            in.close();
//            conn.disconnect();
//
//            JsonObject json = JsonParser.parseString(content.toString()).getAsJsonObject();
//            JsonArray data = json.getAsJsonArray("data");
//
//            for (int i = 0; i < data.size(); i++) {
//                JsonObject teacher = data.get(i).getAsJsonObject();
//                teachers.put(teacher.get("_id").getAsString(), teacher.get("fullname").getAsString());
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void showAddUserForm() {
//        JTextField fullnameField = new JTextField();
//        JTextField msvField = new JTextField();
//        JComboBox<String> majorComboBox = new JComboBox<>(majors.values().toArray(new String[0]));
//        JComboBox<String> gvcnComboBox = new JComboBox<>(teachers.values().toArray(new String[0]));
//        JTextField classNameField = new JTextField();
//
//        Object[] message = {
//                "Họ và tên:", fullnameField,
//                "Mã sinh viên:", msvField,
//                "Chuyên ngành:", majorComboBox,
//                "GVCN:", gvcnComboBox,
//                "Lớp:", classNameField,
//        };
//
//        int option = JOptionPane.showConfirmDialog(null, message, "Thêm sinh viên", JOptionPane.OK_CANCEL_OPTION);
//        if (option == JOptionPane.OK_OPTION) {
//            String fullname = fullnameField.getText();
//            String msv = msvField.getText();
//            String majorId = getKeyFromValue(majors, majorComboBox.getSelectedItem().toString());
//            String gvcnId = getKeyFromValue(teachers, gvcnComboBox.getSelectedItem().toString());
//            String className = classNameField.getText();
//
//            try {
//                UserController.createUser(fullname, msv, majorId, "", gvcnId, "", className, "", "");
//                getAllUsers();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
//            }
//        }
//    }
//
//    private void showUpdateUserForm(User user) {
//        JTextField fullnameField = new JTextField(user.getFullName());
//        JTextField msvField = new JTextField(user.getMsv());
//        JComboBox<String> majorComboBox = new JComboBox<>(majors.values().toArray(new String[0]));
//        majorComboBox.setSelectedItem(majors.get(user.getMajorId()));
//        JComboBox<String> gvcnComboBox = new JComboBox<>(teachers.values().toArray(new String[0]));
//        gvcnComboBox.setSelectedItem(teachers.get(user.getGvcn()));
//        JTextField classNameField = new JTextField(user.getClassName());
//
//        Object[] message = {
//                "Họ và tên:", fullnameField,
//                "Mã sinh viên:", msvField,
//                "Chuyên ngành:", majorComboBox,
//                "GVCN:", gvcnComboBox,
//                "Lớp:", classNameField,
//        };
//
//        int option = JOptionPane.showConfirmDialog(null, message, "Cập nhật sinh viên", JOptionPane.OK_CANCEL_OPTION);
//        if (option == JOptionPane.OK_OPTION) {
//            String updatedFullname = fullnameField.getText();
//            String updatedMsv = msvField.getText();
//            String updatedMajorId = getKeyFromValue(majors, majorComboBox.getSelectedItem().toString());
//            String updatedGvcnId = getKeyFromValue(teachers, gvcnComboBox.getSelectedItem().toString());
//            String updatedClassName = classNameField.getText();
//
//            try {
//                UserController.updateUser(user.getId(), updatedFullname, updatedMsv, updatedMajorId, "", updatedGvcnId, "", updatedClassName, "", "");
//                getAllUsers();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
//            }
//        }
//    }
//
//    private void showUserDetails(User user) {
//        JTextField fullnameField = new JTextField(user.getFullName());
//        JTextField msvField = new JTextField(user.getMsv());
//        JTextField majorField = new JTextField(majors.get(user.getMajorId()));
//        JTextField gvcnField = new JTextField(teachers.get(user.getGvcn()));
//        JTextField classNameField = new JTextField(user.getClassName());
//
//        fullnameField.setEditable(false);
//        msvField.setEditable(false);
//        majorField.setEditable(false);
//        gvcnField.setEditable(false);
//        classNameField.setEditable(false);
//
//        Object[] message = {
//                "Họ và tên:", fullnameField,
//                "Mã sinh viên:", msvField,
//                "Chuyên ngành:", majorField,
//                "GVCN:", gvcnField,
//                "Lớp:", classNameField,
//        };
//
//        JOptionPane.showMessageDialog(null, message, "Thông tin chi tiết sinh viên", JOptionPane.INFORMATION_MESSAGE);
//    }
//
//    private String getKeyFromValue(Map<String, String> map, String value) {
//        for (Map.Entry<String, String> entry : map.entrySet()) {
//            if (entry.getValue().equals(value)) {
//                return entry.getKey();
//            }
//        }
//        return null;
//    }
//
//    private void getAllUsers() {
//        try {
//            List<User> users = UserController.getAllUsers();
//            userTableModel.setUsers(users);
//        } catch (Exception e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
//        }
//    }
//
//    private class ActionRenderer extends DefaultTableCellRenderer {
//        private JPanel panel;
//        private JButton editButton;
//        private JButton deleteButton;
//        private JButton detailButton;
//
//        public ActionRenderer() {
//            panel = new JPanel(new GridLayout(1, 3, 5, 5));
//            editButton = new JButton("Sửa");
//            deleteButton = new JButton("Xóa");
//            detailButton = new JButton("Xem");
//
//            panel.add(editButton);
//            panel.add(deleteButton);
//            panel.add(detailButton);
//        }
//
//        @Override
//        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//            return panel;
//        }
//    }
//
//    private class ActionEditor extends AbstractCellEditor implements TableCellEditor {
//        private JPanel panel;
//        private JButton editButton;
//        private JButton deleteButton;
//        private JButton detailButton;
//
//        public ActionEditor() {
//            panel = new JPanel(new GridLayout(1, 3, 5, 5));
//            editButton = new JButton("Sửa");
//            deleteButton = new JButton("Xóa");
//            detailButton = new JButton("Xem");
//
//            editButton.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    int row = userTable.getSelectedRow();
//                    if (row >= 0) {
//                        User user = userTableModel.getUserAt(row);
//                        showUpdateUserForm(user);
//                    }
//                }
//            });
//
//            deleteButton.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    int row = userTable.getSelectedRow();
//                    if (row >= 0) {
//                        User user = userTableModel.getUserAt(row);
//                        int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa người dùng này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
//                        if (confirm == JOptionPane.YES_OPTION) {
//                            try {
//                                UserController.deleteUser(user.getId());
//                                getAllUsers();
//                            } catch (Exception ex) {
//                                ex.printStackTrace();
//                                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
//                            }
//                        }
//                    }
//                }
//            });
//
//            detailButton.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    int row = userTable.getSelectedRow();
//                    if (row >= 0) {
//                        User user = userTableModel.getUserAt(row);
//                        showUserDetails(user);
//                    }
//                }
//            });
//
//            panel.add(editButton);
//            panel.add(deleteButton);
//            panel.add(detailButton);
//        }
//
//        @Override
//        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//            return panel;
//        }
//
//        @Override
//        public Object getCellEditorValue() {
//            return null;
//        }
//    }
//
//    private class UserTableModel extends AbstractTableModel {
//        private String[] columnNames = {"Họ và tên", "Mã sinh viên", "Chuyên ngành", "Email", "GVCN", "Lớp", "Hành động"};
//        private List<User> users = new ArrayList<>();
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
//        public String getColumnName(int column) {
//            return columnNames[column];
//        }
//
//        @Override
//        public Object getValueAt(int rowIndex, int columnIndex) {
//            User user = users.get(rowIndex);
//            switch (columnIndex) {
//                case 0:
//                    return user.getFullName();
//                case 1:
//                    return user.getMsv();
//                case 2:
//                    return majors.get(user.getMajorId());
//                case 3:
//                    return user.getEmail();
//                case 4:
//                    return teachers.get(user.getGvcn());
//                case 5:
//                    return user.getClassName();
//                case 6:
//                    return "Hành động";
//                default:
//                    return null;
//            }
//        }
//
//        public void setUsers(List<User> users) {
//            this.users = users;
//            fireTableDataChanged();
//        }
//
//        public User getUserAt(int row) {
//            return users.get(row);
//        }
//    }
//}
