package main.java.com.TLU.studentmanagement.controller;

import main.java.com.TLU.studentmanagement.model.User;
import main.java.com.TLU.studentmanagement.util.HttpUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class UserController {

    // Phương thức lấy danh sách tất cả người dùng
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            String response = HttpUtil.sendGet("http://localhost:8080/api/user/getAll");
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray jsonArray = jsonResponse.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                User user = new User();
                user.setId(jsonObj.getString("_id"));
                user.setFullName(jsonObj.optString("fullname"));  // Sử dụng optString để tránh JSONException nếu không có trường này
                user.setMsv(jsonObj.getString("msv"));
                user.setYear(jsonObj.optString("year"));
                user.setGvcn(jsonObj.optString("gvcn"));
                user.setGender(jsonObj.optString("gender"));
                user.setClassName(jsonObj.optString("class"));
                user.setEmail(jsonObj.optString("email"));
                user.setMajorId(jsonObj.optString("major")); // Đảm bảo bạn đã cập nhật tên trường nếu cần
                user.setAdmin(jsonObj.getBoolean("isAdmin"));
                user.setGv(jsonObj.optBoolean("isGV"));
                user.setDeleted(jsonObj.getBoolean("deleted"));
                users.add(user);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi phân tích dữ liệu JSON: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải danh sách người dùng: " + e.getMessage());
        }
        return users;
    }


    // Phương thức tạo người dùng mới
    public void createUser(User user) {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("fullname", user.getFullName());
            jsonObj.put("msv", user.getMsv());
            jsonObj.put("year", user.getYear());
            jsonObj.put("gvcn", user.getGvcn());
            jsonObj.put("gender", user.getGender());
            jsonObj.put("className", user.getClassName());
            jsonObj.put("email", user.getEmail());
            jsonObj.put("majorId", user.getMajorId());

            String response = HttpUtil.sendPost("http://localhost:8080/api/user/create-user", jsonObj.toString());
            JSONObject responseJson = new JSONObject(response);

            JOptionPane.showMessageDialog(null, responseJson.getString("message"));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tạo người dùng: " + e.getMessage());
        }
    }

    // Phương thức cập nhật thông tin người dùng
    public void updateUser(String userId, User user) {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("fullname", user.getFullName());
            jsonObj.put("msv", user.getMsv());
            jsonObj.put("year", user.getYear());
            jsonObj.put("gvcn", user.getGvcn());
            jsonObj.put("gender", user.getGender());
            jsonObj.put("className", user.getClassName());
            jsonObj.put("email", user.getEmail());
            jsonObj.put("majorId", user.getMajorId());

            String response = HttpUtil.sendPut("http://localhost:8080/api/user/updateByAdmin/" + userId, jsonObj.toString());
            JSONObject responseJson = new JSONObject(response);

            JOptionPane.showMessageDialog(null, responseJson.getString("message"));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật người dùng: " + e.getMessage());
        }
    }

    // Phương thức xóa người dùng
    public void deleteUser(String userId) {
        try {
            String response = HttpUtil.sendDelete("http://localhost:8080/api/user/delete/" + userId);
            JSONObject responseJson = new JSONObject(response);

            JOptionPane.showMessageDialog(null, responseJson.getString("message"));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi xóa người dùng: " + e.getMessage());
        }
    }

    // Phương thức khôi phục người dùng đã xóa
    public void restoreUser(String userId) {
        try {
            String response = HttpUtil.sendPut("http://localhost:8080/api/user/restore/" + userId, null);
            JSONObject responseJson = new JSONObject(response);

            JOptionPane.showMessageDialog(null, responseJson.getString("message"));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi khôi phục người dùng: " + e.getMessage());
        }
    }

    // Phương thức lấy danh sách giáo viên từ API hoặc database
    public List<String> getAllTeachers() {
        List<String> teachers = new ArrayList<>();
        try {
            String response = HttpUtil.sendGet("http://localhost:8080/api/teacher/getAll");
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray jsonArray = jsonResponse.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                teachers.add(jsonObj.getString("fullname"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi phân tích dữ liệu JSON: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải danh sách giáo viên: " + e.getMessage());
        }
        return teachers;
    }

    // Phương thức lấy danh sách chuyên ngành từ API hoặc database
    public List<String> getAllMajors() {
        List<String> majors = new ArrayList<>();
        try {
            String response = HttpUtil.sendGet("http://localhost:8080/api/major/getAll");
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray jsonArray = jsonResponse.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                majors.add(jsonObj.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi phân tích dữ liệu JSON: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải danh sách chuyên ngành: " + e.getMessage());
        }
        return majors;
    }

}
