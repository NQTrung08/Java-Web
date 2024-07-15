package main.java.com.TLU.studentmanagement.view.pages.Majors;

import main.java.com.TLU.studentmanagement.controller.majors.MajorController;
import main.java.com.TLU.studentmanagement.model.Major;
import main.java.com.TLU.studentmanagement.session.UserSession;
import raven.toast.Notifications;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MajorPanel extends JPanel {

    private JButton addButton;
    private JTable majorTable;
    private MajorTableModel majorTableModel;

    public MajorPanel() {
        initUI();
        getAllMajors();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Thông tin chuyên ngành", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(UIManager.getColor("Label.foreground")); // Sử dụng màu sắc từ FlatLaf
        add(titleLabel, BorderLayout.NORTH);

        // Tạo bảng với dữ liệu và tiêu đề cột
        majorTableModel = new MajorTableModel();
        majorTable = new JTable(majorTableModel);
        majorTable.setFillsViewportHeight(true);

        // Căn giữa các giá trị trong bảng
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        majorTable.setRowHeight(40); // Tùy chọn: Thay đổi chiều cao của các hàng

        // Áp dụng FlatLaf cho JTable
        majorTable.setBorder(BorderFactory.createLineBorder(UIManager.getColor("Table.gridColor")));
        majorTable.setBackground(UIManager.getColor("Table.background"));
        majorTable.setSelectionBackground(UIManager.getColor("Table.selectionBackground"));
        majorTable.setSelectionForeground(UIManager.getColor("Table.selectionForeground"));
        majorTable.setGridColor(UIManager.getColor("Table.gridColor"));
        majorTable.setShowVerticalLines(true);
        majorTable.setShowHorizontalLines(true);

        // Căn giữa các giá trị trong bảng
        for (int i = 0; i < majorTable.getColumnCount(); i++) {
            majorTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Customize headers
        JTableHeader header = majorTable.getTableHeader();
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18));
        header.setForeground(UIManager.getColor("TableHeader.foreground"));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40)); // Cập nhật chiều cao của header

        // Thêm các cột Sửa và Xóa
        majorTable.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer("Sửa"));
        majorTable.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer("Xóa"));

        // Xử lý các sự kiện nhấn nút
        majorTable.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor("Sửa"));
        majorTable.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor("Xóa"));

        add(new JScrollPane(majorTable), BorderLayout.CENTER);

        addButton = new JButton("Thêm chuyên ngành");
        addButton.setFocusPainted(false); // Loại bỏ đường viền khi nhấn
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (UserSession.getUser() != null && UserSession.getUser().isAdmin()) {
                    showAddMajorForm();
                } else {
                    Notifications.getInstance().show(Notifications.Type.WARNING, "Bạn không có quyền xem thông tin này.");
                }
            }
        });
        add(addButton, BorderLayout.SOUTH);
    }

    private void showAddMajorForm() {
        JTextField nameField = new JTextField();
        JTextField codeField = new JTextField();

        Object[] message = {
                "Tên chuyên ngành:", nameField,
                "Mã chuyên ngành:", codeField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Thêm chuyên ngành", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String code = codeField.getText();

            try {
                MajorController.createMajor(name, code);
                getAllMajors(); // Refresh the list after adding new major
            } catch (Exception ex) {
                ex.printStackTrace();
                Notifications.getInstance().show(Notifications.Type.ERROR, "Error: " + ex.getMessage());
            }
        }
    }

    private void showUpdateMajorForm(Major major) {
        JTextField nameField = new JTextField(major.getName());
        JTextField codeField = new JTextField(major.getCode());

        Object[] message = {
                "Tên chuyên ngành:", nameField,
                "Mã chuyên ngành:", codeField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Cập nhật chuyên ngành", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String code = codeField.getText();

            try {
                MajorController.updateMajor(major.getId(), name, code);
                getAllMajors(); // Refresh the list after updating the major
            } catch (Exception ex) {
                ex.printStackTrace();
                Notifications.getInstance().show(Notifications.Type.ERROR, "Error: " + ex.getMessage());
            }
        }
    }

    private void getAllMajors() {
        if (UserSession.getUser() != null && UserSession.getUser().isAdmin()) {
            try {
                List<Major> majors = MajorController.getAllMajors();
                majorTableModel.setMajors(majors);
            } catch (Exception ex) {
                ex.printStackTrace();
                Notifications.getInstance().show(Notifications.Type.ERROR, "Error: " + ex.getMessage());
            }
        } else {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Bạn không có quyền xem thông tin này.");
        }
    }

    private void deleteMajor(Major major) {
        int option = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa chuyên ngành " + major.getName() + " không?", "Xóa chuyên ngành", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                MajorController.deleteMajor(major.getId());
                getAllMajors(); // Refresh the list after deleting major
            } catch (Exception ex) {
                ex.printStackTrace();
                Notifications.getInstance().show(Notifications.Type.ERROR, "Error: " + ex.getMessage());
            }
        }
    }

    private class MajorTableModel extends AbstractTableModel {

        private final String[] columnNames = {"Tên chuyên ngành", "Mã chuyên ngành", "Hành động", ""};
        private List<Major> majors = new ArrayList<>();

        public void setMajors(List<Major> majors) {
            this.majors = majors;
            fireTableDataChanged(); // Cập nhật dữ liệu bảng khi thay đổi danh sách chuyên ngành
        }

        @Override
        public int getRowCount() {
            return majors.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Major major = majors.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return major.getName();
                case 1:
                    return major.getCode();
                case 2:
                    return "Sửa";
                case 3:
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
            return columnIndex == 2 || columnIndex == 3; // Chỉ cột Sửa và Xóa mới có thể chỉnh sửa
        }
    }

    private class ButtonRenderer extends JButton implements TableCellRenderer {
        private final String buttonType;

        public ButtonRenderer(String buttonType) {
            this.buttonType = buttonType;
            setText(buttonType);
            setFont(getFont().deriveFont(Font.BOLD));
            setFocusPainted(false);
            setForeground(UIManager.getColor("Button.foreground"));
            setBackground(UIManager.getColor("Button.background"));
            setBorder(BorderFactory.createLineBorder(UIManager.getColor("Button.borderColor")));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            if (isSelected) {
                setBackground(UIManager.getColor("Table.selectionBackground"));
                setForeground(UIManager.getColor("Table.selectionForeground"));
            } else {
                setBackground(UIManager.getColor("Button.background"));
                setForeground(UIManager.getColor("Button.foreground"));
            }
            return this;
        }
    }

    private class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private final JButton button;
        private Major currentMajor;

        public ButtonEditor(String buttonType) {
            button = new JButton(buttonType);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int row = majorTable.getSelectedRow();
                    if (row != -1) {
                        currentMajor = majorTableModel.majors.get(row);
                        if ("Sửa".equals(buttonType)) {
                            showUpdateMajorForm(currentMajor);
                        } else if ("Xóa".equals(buttonType)) {
                            deleteMajor(currentMajor);
                        }
                    }
                    // Chấm dứt quá trình chỉnh sửa và dừng cập nhật cell
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
            // Đảm bảo hành động nút được thực hiện khi nhấn vào nút
            fireEditingStopped();
            return true;
        }
    }
}
