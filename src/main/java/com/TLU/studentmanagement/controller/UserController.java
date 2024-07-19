package main.java.com.TLU.studentmanagement.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.com.TLU.studentmanagement.model.User;
import main.java.com.TLU.studentmanagement.util.HttpUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URL;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class UserController {

    private static final String BASE_URL = "http://localhost:8080/api/user/";

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            String response = HttpUtil.sendGet(BASE_URL + "getAll");
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray jsonArray = jsonResponse.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                User user = new User();
                user.setId(jsonObj.getString("_id"));
                user.setFullName(jsonObj.optString("fullname"));
                user.setMsv(jsonObj.getString("msv"));
                user.setYear(jsonObj.optString("year"));
                user.setGender(jsonObj.optString("gender"));
                user.setClassName(jsonObj.optString("class"));
                user.setEmail(jsonObj.optString("email"));
//                user.setMajorId(jsonObj.optString("major"));
                user.setAdmin(jsonObj.getBoolean("isAdmin"));
                user.setGv(jsonObj.optBoolean("isGV"));
                user.setDeleted(jsonObj.getBoolean("deleted"));

                // Kiểm tra và gán majorId nếu tồn tại và là đối tượng JSON
                if (jsonObj.has("majorId") && !jsonObj.isNull("majorId")) {
                    Object majorIdObj = jsonObj.get("majorId");
                    if (majorIdObj instanceof JSONObject) {
                        JSONObject majorIdJson = (JSONObject) majorIdObj;
                        if (majorIdJson.has("_id")) {
                            user.setMajorId(majorIdJson.getString("_id"));
                            user.setMajorName(majorIdJson.getString("name"));
                        } else {
                            user.setMajorId(""); // Gán giá trị mặc định là rỗng nếu không tìm thấy _id trong đối tượng JSON
                        }
                    } else if (majorIdObj instanceof String) {
                        user.setMajorId((String) majorIdObj);
                    } else {
                        user.setMajorId(""); // Gán giá trị mặc định là rỗng nếu majorId không phải là chuỗi hoặc đối tượng JSON
                    }
                } else {
                    user.setMajorId(""); // Gán giá trị mặc định là rỗng nếu majorId không tồn tại
                }

                // Kiểm tra và gán majorId nếu tồn tại và là đối tượng JSON
                if (jsonObj.has("gvcn") && !jsonObj.isNull("gvcn")) {
                    Object gvcnObj = jsonObj.get("gvcn");
                    if (gvcnObj instanceof JSONObject) {
                        JSONObject gvcnObjJson = (JSONObject) gvcnObj;
                        if (gvcnObjJson.has("_id")) {
                            user.setGvcn(gvcnObjJson.getString("_id"));
                            user.setGvcnName(gvcnObjJson.getString("fullname"));
                        } else {
                            user.setGvcn(""); // Gán giá trị mặc định là rỗng nếu không tìm thấy _id trong đối tượng JSON
                        }
                    } else if (gvcnObj instanceof String) {
                        user.setGvcn((String) gvcnObj);
                    } else {
                        user.setGvcn(""); // Gán giá trị mặc định là rỗng nếu majorId không phải là chuỗi hoặc đối tượng JSON
                    }
                } else {
                    user.setGvcn(""); // Gán giá trị mặc định là rỗng nếu majorId không tồn tại
                }

////                Lay thong tin major tuwf jsonObj
//                JSONObject majorObj = jsonObj.optJSONObject("majorId");
//                if(majorObj != null) {
//                    String majorName = majorObj.optString("name");
//                    user.setMajor(majorName);
//                }
//
//                // Lấy thông tin gvcn từ đối tượng jsonObj
//                JSONObject gvcnObj = jsonObj.optJSONObject("gvcn");
//                if (gvcnObj != null) {
//                    String gvcnFullName = gvcnObj.optString("fullname");
//                    user.setGvcn(gvcnFullName);
//                }

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


    public static void createUser(User user) {
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

            System.out.println(jsonObj);
//            System.out.println("MajorId: " + user.getMajorId());
//            System.out.println("GVCN: " + user.getGvcn());

            String response = HttpUtil.sendPost(BASE_URL + "create-user", jsonObj.toString());
            JSONObject responseJson = new JSONObject(response);

            System.out.println("API response: " + responseJson);

//            JOptionPane.showMessageDialog(null, responseJson.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tạo người dùng: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tạo người dùng: " + e.getMessage());
        }
    }

    public static void updateUser(String userId, User user) {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("gvcn", user.getGvcn());
            jsonObj.put("majorId", user.getMajorId());

            System.out.println("gvcnId: " + user.getGvcn());
            System.out.println("majorId: " + user.getMajorId());

            String response = HttpUtil.sendPut(BASE_URL + "updateByAdmin/" + userId, jsonObj.toString());
            JSONObject responseJson = new JSONObject(response);

            System.out.println("API response: " + response);
//            System.out.println("UserId: " + userId);

//            JOptionPane.showMessageDialog(null, responseJson.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật người dùng: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật người dùng: " + e.getMessage());
        }
    }

    public static void deleteUser(String userId) {
        try {
            String response = HttpUtil.sendDelete(BASE_URL + "delete/" + userId);
            JSONObject responseJson = new JSONObject(response);

//            JOptionPane.showMessageDialog(null, responseJson.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi xóa người dùng: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi xóa người dùng: " + e.getMessage());
        }
    }

    public void restoreUser(String msv) {
        try {
            String response = HttpUtil.sendPut(BASE_URL + "restore/" + msv, null);
            JSONObject responseJson = new JSONObject(response);

            JOptionPane.showMessageDialog(null, responseJson.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi khôi phục người dùng: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi khôi phục người dùng: " + e.getMessage());
        }
    }

//    public List<String> getAllTeachers() {
//        List<String> teachers = new ArrayList<>();
//        try {
//            String response = HttpUtil.sendGet("http://localhost:8080/api/teacher/getAll");
//            JSONObject jsonResponse = new JSONObject(response);
//            JSONArray jsonArray = jsonResponse.getJSONArray("data");
//
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObj = jsonArray.getJSONObject(i);
//                teachers.add(jsonObj.getString("fullname"));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Lỗi khi phân tích dữ liệu JSON: " + e.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Lỗi khi tải danh sách giáo viên: " + e.getMessage());
//        }
//        return teachers;
//    }
//
//    public List<String> getAllMajors() {
//        List<String> majors = new ArrayList<>();
//        try {
//            String response = HttpUtil.sendGet("http://localhost:8080/api/major/getAll");
//            JSONObject jsonResponse = new JSONObject(response);
//            JSONArray jsonArray = jsonResponse.getJSONArray("data");
//
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObj = jsonArray.getJSONObject(i);
//                majors.add(jsonObj.getString("name"));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Lỗi khi phân tích dữ liệu JSON: " + e.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Lỗi khi tải danh sách chuyên ngành: " + e.getMessage());
//        }
//        return majors;
//    }

//    public List<User> getAllTeacher() {
//        List<User> teachers = new ArrayList<>();
//        try {
//            String response = HttpUtil.sendGet("http://localhost:8080/api/user/getAll");
//
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode node = mapper.readTree(response).get("data");
//
//            for (JsonNode objNode : node) {
//                if (objNode.has("isGV") && objNode.get("isGV").asBoolean()) {
//                    User teacher = mapper.treeToValue(objNode, User.class);
//                    teachers.add(teacher);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return teachers;
//    }
//
//    public List<String[]> getAllMajor() {
//        List<String[]> majors = new ArrayList<>();
//        try {
//            String response = HttpUtil.sendGet("http://localhost:8080/api/majors/getAll");
//
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode node = mapper.readTree(response).get("data");
//
//            for (JsonNode objNode : node) {
//                String id = objNode.get("_id").asText();
//                String name = objNode.get("name").asText();
//                majors.add(new String[]{id, name});
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return majors;
//    }

    public static List<User> searchStudents(String keyword) {
        List<User> students = new ArrayList<>();
        String apiUrl = BASE_URL + "searchStudents/?keyword=" + keyword;

        try {
            String response = HttpUtil.sendPost(apiUrl, null); // Since no requestData is needed for this endpoint
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray jsonArray = jsonResponse.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                User user = new User();
                user.setFullName(jsonObj.optString("fullname"));
                user.setMsv(jsonObj.getString("msv"));
                user.setYear(jsonObj.optString("year"));
                user.setClassName(jsonObj.optString("class"));
                user.setEmail(jsonObj.optString("email"));
                students.add(user);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error parsing JSON data: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error searching students: " + e.getMessage());
        }

        return students;
    }
}
