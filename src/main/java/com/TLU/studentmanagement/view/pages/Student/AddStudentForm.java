//package main.java.com.TLU.studentmanagement.view.pages.Student;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.ArrayList;
//import java.util.List;
//
//import main.java.com.TLU.studentmanagement.model.Teacher;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import main.java.com.TLU.studentmanagement.util.HttpUtil;
//
//public class AddStudentForm extends JFrame {
//
//    private JTextField fullNameField;
//    private JTextField msvField;
//    private JTextField majorField;
//    private JTextField yearField;
//    private JComboBox<Teacher> gvcnComboBox;
//    private JTextField genderField;
//    private JTextField classNameField;
//    private JTextField emailField;
//    private String adminToken; // Token xác thực admin
//    private List<Teacher> teachers;
//
//    public AddStudentForm(String adminToken) {
//        this.adminToken = adminToken; // Lưu token
//        teachers = fetchTeachers(); // Lấy danh sách giáo viên từ API
//
//        setTitle("Add Student");
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setSize(400, 400);
//        setLocationRelativeTo(null);
//        setLayout(new GridLayout(9, 2, 10, 10));
//
//        // Form fields
//        add(new JLabel("Full Name:"));
//        fullNameField = new JTextField();
//        add(fullNameField);
//
//        add(new JLabel("MSV:"));
//        msvField = new JTextField();
//        add(msvField);
//
//        add(new JLabel("Major:"));
//        majorField = new JTextField();
//        add(majorField);
//
//        add(new JLabel("Year:"));
//        yearField = new JTextField();
//        add(yearField);
//
//        add(new JLabel("GVCN:"));
//        gvcnComboBox = new JComboBox<>();
//        for (Teacher teacher : teachers) {
//            gvcnComboBox.addItem(teacher);
//        }
//        add(gvcnComboBox);
//
//        add(new JLabel("Gender:"));
//        genderField = new JTextField();
//        add(genderField);
//
//        add(new JLabel("Class Name:"));
//        classNameField = new JTextField();
//        add(classNameField);
//
//        add(new JLabel("Email:"));
//        emailField = new JTextField();
//        add(emailField);
//
//        // Submit button
//        JButton submitButton = new JButton("Submit");
//        submitButton.addActionListener(new SubmitButtonListener());
//        add(submitButton);
//
//        setVisible(true);
//    }
//
//    // Phương thức để lấy danh sách giáo viên từ API
//    public List<Teacher> fetchTeachers() {
//        List<Teacher> teacherList = new ArrayList<>();
//        try {
//            String apiUrl = "http://localhost:8080/api/teacher/get-all";
//            String response = HttpUtil.sendGet(apiUrl);
//            System.out.println("API Response: " + response); // In ra phản hồi từ API để kiểm tra
//
//            // Chuyển đổi phản hồi JSON thành đối tượng JSON
//            JSONObject jsonResponse = new JSONObject(response);
//            JSONArray jsonArray = jsonResponse.getJSONArray("data");
//
//            // Lặp qua mỗi đối tượng JSON trong mảng
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                // Lấy giá trị của thuộc tính "fullname" và "mgv"
//                String _id = jsonObject.getString("_id");
//
//                String fullName = jsonObject.getString("fullname");
//                String mgv = jsonObject.getString("mgv");
//
//                // Tạo đối tượng Teacher và thêm vào danh sách teacherList
//                teacherList.add(new Teacher(_id, mgv, fullName));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return teacherList;
//    }
//
//    private class SubmitButtonListener implements ActionListener {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            try {
//                String apiUrl = "http://localhost:8080/api/user/create-user";
//                JSONObject jsonInput = new JSONObject();
//                jsonInput.put("fullName", fullNameField.getText());
//                jsonInput.put("msv", msvField.getText());
//                jsonInput.put("major", majorField.getText());
//                jsonInput.put("year", yearField.getText());
//
//                // Lấy Teacher từ gvcnComboBox và lấy mgv của nó
//                Teacher selectedTeacher = (Teacher) gvcnComboBox.getSelectedItem();
//                jsonInput.put("gvcn", selectedTeacher.getMgv());
//
//                jsonInput.put("gender", genderField.getText());
//                jsonInput.put("className", classNameField.getText());
//                jsonInput.put("email", emailField.getText());
//
//                String requestData = jsonInput.toString();
//                System.out.print(requestData);
//                String response = HttpUtil.sendPost(apiUrl, requestData, adminToken);
//
//                JOptionPane.showMessageDialog(null, "Student added successfully!");
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
//            }
//        }
//    }
//
//    public static void main(String[] args) {
//        // Chạy ứng dụng với một token giả định (thay thế token thực tế của bạn)
//        String adminToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY2OGJiNWVlNjBhOTdhYmQxN2NhMTM1MCIsImlzQWRtaW4iOnRydWUsImlhdCI6MTcyMDYyMjQ3NiwiZXhwIjoxNzIwODgxNjc2fQ.PCF_OgbcFepaG-ETuxP9XVmebV4m6n7W6dVx6q0-ghg";
//        SwingUtilities.invokeLater(() -> new AddStudentForm(adminToken));
//    }
//}
//
