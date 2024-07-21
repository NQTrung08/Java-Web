package main.java.com.TLU.studentmanagement.controller.teacher;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.com.TLU.studentmanagement.model.Teacher;
import main.java.com.TLU.studentmanagement.util.HttpUtil;
import org.json.JSONArray;
import org.json.JSONObject;

public class TeacherController {

    private static final String BASE_URL = "http://localhost:8080/api/teacher/";

    public static List<Teacher> getAllTeachers() {
        String apiUrl = "http://localhost:8080/api/teacher/getAll";
        try {
            String response = HttpUtil.sendGet(apiUrl);

            // Process JSON response
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray teachersArray = jsonResponse.getJSONArray("data");
            List<Teacher> teachers = new ArrayList<>();

            for (int i = 0; i < teachersArray.length(); i++) {
                JSONObject teacherObject = teachersArray.getJSONObject(i);
                Teacher teacher = new Teacher();
                teacher.setId(teacherObject.getString("_id"));
                teacher.setFullName(teacherObject.optString("fullname", "n/a"));
                teacher.setEmail(teacherObject.optString("email", "n/a"));
                teacher.setAdmin(teacherObject.getBoolean("isAdmin"));
                teacher.setGV(teacherObject.optBoolean("isGV"));
                // Add other properties as needed
                teachers.add(teacher);
            }

            return teachers;
        } catch (Exception e) {
            // Handle specific exceptions here
            e.printStackTrace(); // Log the exception or handle it according to your application's needs
            return new ArrayList<>(); // Return an empty list or handle gracefully in case of error
        }
    }

    public static Teacher getTeacherById(String id) throws Exception {
        String response = HttpUtil.sendGet(BASE_URL + id);
        JSONObject jsonResponse = new JSONObject(response);
        JSONObject data = jsonResponse.getJSONObject("data");

        Teacher teacher = new Teacher();
        teacher.setId(data.getString("_id"));
        teacher.setMgv(data.getString("mgv"));
        teacher.setFullName(data.getString("fullname"));
        return teacher;
    }

    public static void createTeacher(String mgv, String fullName) throws Exception {
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("mgv", mgv);
        jsonInput.put("fullname", fullName);

        HttpUtil.sendPost(BASE_URL + "create-teacher", jsonInput.toString());
    }

    public static void updateTeacher(String id, String mgv, String fullName) throws Exception {
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("mgv", mgv);
        jsonInput.put("fullname", fullName);

        HttpUtil.sendPut(BASE_URL + "update/" + id, jsonInput.toString());
    }

    public static void deleteTeacher(String id) throws Exception {
        HttpUtil.sendDelete(BASE_URL + "delete/" + id);
    }
}
