package main.java.com.TLU.studentmanagement.view;

import main.java.com.TLU.studentmanagement.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class StudentScoreList extends JFrame {
    private JPanel pnlMain;
    private JTable table;
    private JComboBox<String> comboBox;
    private JTextField txtSearch;

    public StudentScoreList() {
        initComponents();

//        this.setContentPane(pnlMain);
        this.setTitle("Quản lý điểm sinh viên");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        // Fetch and load data from API
        loadStudentScores();
        loadSubjects();
    }

    private void initComponents() {
        JPanel pnlMain = new JPanel(new BorderLayout());

        // Panel phía trên
        JPanel pnlTop = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("Danh sách điểm của sinh viên", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        pnlTop.add(lblTitle, BorderLayout.CENTER);
        pnlMain.add(pnlTop, BorderLayout.NORTH);

        // Panel chính giữa với bảng và các nút
        JPanel pnlCenter = new JPanel(new BorderLayout());

        // Bảng
        String[] columnNames = {"Mã", "Họ Tên", "Môn học", "Mã môn", "Điểm giữa kỳ", "Điểm cuối kỳ", "Điểm tổng kết", "Tùy chọn"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Chỉ cột "Tùy chọn" có thể chỉnh sửa
            }
        };
        JTable table = new JTable(model);

        // Thêm nút vào cột "Tùy chọn"
        table.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new JCheckBox(), table));

        JScrollPane scrollPane = new JScrollPane(table);
        pnlCenter.add(scrollPane, BorderLayout.CENTER);
        pnlMain.add(pnlCenter, BorderLayout.CENTER);

        // Panel phía dưới với các chức năng tìm kiếm và lọc
        JPanel pnlBottom = new JPanel();
        comboBox = new JComboBox<>();
        txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Tìm kiếm");
        pnlBottom.add(comboBox);
        pnlBottom.add(txtSearch);
        pnlBottom.add(btnSearch);
        pnlMain.add(pnlBottom, BorderLayout.SOUTH);

        this.setContentPane(pnlMain);
    }

    private void loadSubjects() {
        String apiUrl = "https://api.yourdomain.com/subjects"; // Thay thế URL này bằng URL của API của bạn

        new Thread(() -> {
            try {
                String json = HttpUtil.sendGet(apiUrl);
                Gson gson = new Gson();
                java.lang.reflect.Type listType = new TypeToken<List<Subject>>() {}.getType();
                List<Subject> subjects = gson.fromJson(json, listType);

                SwingUtilities.invokeLater(() -> {
                    comboBox.addItem(new Subject("", "-- Tất cả --").toString()); // Thêm mục chọn mặc định
                    for (Subject subject : subjects) {
                        comboBox.addItem(subject.toString());
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

//    private void loadStudentScores() {
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url("https://api.yourdomain.com/studentscores") // Replace with your API URL
//                .build();
//
//        new Thread(() -> {
//            try (Response response = client.newCall(request).execute()) {
//                if (response.isSuccessful() && response.body() != null) {
//                    String json = response.body().string();
//                    Gson gson = new Gson();
//                    java.lang.reflect.Type listType = new TypeToken<List<StudentScore>>() {}.getType();
//                    List<StudentScore> studentScores = gson.fromJson(json, listType);
//
//                    SwingUtilities.invokeLater(() -> {
//                        DefaultTableModel model = (DefaultTableModel) table.getModel();
//                        for (StudentScore score : studentScores) {
//                            model.addRow(new Object[]{
//                                    score.getId(),
//                                    score.getName(),
//                                    score.getSubject(),
//                                    score.getSemester(),
//                                    score.getOralScore(),
//                                    score.getOneHourTestScore(),
//                                    score.getFinalExamScore(),
//                                    "Chi tiết"
//                            });
//                        }
//                    });
//                } else {
//                    JOptionPane.showMessageDialog(this, "Failed to fetch data from API");
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
//            }
//        }).start();
//    }

    private void loadStudentScores() {
        String apiUrl = "https://api.yourdomain.com/studentscores"; // Thay thế URL này bằng URL của API của bạn

        new Thread(() -> {
            try {
                String json = HttpUtil.sendGet(apiUrl);
                Gson gson = new Gson();
                java.lang.reflect.Type listType = new TypeToken<List<StudentScore>>() {}.getType();
                List<StudentScore> studentScores = gson.fromJson(json, listType);

                SwingUtilities.invokeLater(() -> {
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    for (StudentScore score : studentScores) {
                        model.addRow(new Object[]{
                                score.getId(),
                                score.getName(),
                                score.getSubject(),
                                score.getSemester(),
                                score.getOralScore(),
                                score.getOneHourTestScore(),
                                score.getFinalExamScore(),
                                "Chi tiết"
                        });
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentScoreList());
    }
}

// Custom renderer and editor classes for button in JTable
class ButtonRenderer extends JButton implements TableCellRenderer {
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
    private JButton button;
    private String label;
    private boolean isPushed;
    private JTable table; // Add table as an instance variable

    public ButtonEditor(JCheckBox checkBox, JTable table) {
        super(checkBox);
        this.table = table; // Assign the passed table to the instance variable
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            // Thực hiện hành động khi nút được nhấn
            JOptionPane.showMessageDialog(button, "Button clicked in row " + table.getSelectedRow());
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}

// Định nghĩa lớp StudentScore
class StudentScore {
    private String id;
    private String name;
    private String subject;
    private String semester;
    private int oralScore;
    private int oneHourTestScore;
    private int finalExamScore;

    // Constructors, getters, setters
    public StudentScore() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public int getOralScore() {
        return oralScore;
    }

    public void setOralScore(int oralScore) {
        this.oralScore = oralScore;
    }

    public int getOneHourTestScore() {
        return oneHourTestScore;
    }

    public void setOneHourTestScore(int oneHourTestScore) {
        this.oneHourTestScore = oneHourTestScore;
    }

    public int getFinalExamScore() {
        return finalExamScore;
    }

    public void setFinalExamScore(int finalExamScore) {
        this.finalExamScore = finalExamScore;
    }
}

class Subject {
    private String id;
    private String name;

    public Subject(String s, String s1) {
    }

    public Subject() {
    }

    // Các phương thức getter và setter cho các thuộc tính id và name

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
