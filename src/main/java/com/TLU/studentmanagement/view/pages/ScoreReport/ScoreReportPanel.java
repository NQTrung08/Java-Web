package main.java.com.TLU.studentmanagement.view.pages.ScoreReport;

import com.formdev.flatlaf.FlatLightLaf;
import main.java.com.TLU.studentmanagement.controller.semesters.SemesterController;
import main.java.com.TLU.studentmanagement.model.Semester;
import main.java.com.TLU.studentmanagement.session.UserSession;
import main.java.com.TLU.studentmanagement.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class ScoreReportPanel extends JPanel {

    private JComboBox<String> semesterComboBox;
    private JComboBox<String> programComboBox;

    public ScoreReportPanel() {
        initUI();
        loadSemesterData();
        loadProgramData();
    }

    public void initUI() {
        setLayout(new BorderLayout());
        // Apply FlatLaf theme
        FlatLightLaf.install();
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

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Thêm padding cho header

        JLabel programLabel = new JLabel("Chương trình đào tạo");
        programComboBox = new JComboBox<>(); // Khởi tạo JComboBox rỗng

        JLabel semesterLabel = new JLabel("Học kỳ - Nhóm - Năm học");
        semesterComboBox = new JComboBox<>(new String[]{"Tất cả"});

        headerPanel.add(programLabel);
        headerPanel.add(programComboBox);
        headerPanel.add(semesterLabel);
        headerPanel.add(semesterComboBox);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Thêm padding cho content

        JTable table = new JTable();
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"STT", "Mã môn học", "Tên môn học", "Số TC", "Điểm hệ 10", "Điểm hệ 4", "Điểm chữ", "Kết quả"});
        table.setModel(model);

        // Custom renderer for table header
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setOpaque(false);
        tableHeader.setBackground(new Color(0x2A3F54));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 48));
        tableHeader.setFont(new Font("Arial", Font.BOLD, 20)); // Điều chỉnh font cho tiêu đề bảng

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(new Color(0x2A3F54));
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        headerRenderer.setFont(new Font("Arial", Font.BOLD, 20)); // Điều chỉnh font cho tiêu đề bảng

        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        // Custom renderer for table cells with border
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new Font("Arial", Font.PLAIN, 14)); // Điều chỉnh font cho nội dung cell
                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                } else {
                    c.setBackground(table.getBackground());
                    c.setForeground(table.getForeground());
                }
                setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(220, 220, 220))); // Thiết lập border cho các ô
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        };
        table.setDefaultRenderer(Object.class, cellRenderer);

        // Increase row height
        table.setRowHeight(48);
        table.setBackground(Color.WHITE);
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(220, 220, 220));

        JScrollPane tableScrollPane = new JScrollPane(table);

        contentPanel.add(tableScrollPane, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        // Add some sample data to the table
        for (int i = 1; i <= 10; i++) {
            model.addRow(new Object[]{i, "Mã môn học " + i, "Tên môn học " + i, (int) (Math.random() * 4) + 2,
                    (Math.random() * 10), (Math.random() * 4), "A", "Đạt", "Ghi chú", "Chi tiết"});
        }
    }

    private void loadSemesterData() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    List<Semester> semesters = SemesterController.getAllSemesters();
                    for (Semester semester : semesters) {
                        String comboBoxItem = semester.getSemester() + " - " + semester.getGroup() + " - " + semester.getYear();
                        semesterComboBox.addItem(comboBoxItem);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void loadProgramData() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    // Lấy thông tin User hiện tại từ UserSession
                    User user = UserSession.getUser();
                    if (user != null) {
                        // Thêm thông tin chương trình đào tạo vào JComboBox
                        programComboBox.addItem(user.getMajorName());
                        System.out.println(user.getMajorId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

}
