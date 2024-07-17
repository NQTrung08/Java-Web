package main.java.com.TLU.studentmanagement.controller.teacher;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.com.TLU.studentmanagement.model.Teacher;
import main.java.com.TLU.studentmanagement.util.HttpUtil;
import org.json.JSONObject;

public class TeacherController {

    private static final String API_URL = "http://localhost:8080/api/teacher/getAll";

    public static List<Teacher> getAllTeachers() throws Exception {
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            ObjectMapper mapper = new ObjectMapper();
            List<Teacher> teacherList = mapper.readValue(response.toString(),
                    mapper.getTypeFactory().constructCollectionType(List.class, Teacher.class));
            return teacherList;
        } else {
            throw new Exception("GET request not worked");
        }
    }

    public static Teacher getTeacherById(String id) throws Exception {
        String apiUrl = "http://localhost:8080/api/teacher/" + id;
        String response = HttpUtil.sendGet(apiUrl);
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

        String apiUrl = "http://localhost:8080/api/teacher/create-teacher";
        HttpUtil.sendPost(apiUrl, jsonInput.toString());
    }

//    Ham update giang vien (chua co backend)
//    public static void updateTeacher(String id, String mgv, String fullName) throws Exception {
//        JSONObject jsonInput = new JSONObject();
//        jsonInput.put("mgv", mgv);
//        jsonInput.put("fullname", fullName);
//
//        String apiUrl = "http://localhost:8080/api/teacher/update/" + id;
//        HttpUtil.sendPut(apiUrl, jsonInput.toString());
//    }

//    Ham xoa giang vien (chua co backend)
//    public static void deleteTeacher(String id) throws Exception {
//        String apiUrl = "http://localhost:8080/api/teacher/delete/" + id;
//        HttpUtil.sendDelete(apiUrl);
//    }

}
